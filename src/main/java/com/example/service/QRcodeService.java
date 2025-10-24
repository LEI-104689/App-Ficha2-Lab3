package com.example.service;

import com.example.examplefeature.Task;
import com.example.examplefeature.TaskRepository;
import com.example.util.QRCodeGenerator;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Service
public class QRcodeService {

    private final TaskRepository taskRepository;
    private final QRCodeGenerator qrCodeGenerator;

    public QRcodeService(TaskRepository taskRepository, QRCodeGenerator qrCodeGenerator) {
        this.taskRepository = taskRepository;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    // Get all tasks for a specific date
    public List<Task> getTasksByDate(LocalDate date) {
        return taskRepository.findByDueDate(date);
    }

    // Generate QR code bytes for a given task ID
    public byte[] generateQRCodeForTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        return generateQRCodeForTask(task);
    }

    // Generate QR code bytes for a task object
    public byte[] generateQRCodeForTask(Task task) {
        String payload = serializeTask(task);
        return qrCodeGenerator.generateQRCodeImage(payload, 300, 300);
    }

    // Import task from QR payload
    public Task importTaskFromPayload(String qrPayload) {
        Task imported = deserializeTask(qrPayload);
        return taskRepository.save(imported);
    }

    // Serialize minimal task data to a string (JSON-like)
    private String serializeTask(Task task) {
        return String.format("{\"title\":\"%s\",\"description\":\"%s\",\"dueDate\":\"%s\"}",
                escape(task.getTitle()),
                escape(task.getDescription()),
                task.getDueDate());
    }

    // Deserialize payload string back into a Task
    private Task deserializeTask(String payload) {
        // Simplified parsing; replace with JSON library (e.g. Jackson)
        String clean = new String(payload.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        Task task = new Task();
        if (clean.contains("\"title\"")) {
            task.setTitle(extractValue(clean, "title"));
        }
        if (clean.contains("\"description\"")) {
            task.setDescription(extractValue(clean, "description"));
        }
        if (clean.contains("\"dueDate\"")) {
            task.setDueDate(LocalDate.parse(extractValue(clean, "dueDate")));
        }
        return task;
    }

    // Helper methods
    private String escape(String text) {
        return text == null ? "" : text.replace("\"", "\\\"");
    }

    private String extractValue(String json, String key) {
        String pattern = String.format("\"%s\":\"([^\"]*)\"", key);
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(json);
        return matcher.find() ? matcher.group(1) : "";
    }
}
