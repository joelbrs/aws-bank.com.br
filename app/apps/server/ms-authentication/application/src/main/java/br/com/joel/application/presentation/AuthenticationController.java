package br.com.joel.application.presentation;

import br.com.joel.application.presentation.dtos.authentication.ConfirmSignUpDtoIn;
import br.com.joel.application.presentation.dtos.authentication.SignUpDtoIn;
import br.com.joel.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/v1")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody SignUpDtoIn dto) {
        authenticationService.signUp(dto.toDomain());
    }

    @PostMapping("/v1/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmSignUp(@Valid @RequestBody ConfirmSignUpDtoIn dto) {
        authenticationService.confirmSignUp(dto.taxId(), dto.totpCode());
    }
}
