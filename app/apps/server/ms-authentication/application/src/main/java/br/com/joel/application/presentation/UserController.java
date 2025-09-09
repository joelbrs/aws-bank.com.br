package br.com.joel.application.presentation;

import br.com.joel.application.presentation.dtos.user.ConfirmUserDtoIn;
import br.com.joel.application.presentation.dtos.user.CreateUserDtoIn;
import br.com.joel.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/v1")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody CreateUserDtoIn dto) {
        userService.createUser(dto.toDomain());
    }

    @PostMapping("/v1/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmUser(@Valid @RequestBody ConfirmUserDtoIn dto) {
        userService.confirmUser(dto.taxId(), dto.totpCode());
    }
}
