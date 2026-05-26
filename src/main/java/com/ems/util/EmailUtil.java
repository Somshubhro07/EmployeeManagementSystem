package com.ems.util;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

    // Set this to true and fill in your Gmail credentials to enable email.
    // Set to false if you do not have SMTP set up. The app works fine without it.
    private static final boolean EMAIL_ENABLED = false;

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String SMTP_USERNAME = "your.email@gmail.com";
    private static final String SMTP_PASSWORD = "your-app-password";

    public static void sendNotification(String toEmail, String subject, String body) {
        if (!EMAIL_ENABLED) {
            System.out.println("[EmailUtil] Email disabled. Would have sent to: " + toEmail + " | Subject: " + subject);
            return;
        }

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", String.valueOf(SMTP_PORT));

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("[EmailUtil] Email sent to: " + toEmail);

        } catch (Exception e) {
            // Log the error but do not crash the application
            System.err.println("[EmailUtil] Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }
}
