package br.com.joel.application.infrastructure.adapters;

import br.com.joel.application.config.properties.SesProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSESAdapterTest {

    @Mock
    private SesProperties sesProperties;
    @Mock
    private MailSender mailSender;

    @InjectMocks
    private EmailSESAdapter emailSESAdapter;

    @Test
    void send_deveEnviarEmailComParametrosCorretos() {
        String to = "destino@email.com";
        String subject = "Assunto";
        String body = "Corpo do e-mail";
        String from = "remetente@email.com";

        when(sesProperties.getFrom()).thenReturn(from);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailSESAdapter.send(to, subject, body);

        verify(mailSender, times(1)).send(captor.capture());
        SimpleMailMessage sentMessage = captor.getValue();

        assertEquals(from, sentMessage.getFrom());
        assertArrayEquals(new String[]{to}, sentMessage.getTo());
        assertEquals(subject, sentMessage.getSubject());
        assertEquals(body, sentMessage.getText());
    }
}