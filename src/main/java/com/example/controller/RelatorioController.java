package com.example.controller;

import com.example.examplefeature.Task;
import com.example.examplefeature.TaskService;
import com.example.service.RelatorioPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RelatorioController {

    private final RelatorioPdfService pdfService;
    private final TaskService taskService;

    public RelatorioController(RelatorioPdfService pdfService, TaskService taskService) {
        this.pdfService = pdfService;
        this.taskService = taskService;
    }

    @GetMapping("/api/relatorio/pdf")
    public ResponseEntity<byte[]> exportarPdf() throws Exception {
        List<Task> tasks = taskService.list(org.springframework.data.domain.PageRequest.of(0, 100));
        StringBuilder conteudo = new StringBuilder("Relatório de Tarefas\n\n");

        if (tasks.isEmpty()) {
            conteudo.append("Sem tarefas registadas.");
        } else {
            for (Task t : tasks) {
                conteudo.append("Descrição: ").append(t.getDescription()).append("\n")
                        .append("Data limite: ").append(t.getDueDate() != null ? t.getDueDate() : "N/A").append("\n")
                        .append("Criada em: ").append(t.getCreationDate()).append("\n\n");
            }
        }

        byte[] pdf = pdfService.gerarRelatorio(conteudo.toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
