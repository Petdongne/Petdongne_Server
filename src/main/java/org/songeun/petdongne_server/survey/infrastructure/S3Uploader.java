package org.songeun.petdongne_server.survey.infrastructure;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.util.Delimiters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final S3AsyncClient s3AsyncClient;

    public CompletableFuture<String> uploadAsync(MultipartFile file, String folder) throws IOException {
        byte[] fileBytes = file.getBytes();
        String key = folder + Delimiters.SLASH +
                UUID.randomUUID() + Delimiters.UNDER_BAR +
                file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .contentLength((long) fileBytes.length)
                .build();

        return s3AsyncClient
                .putObject(request, AsyncRequestBody.fromBytes(fileBytes))
                .thenApply(response ->
                        {
                            String uploadedUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + key;
                            return uploadedUrl;
                        }
                );
    }
}
