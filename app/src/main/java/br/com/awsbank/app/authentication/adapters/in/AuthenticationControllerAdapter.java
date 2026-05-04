package br.com.awsbank.app.authentication.adapters.in;

import br.com.awsbank.app.authentication.domain.services.AuthenticationService;
import br.com.awsbank.app.authentication.ports.in.AuthenticationRestPort;
import br.com.awsbank.app.authentication.ports.in.dtos.ResendEmailDtoIn;
import br.com.awsbank.app.authentication.ports.in.dtos.SignUpDtoIn;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationControllerAdapter implements AuthenticationRestPort {

    private final AuthenticationService authenticationService;

    @Override
    public void signUp(SignUpDtoIn signUpDtoIn) {
        authenticationService.signUp(signUpDtoIn.toDomain());
    }

    @Override
    public void resendEmailVerification(ResendEmailDtoIn resendEmailDtoIn) {
        authenticationService.resendEmailVerification(resendEmailDtoIn.email());
    }
}
