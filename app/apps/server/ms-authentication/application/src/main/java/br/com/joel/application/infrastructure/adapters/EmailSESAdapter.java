package br.com.joel.application.infrastructure.adapters;

import br.com.joel.application.config.properties.SesProperties;
import br.com.joel.ports.EmailPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(SesProperties.class)
public class EmailSESAdapter implements EmailPort {

    private final SesProperties sesProperties;
    private final MailSender mailSender;

    @Override
    public void send(String to, String subject, String body) {
        log.info("Sending {} e-mail to {}", subject, to);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sesProperties.getFrom());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
