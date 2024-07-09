package com.example.demo.mock;

import com.example.demo.user.service.port.MailSender;
import lombok.Getter;

@Getter
public class FakeMailSender implements MailSender {


    private String email;
    private String title;
    private String content;

    @Override
    public void send(String email, String title, String content) {
        this.email = email;
        this.title = title;
        this.content = content;
    }
}
