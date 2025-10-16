package com.example.controller;

import com.example.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/enviar")
    public String enviar(@RequestParam String destinatario, @RequestParam boolean incluirAnexo) throws MessagingException {
        emailService.enviarEmail(destinatario, incluirAnexo);
        return "Email enviado para " + destinatario;
    }
}