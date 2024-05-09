package fit.api.social_network.service.impl;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationCode(String email, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setFrom("noreply@example.com");
            helper.setTo(email);
            helper.setSubject("Mã số xác thực");
            helper.setText("Mã số xác thực của bạn là: " + code);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
