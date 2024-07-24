package de.cimt.service;


import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class InformEmployee implements JavaDelegate {

    @Value("$(spring.mail.username)")
    private String senderEmail;

    private final JavaMailSender javaMailSender;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Boolean acceptLeave = (Boolean) execution.getVariable("accept_leave");
        String recipientEmail = (String) execution.getVariable("email");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(senderEmail);
        simpleMailMessage.setTo(recipientEmail);
        if (acceptLeave) {
            simpleMailMessage.setText("your leave is accepted");
            simpleMailMessage.setSubject("accepted leave");
            System.out.println("Accepted leave sent successfully");

        } else {
            simpleMailMessage.setText("your leave is rejected");
            simpleMailMessage.setSubject("rejected leave");
            System.out.println("Rejected leave sent successfully");

        }
        javaMailSender.send(simpleMailMessage);
    }
}
