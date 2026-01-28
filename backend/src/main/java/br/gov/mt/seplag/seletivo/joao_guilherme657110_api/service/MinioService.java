package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.SetBucketPolicyArgs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioService {
    @Autowired private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.public-url:#{null}}")
    private String minioPublicUrl;

    @PostConstruct
    public void initBucket() {
        try {
            boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!bucketExists) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
                );
            }

            // Define política pública para leitura
            String policy = "{\n" +
                    "    \"Version\": \"2012-10-17\",\n" +
                    "    \"Statement\": [\n" +
                    "        {\n" +
                    "            \"Effect\": \"Allow\",\n" +
                    "            \"Principal\": {\"AWS\": [\"*\"]},\n" +
                    "            \"Action\": [\"s3:GetBucketLocation\", \"s3:ListBucket\"],\n" +
                    "            \"Resource\": [\"arn:aws:s3:::" + bucketName + "\"]\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"Effect\": \"Allow\",\n" +
                    "            \"Principal\": {\"AWS\": [\"*\"]},\n" +
                    "            \"Action\": [\"s3:GetObject\"],\n" +
                    "            \"Resource\": [\"arn:aws:s3:::" + bucketName + "/*\"]\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            
            minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build()
            );
        } catch (Exception e) {
            // Logar o erro mas não impedir a inicialização da aplicação
            System.err.println("Erro ao inicializar bucket MinIO: " + e.getMessage());
        }
    }

    public String uploadFile(MultipartFile file, String bucketName, String objectName) throws Exception {
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build()
        );
        String baseUrl = (minioPublicUrl != null && !minioPublicUrl.isBlank()) ? minioPublicUrl : minioUrl;
        return String.format("%s/%s/%s", baseUrl, bucketName, objectName);
    }
}
