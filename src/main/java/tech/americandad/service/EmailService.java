package tech.americandad.service;

import java.util.Date;
import com.sun.mail.smtp.SMTPTransport;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import tech.americandad.constants.EmailConstant;

@Service
public class EmailService {

    public void envioPasswordEmail(String nome, String password, String email) {
        try {
            Message message =  criarEmail(nome, password, email);
            SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(EmailConstant.SIMPLE_MAIL_TRANSFER_PROTOCOL);
            smtpTransport.connect(EmailConstant.GMAIL_SMTP_SERVER, EmailConstant.USUARIO, EmailConstant.PASSWORD);
            smtpTransport.sendMessage(message, message.getAllRecipients());
            smtpTransport.close();
            
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private Message criarEmail(String nome, String password, String email) throws AddressException, MessagingException{

        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(EmailConstant.FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(EmailConstant.CC_EMAIL, false));
        message.setSubject(EmailConstant.EMAIL_ASSUNTO);
        message.setText("Olá " + nome + ", \n \n" + 
        " uma nova senha foi gerada para sua conta no AmericanDad: " + password +
        "\n \n Time de Suporte AmericanDad.");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private Session getEmailSession(){

        Properties properties = System.getProperties();
        properties.put(EmailConstant.SMTP_HOST, EmailConstant.GMAIL_SMTP_SERVER);
        properties.put(EmailConstant.SMTP_AUTH, true);
        properties.put(EmailConstant.SMTP_PORT, EmailConstant.DEFAULT_PORT);
        properties.put(EmailConstant.SMTP_STARTTLS_ENABLE, true);
        properties.put(EmailConstant.SMTP_STARTTLS_REQUIRED, true);

        return Session.getInstance(properties);

    }
    
}
