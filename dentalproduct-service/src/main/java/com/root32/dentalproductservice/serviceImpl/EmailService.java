package com.root32.dentalproductservice.serviceImpl;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.root32.dto.PdfAttachment;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailWithAttachment(String to, String subject, String text, File attachment) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addAttachment(attachment.getName(), attachment);

        javaMailSender.send(message);
    }


 public void sendEmailWithAttachments(String to, String subject, String text, List<PdfAttachment> pdfAttachments) throws MessagingException{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        for (PdfAttachment pdfAttachment : pdfAttachments) {
            System.out.println(pdfAttachment.getName());
            InputStreamSource attachmentSource = new ByteArrayResource(pdfAttachment.getData());
            helper.addAttachment(pdfAttachment.getName(), attachmentSource, "application/pdf");
        }

        javaMailSender.send(message);
    }
}