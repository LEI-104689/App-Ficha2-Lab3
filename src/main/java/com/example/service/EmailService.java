package com.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final RelatorioPdfService pdfService;

    public EmailService(JavaMailSender mailSender, RelatorioPdfService pdfService) {
        this.mailSender = mailSender;
        this.pdfService = pdfService;
    }

    public void enviarEmail(String destinatario, boolean incluirAnexo) throws MessagingException {
        MimeMessage mensagem = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensagem, incluirAnexo);

        helper.setTo(destinatario);
        helper.setSubject("Relatório PDF - TaskList");
        helper.setText("Segue o relatório solicitado da TaskList.", false);

        try {
            byte[] pdf = pdfService.gerarRelatorio("Relatório gerado e enviado por email.");
            helper.addAttachment("relatorio.pdf", new ByteArrayResource(pdf));
        } catch (IOException e) {
            throw new MessagingException("Erro ao gerar o PDF para anexo", e);
        }

        mailSender.send(mensagem);
    }
}