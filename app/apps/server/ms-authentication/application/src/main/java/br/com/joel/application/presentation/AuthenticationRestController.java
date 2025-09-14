package br.com.joel.application.presentation;

import br.com.joel.application.presentation.dtos.authentication.ConfirmSignUpDtoIn;
import br.com.joel.application.presentation.dtos.authentication.SignInDto;
import br.com.joel.application.presentation.dtos.authentication.SignUpDtoIn;
import br.com.joel.application.utils.CookiesUtils;
import br.com.joel.domain.domain.Token;
import br.com.joel.domain.domain.enums.TokenType;
import br.com.joel.exceptions.UnauthorizedException;
import br.com.joel.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationRestController {

    private final AuthenticationService authenticationService;

    @PostMapping("/v1/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody SignUpDtoIn dto) {
        authenticationService.signUp(dto.toDomain());
    }

    @PostMapping("/v1/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmSignUp(@Valid @RequestBody ConfirmSignUpDtoIn dto) {
        authenticationService.confirmSignUp(dto.taxId(), dto.totpCode());
    }

    @PostMapping("/v1/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public void signIn(@Valid @RequestBody SignInDto dto, HttpServletResponse response) {
        this.defineCookies(response, authenticationService.signIn(dto.taxId(), dto.password()));
    }

    @PostMapping("/v1/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        this.defineCookies(response, authenticationService.refreshToken(this.getToken(request, TokenType.REFRESH_TOKEN)));
    }

    @PostMapping("v1/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        List<Cookie> cookies = CookiesUtils
                .clearCookies(List.of(TokenType.ACCESS_TOKEN.getCookieName(), TokenType.REFRESH_TOKEN.getCookieName()));

        cookies.forEach(response::addCookie);
    }

    @PostMapping("/v1/sign-in-actions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signInActions(@RequestParam String taxId, @RequestParam String actionsPassword, HttpServletResponse response) {
        String actionsToken = authenticationService.signInActions(taxId, actionsPassword);

        Cookie actionsTokenCookie =
                CookiesUtils.generateHttpOnlyCookie(TokenType.ACTIONS_TOKEN.getCookieName(), actionsToken, this.getExpiration(actionsToken));

        response.addCookie(actionsTokenCookie);
    }

    @PostMapping("/v1/validate-token")
    @ResponseStatus(HttpStatus.OK)
    public Boolean validateToken(@RequestParam TokenType tokenType, HttpServletRequest request) {
        return authenticationService.isTokenValid(this.getToken(request, tokenType), tokenType);
    }

    private void defineCookies(HttpServletResponse response, Token token) {
        String accessToken = token.getAccessToken();
        String refreshToken = token.getRefreshToken();

        Cookie accessTokenCookie =
                CookiesUtils.generateHttpOnlyCookie(TokenType.ACCESS_TOKEN.getCookieName(), accessToken, this.getExpiration(accessToken));

        Cookie refreshTokenCookie =
                CookiesUtils.generateHttpOnlyCookie(TokenType.REFRESH_TOKEN.getCookieName(), refreshToken, this.getExpiration(refreshToken));

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    private int getExpiration(String token) {
        return (int) ((authenticationService.getExpiration(token).getTime() - System.currentTimeMillis()) / 1000);
    }

    private String getToken(HttpServletRequest request, TokenType tokenType) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(tokenType.getCookieName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new UnauthorizedException("Refresh token not found in cookies.");
    }
}
