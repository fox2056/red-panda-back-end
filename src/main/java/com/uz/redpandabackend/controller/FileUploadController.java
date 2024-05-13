package com.uz.redpandabackend.controller;

import com.uz.redpandabackend.service.PersonService;
import com.uz.redpandabackend.utils.IcsUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@Controller
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FileUploadController {


    private final Environment environment;
    private PersonService personService;

    @PostMapping("/")
    public ResponseEntity<String> handleFileUpload(@RequestPart("email") String email,
                                                   @RequestPart("imie") String imie,
                                                   @RequestPart("nazwisko") String nazwisko,
                                                   @RequestPart("secret") String secret,
                                    @RequestPart("file") MultipartFile file) {

        if (!Objects.equals(secret, environment.getProperty("secret"))) {
            return ResponseEntity.ok("Dobra próba kolega, próbuj dalej");
        }

        try {
            personService.updateOrInsertPerson(email, imie, nazwisko, IcsUtils.parseICSFromFile(file));
            return ResponseEntity.ok("Kalendarz wgrany");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }

}
