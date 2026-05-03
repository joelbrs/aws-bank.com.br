package br.com.awsbank.authorizer;

import br.com.awsbank.authorizer.exceptions.AuthorizerException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.auth0.jwk.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.concurrent.TimeUnit;

enum Environment {
    LOCAL, DEV, PROD;

    public static Environment fromString(String env) {
        try {
            return Environment.valueOf(env.toUpperCase());
        } catch (Exception e) {
            return LOCAL;
        }
    }
}

public class KeycloakAuthorizerHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private static final Environment ENVIRONMENT = Environment.fromString(System.getenv().getOrDefault("ENVIRONMENT", "LOCAL"));

    private static final String KEYCLOAK_URL =
            System.getenv().getOrDefault("KEYCLOAK_URL", "http://keycloak.localhost.localstack.cloud:4566");

    private static final String KEYCLOAK_REALM =
            System.getenv().getOrDefault("KEYCLOAK_REALM", "localstack");

    private static final String EXPECTED_AUDIENCE =
            System.getenv().getOrDefault("EXPECTED_AUDIENCE", "localstack-client");

    private static final String ISSUER =
            KEYCLOAK_URL + "/realms/" + KEYCLOAK_REALM;

    // JWKS Provider's Cache
    private static final JwkProvider jwkProvider;

    static {
        try {
            String jwksUrl = ISSUER + "/protocol/openid-connect/certs";

            jwkProvider = new GuavaCachedJwkProvider(
                    new UrlJwkProvider(new URL(jwksUrl)),
                    10,
                    24,
                    TimeUnit.HOURS
            );

        } catch (Exception e) {
            throw new AuthorizerException("Erro ao inicializar JWK provider", e);
        }
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> event, Context context) {
        context.getLogger().log("Authorizer event: " + event);

        try {
            String token = (String) event.getOrDefault("authorizationToken", "");

            if (token.isEmpty()) {
                context.getLogger().log("Authorization token is empty");
                return generatePolicy("anonymous", "Deny", (String) event.get("methodArn"), null);
            }

            if (token.toLowerCase().startsWith("bearer ")) {
                token = token.substring(7);
            }

            context.getLogger().log("Authorization token: " + token);

            DecodedJWT jwt = validateToken(token, context);

            String subject = jwt.getSubject();
            String username = jwt.getClaim("preferred_username").asString();
            String email = jwt.getClaim("email").asString();

            List<String> roles = extractRoles(jwt);

            context.getLogger().log("Authorized user: " + username + ", roles: " + roles);

            Map<String, Object> contextData = new HashMap<>();
            contextData.put("sub", subject);
            contextData.put("username", username);
            contextData.put("email", email);
            contextData.put("roles", String.join(",", roles));

            return generatePolicy(subject, "Allow", (String) event.get("methodArn"), contextData);
        } catch (Exception e) {
            context.getLogger().log("Authorization failed: " + e.getMessage());
            return generatePolicy("anonymous", "Deny", (String) event.get("methodArn"), null);
        }
    }

    private DecodedJWT validateToken(String token, Context context) throws Exception {
        context.getLogger().log("Validating token: " + token);

        DecodedJWT decoded = JWT.decode(token);
        context.getLogger().log("Decoded token: " + decoded);

        String kid = decoded.getKeyId();
        context.getLogger().log("Token Kid: " + kid);

        Jwk jwk = jwkProvider.get(kid);
        RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();

        Algorithm algorithm = Algorithm.RSA256(publicKey, null);

        context.getLogger().log("Algorithm: " + algorithm);
        context.getLogger().log("Issuer: " + ISSUER);
        context.getLogger().log("Expected Audience: " + EXPECTED_AUDIENCE);

        boolean isStrictValidation = ENVIRONMENT == Environment.PROD;

        if (!isStrictValidation) {
            context.getLogger().log("Performing relaxed validation (non-PROD environment)");

            if (!decoded.getIssuer().equals(ISSUER)) {
                context.getLogger().log("Skipping issuer validation (non-PROD environment)");
            }

            if (!decoded.getClaim("azp").asString().equals(EXPECTED_AUDIENCE)) {
                context.getLogger().log("Skipping audience validation (non-PROD environment)");
            }

            return JWT.require(algorithm)
                    .withIssuer("http://localhost:8080/realms/localstack")
                    .withClaim("azp", EXPECTED_AUDIENCE)
                    .build()
                    .verify(token);
        }

        context.getLogger().log("Performing strict validation (PROD environment)");

        return JWT.require(algorithm)
                .withIssuer(ISSUER)
                .withClaim("azp", EXPECTED_AUDIENCE)
                .build()
                .verify(token);
    }

    private List<String> extractRoles(DecodedJWT jwt) {
        List<String> roles = new ArrayList<>();

        Map<String, Object> realmAccess = jwt.getClaim("realm_access").asMap();
        if (realmAccess != null && realmAccess.get("roles") instanceof List) {
            roles.addAll((List<String>) realmAccess.get("roles"));
        }

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access").asMap();
        if (resourceAccess != null) {
            for (Map.Entry<String, Object> entry : resourceAccess.entrySet()) {
                String client = entry.getKey();
                Map<String, Object> access = (Map<String, Object>) entry.getValue();

                List<String> clientRoles = (List<String>) access.get("roles");
                if (clientRoles != null) {
                    for (String role : clientRoles) {
                        roles.add(client + ":" + role);
                    }
                }
            }
        }

        return roles;
    }

    private Map<String, Object> generatePolicy(
            String principalId,
            String effect,
            String resource,
            Map<String, Object> context
    ) {

        Map<String, Object> policy = new HashMap<>();
        policy.put("principalId", principalId);

        Map<String, Object> policyDocument = new HashMap<>();
        policyDocument.put("Version", "2012-10-17");

        Map<String, Object> statement = new HashMap<>();
        statement.put("Action", "execute-api:Invoke");
        statement.put("Effect", effect);
        statement.put("Resource", resource);

        policyDocument.put("Statement", List.of(statement));

        policy.put("policyDocument", policyDocument);

        if (context != null) {
            Map<String, String> ctx = new HashMap<>();
            for (Map.Entry<String, Object> e : context.entrySet()) {
                ctx.put(e.getKey(), String.valueOf(e.getValue()));
            }
            policy.put("context", ctx);
        }

        return policy;
    }
}
