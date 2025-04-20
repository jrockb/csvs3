package co.com.jcd.csvs3.app.service;

import co.com.jcd.csvs3.app.dao.ITabHRepository;
import co.com.jcd.csvs3.app.entity.TabHEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@Slf4j
public class CsvS3ServiceImpl implements ICsvS3Service {

    @Autowired
    private ITabHRepository repository;

    @Autowired
    private S3Client s3Client;

    @Value("${spring.destination.folder}")
    private String destinationFolder;

    @Override
    public void downloadCsv(String bucket, String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        try(InputStream inputStream = this.s3Client.getObject(getObjectRequest,
                ResponseTransformer.toInputStream());
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
            for (CSVRecord recordCsv : parser) {
                try {
                    TabHEntity tabH = new TabHEntity();
                    tabH.setOrdId(recordCsv.get(0));
                    tabH.setIdTabG(Long.valueOf(recordCsv.get(1)));
                    tabH.setCodF(Long.valueOf(recordCsv.get(2)));
                    tabH.setTipoIdP(recordCsv.get(3));
                    tabH.setNIdP(Long.valueOf(recordCsv.get(4)));
                    tabH.setNP(recordCsv.get(5));
                    tabH.setTDS(recordCsv.get(6));
                    tabH.setNDS(recordCsv.get(7));
                    tabH.setFSol(parseDateString(recordCsv.get(8)));
                    tabH.setFPr(parseDateString(recordCsv.get(9)));
                    tabH.setEstP(recordCsv.get(10));
                    tabH.setVB(new BigDecimal(recordCsv.get(11)));
                    repository.save(tabH);
                } catch(DateTimeParseException | NumberFormatException e) {
                    log.error("Error {} : {}", recordCsv.get(0), e.getMessage());
                }

            }

        } catch(Exception ex) {
            log.error(ex.getMessage());
        }

    }

    private LocalDateTime parseDateString(String date) {
        if(date.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}$")) {
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        } else if(date.matches("^\\d{4}-\\d{2}-\\d{2}$")){
            LocalDate dateFormated = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return dateFormated.atStartOfDay();
        } else {
            throw new DateTimeParseException("Invalid date string", date, 0);
        }
    }


}
