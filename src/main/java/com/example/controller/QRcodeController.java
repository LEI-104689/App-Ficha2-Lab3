package com.example.controller;

import com.example.examplefeature.TaskService;
import com.example.service.QRcodeService;
import com.example.examplefeature.Task;
import com.example.service.RelatorioPdfService;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/api/qrcode")
public class QRcodeController {

    private final QRcodeService qrService;
    private final TaskService taskService;

    public QRcodeController(QRcodeService qrService, TaskService taskService) {
        this.qrService = qrService;
        this.taskService = taskService;
    }


    @PostMapping("/read")
    public ResponseEntity<String> readQRCodeAndCreateTask(@RequestParam("file") MultipartFile file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);

            String qrData = result.getText();
            Task newTask = new Task();
            newTask.setName(qrData);
//            taskService.createTask(newTask);

            return ResponseEntity.ok("Task created: " + qrData);
        } catch (IOException | NotFoundException e) {
            return ResponseEntity.badRequest().body("Could not read QR code");
        }
    }
}
