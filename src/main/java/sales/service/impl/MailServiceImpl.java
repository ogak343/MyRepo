package sales.service.impl;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sales.service.MailService;


@Component
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public MailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendCode(int code, String name, String email) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("code", code);
        String message = templateEngine.process("confirmationMail", context);

        sendMessage(message, email);
    }

    @SneakyThrows
    private void sendMessage(String message, String email) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setText(message, true);
        helper.setTo(new InternetAddress(email));
        mailSender.send(mimeMessage);
    }
}
