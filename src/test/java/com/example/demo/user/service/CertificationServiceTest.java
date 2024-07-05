package com.example.demo.user.service;


import com.example.demo.mock.FakeMailSender;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CertificationServiceTest {


    @Test
    public void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다()  {


        // given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);

        // when
        certificationService.send("khmgobe@gmail.com", 1, "aaaaaaaa-aaaa-aaaa-aaaaaaaaaa");

        // then
        assertThat(fakeMailSender.getEmail()).isEqualTo("khmgobe@gmail.com");
        assertThat(fakeMailSender.getTitle()).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.getContent()).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaaaaaa-aaaa-aaaa-aaaaaaaaaa");
    }
}
