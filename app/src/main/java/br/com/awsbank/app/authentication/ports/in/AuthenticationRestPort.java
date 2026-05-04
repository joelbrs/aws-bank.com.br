package br.com.awsbank.app.authentication.ports.in;

import br.com.awsbank.app.authentication.ports.in.dtos.ResendEmailDtoIn;
import br.com.awsbank.app.authentication.ports.in.dtos.SignUpDtoIn;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/authentication")
public interface AuthenticationRestPort {

    @PostMapping(value = "/signup")
    @ResponseStatus(HttpStatus.CREATED)
    void signUp(@RequestBody @Valid SignUpDtoIn signUpDtoIn);

    @PostMapping(value = "/resend-email-verification")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void resendEmailVerification(@RequestBody @Valid ResendEmailDtoIn resendEmailDtoIn);
}
