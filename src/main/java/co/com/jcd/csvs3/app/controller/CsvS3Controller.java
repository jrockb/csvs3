package co.com.jcd.csvs3.app.controller;

import co.com.jcd.csvs3.app.service.ICsvS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/csvs3")
public class CsvS3Controller {

    @Autowired
    private ICsvS3Service service;

    @PostMapping("/download")
    public ResponseEntity<String> downloadFile(@RequestParam(name = "bucketName") String bucketName,
                                               @RequestParam(name = "key") String key) throws IOException {
        this.service.downloadCsv(bucketName, key);
        return ResponseEntity.ok("Archivo descargado correctamente");
    }

}
