package com.example.jcvbuilder.controllers;
import com.example.jcvbuilder.models.DTO.CVPayload;
import com.example.jcvbuilder.models.DTO.SkillCategory;
import com.example.jcvbuilder.models.DTO.UserDTO;
import com.example.jcvbuilder.models.User;
import com.example.jcvbuilder.services.CVBuilder;
import com.example.jcvbuilder.services.JDBCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.example.jcvbuilder.services.PDFReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
//@RequestMapping("/api/pdf")
@CrossOrigin(origins = "http://localhost:5173")
public class CVController {
    JDBCService dbService;
    CVBuilder cvBuilder;
    PDFReader myReader;

    @Autowired
    public CVController(JDBCService dbService) {
        this.dbService = dbService;
        this.cvBuilder = new CVBuilder();
        this.myReader =  new PDFReader();
    }

   /* @Async
    @GetMapping("/analyze")
    public CompletableFuture<String> analyzeCV() {
        String path = "src/main/resources/static/Cv_Jean_NL.pdf";
        return CompletableFuture.completedFuture(this.myReader.extractSkills(path));
    }*/

    @PostMapping("/analyze")
    public CompletableFuture<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (!file.getContentType().equals("application/pdf")) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Only PDF files are accepted"));
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get("uploads/" + fileName);
        try {
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            List<SkillCategory> skillCategories = this.myReader.extractSkills(path.toString());

            //now let's analyze the cv
            return CompletableFuture.completedFuture(ResponseEntity.ok(skillCategories));
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR));

        }
        //return ResponseEntity.ok("File uploaded successfully");
    }



    @GetMapping("/login")
    public ResponseEntity<String> getLoginPage() {
        try {
            Resource resource = new ClassPathResource("templates/login.html");

            if (resource.exists()) {
                try (Reader reader = new InputStreamReader(resource.getInputStream())) {
                    String content = FileCopyUtils.copyToString(reader);
                    return ResponseEntity.ok(content);
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login page not found");
            }
        } catch (IOException e) {
            // Handle other exceptions if needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> signin(@RequestBody UserDTO credentials) {
        String name = credentials.getUsername();
        String psw = credentials.getPassword();

        //System.out.println(String.format("Got %s", name));
        UserDTO aUser = dbService.findUserByUsername(name, psw);
        // Check credentials and return 401 if unauthorized
        if (aUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong credentials");
        }
        return ResponseEntity.ok(String.format("Authentication successful, welcome %s!", name));
    }

    @PostMapping("/register")
    public ResponseEntity<String> signup(@RequestBody UserDTO credentials) {
        String name = credentials.getUsername();
        String psw = credentials.getPassword();
        Boolean insertResult = dbService.registerUser(name, psw);
        if (insertResult) {
            return ResponseEntity.ok(String.format("Registration successful, welcome onboard %s!", name));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @Async
    @PostMapping("/generate")
    public CompletableFuture<ResponseEntity<Resource>> generatePdf(@RequestBody CVPayload request) throws IOException {
        this.cvBuilder.buildCV(request);
        String pdfFilePath = "src/main/java/com/example/jcvbuilder/resume_outputs/generated.pdf";

        File pdfFile = new File(pdfFilePath);
        if (pdfFile.exists()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());
            headers.setContentType(MediaType.APPLICATION_PDF);

            Resource resource = new FileSystemResource(pdfFile);

            return CompletableFuture.completedFuture(
                    ResponseEntity
                            .ok()
                            .headers(headers)
                            .contentLength(pdfFile.length())
                            .contentType(MediaType.APPLICATION_PDF)
                            .body(resource)
            );
        } else {
            return CompletableFuture.completedFuture(
                    ResponseEntity
                            .notFound()
                            .build()
            );
        }
    }
}
