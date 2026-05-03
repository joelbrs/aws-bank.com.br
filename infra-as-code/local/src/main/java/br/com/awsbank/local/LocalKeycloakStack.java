package br.com.awsbank.local;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

import java.util.List;
import java.util.Map;

public class LocalKeycloakStack extends Stack {
    public LocalKeycloakStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public LocalKeycloakStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Function authorizerFn = Function.Builder
                .create(this, "AuthorizerFunction")
                .functionName("keycloak-jwt-authorizer")
                .runtime(Runtime.JAVA_21)
                .handler("br.com.awsbank.authorizer.KeycloakAuthorizerHandler::handleRequest")
                .code(Code.fromAsset("../../authorizer/target/authorizer-1.0.jar"))
                .timeout(Duration.seconds(10))
                .memorySize(256)
                .environment(Map.of(
                        "KEYCLOAK_URL", "http://keycloak.localhost.localstack.cloud:4566",
                        "KEYCLOAK_REALM", "localstack",
                        "KEYCLOAK_CLIENT_ID", "localstack-client"
                    )
                )
                .build();

        RestApi api = RestApi.Builder
                .create(this, "LocalKeycloakApi")
                .restApiName("Local Keycloak API")
                .description("Keycloak API JWT authentication")
                .deployOptions(StageOptions.builder()
                        .stageName("prod")
                        .build())
                .build();

        TokenAuthorizer authorizer = TokenAuthorizer.Builder.create(this, "KeycloakAuthorizer")
                .handler(authorizerFn)
                .identitySource("method.request.header.Authorization")
                .resultsCacheTtl(Duration.seconds(0))
                .build();

        MockIntegration mockIntegration = MockIntegration.Builder.create()
                .integrationResponses(List.of(
                        IntegrationResponse.builder()
                                .statusCode("200")
                                .build()
                ))
                .passthroughBehavior(PassthroughBehavior.NEVER)
                .requestTemplates(Map.of(
                        "application/json", "{\"statusCode\": 200}"
                ))
                .build();

        Resource users = api.getRoot().addResource("users");

        users.addMethod("GET", mockIntegration, MethodOptions.builder()
                .authorizer(authorizer)
                .authorizationType(AuthorizationType.CUSTOM)
                .build());
    }
}
