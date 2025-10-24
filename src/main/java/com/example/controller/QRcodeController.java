package com.example.controller;

import com.example.examplefeature.Task;
import com.example.service.QRcodeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/qrcode")
public class QRcodeController {

    private final QRcodeService qrCodeService;

    public QRcodeController(QRcodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    // Generate QR code for a given task ID
    @GetMapping("/generate/{taskId}")
    public ResponseEntity<byte[]> generateQRCode(@PathVariable Long taskId) {
        byte[] qrBytes = qrCodeService.generateQRCodeForTaskId(taskId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/png")
                .body(qrBytes);
    }

    // Get tasks for a given date
    @GetMapping("/tasks")
    public List<Task> getTasksByDate(@RequestParam LocalDate date) {
        return qrCodeService.getTasksByDate(date);
    }

    // Import task from a scanned QR payload
    @PostMapping("/import")
    public ResponseEntity<Task> importTask(@RequestBody String qrPayload) {
        Task task = qrCodeService.importTaskFromPayload(qrPayload);
        return ResponseEntity.ok(task);
    }

    // Optional: handle invalid requests
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
