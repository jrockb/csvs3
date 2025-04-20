package co.com.jcd.csvs3.app.service;

import java.io.IOException;

public interface ICsvS3Service {

    void downloadCsv(String bucket, String key) throws IOException;
}
