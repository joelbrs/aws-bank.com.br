package br.com.joel.services;

import br.com.joel.domain.domain.User;
import br.com.joel.domain.domain.UserPassword;
import br.com.joel.exceptions.BusinessException;
import br.com.joel.exceptions.ExternalServiceException;
import br.com.joel.ports.database.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPasswordService userPasswordService;
    @Mock
    private AccountService accountService;
    @Mock
    private TOTPService totpService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(
                userRepository,
                userPasswordService,
                totpService,
                accountService
        );
    }

    @Test
    void createUser_success() throws Exception {
        User user =  new User();
        user.setTaxId("12345678901");
        UserPassword password = new UserPassword();
        password.setLoginPassword("senha");
        password.setActionsPassword("outraSenha");
        user.setPassword(password);

        doNothing().when(totpService).sendTOTP(anyString(), any());

        userService.createUser(user);

        verify(userRepository).create(any(User.class));
        verify(userPasswordService).create(any(UserPassword.class));
    }

    @Test
    void createUser_on_business_exception() {
        User user = new User();
        user.setTaxId("12345678901");
        UserPassword password = new UserPassword();
        password.setLoginPassword("senha");
        password.setActionsPassword("outraSenha");
        user.setPassword(password);

        doThrow(BusinessException.class).when(userPasswordService).create(any());

        BusinessException ex = assertThrows(BusinessException.class, () -> userService.createUser(user));
        assertTrue(ex.getMessage().contains("creating user"));
    }

    @Test
    void createUser_on_exception() {
        User user = new User();
        user.setTaxId("12345678901");
        UserPassword password = new UserPassword();
        password.setLoginPassword("senha");
        password.setActionsPassword("outraSenha");
        user.setPassword(password);

        doThrow(RuntimeException.class).when(userRepository).create(any());

        ExternalServiceException ex = assertThrows(ExternalServiceException.class, () -> userService.createUser(user));
        assertTrue(ex.getMessage().contains("creating user"));
    }

    @Test
    void confirmUser_success() {
        when(userRepository.existsByTaxId("cpf")).thenReturn(true);
        doNothing().when(totpService).validateTOTP(anyString(), anyString());

        userService.confirmUser("cpf", "123");

        verify(userRepository).confirm("cpf");
        verify(accountService).createAccount("cpf");
    }

    @Test
    void confirmUser_notFound() {
        when(userRepository.existsByTaxId("cpf")).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class, () -> userService.confirmUser("cpf", "123"));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void confirmUser_totpException() {
        when(userRepository.existsByTaxId("cpf")).thenReturn(true);
        doThrow(new IllegalArgumentException("fail")).when(totpService).validateTOTP(anyString(), anyString());

        Assertions.assertThrows(ExternalServiceException.class, () -> userService.confirmUser("cpf", "123"));

        verify(userRepository, never()).confirm("cpf");
        verify(accountService, never()).createAccount("cpf");
    }
}
