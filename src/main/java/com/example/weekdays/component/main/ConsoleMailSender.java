package com.example.weekdays.component.main;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;

@Profile("local")
@Component
@Slf4j
public class ConsoleMailSender implements JavaMailSender { //임시 메일전송 클래스
    //실제로 메일을 전송하지 않습니다. 내용을 콘솔에 출력하는 용도로 사용합니다.


    @Override
    public MimeMessage createMimeMessage() {
        return null;
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        return null;
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {

    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {

    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {

    }

    @Override
    public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
    log.info(simpleMessage.getText()); //가입이 되면 콘솔창에 로그를 남깁니다.

    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }
}
