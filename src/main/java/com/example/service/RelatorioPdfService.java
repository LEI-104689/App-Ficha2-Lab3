package com.example.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class RelatorioPdfService {
    public byte[] gerarRelatorio(String conteudo) throws IOException {
        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 14);
                content.newLineAtOffset(50, 700);
                for (String line : conteudo.split("\n")) {
                    content.showText(line);
                    content.newLineAtOffset(0, -20); // move para a linha seguinte
                }
                content.endText();
            }

            doc.save(out);
            return out.toByteArray();
        }
    }
}