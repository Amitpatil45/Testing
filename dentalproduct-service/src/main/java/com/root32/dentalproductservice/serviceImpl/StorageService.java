package com.root32.dentalproductservice.serviceImpl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class StorageService {

    @Value("${bucket-name}")
    private String bucketName;

    @Value("${endpoint-url}")
    private String baseUrl;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadFile(String base64, String fileName, String folderName) throws IOException {
        File fileObj = convertBase64ToFile(base64, fileName);

        // Check if the folder exists, and create it if not
        if (!s3Client.doesObjectExist(bucketName, folderName)) {
            s3Client.putObject(bucketName, folderName, "");
        }
        // Upload the file to the folder
        String fileNameWIthFolder = folderName + "/" + System.currentTimeMillis() + "_" + fileName;
        s3Client.putObject(new PutObjectRequest(bucketName, fileNameWIthFolder, fileObj));
        fileObj.delete();
        return baseUrl + fileNameWIthFolder;
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return "removed";
    }

    public static File convertBase64ToFile(String base64, String fileName) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);

        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedBytes);
        }

        return file;
    }
}