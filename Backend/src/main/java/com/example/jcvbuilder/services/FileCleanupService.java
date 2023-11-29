package com.example.jcvbuilder.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Service
public class FileCleanupService {

    @Scheduled(fixedRate = 10000) // 1 minute (adjust as needed)
    public void cleanupFiles() {
        String htmlFilePath = "src/main/java/com/example/jcvbuilder/resume_outputs/generated.html";
        String pdfFilePath = "src/main/java/com/example/jcvbuilder/resume_outputs/generated.pdf";

        deleteFile(htmlFilePath);
        deleteFile(pdfFilePath);
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("File deleted: " + filePath);
            } else {
                System.err.println("Failed to delete file: " + filePath);
            }
        }
    }
}
