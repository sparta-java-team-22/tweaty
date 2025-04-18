package com.tweaty.auth.infrastructure.client;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Uploader {

	private final S3Client s3Client;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucketName;

	public String upload(MultipartFile file, String dirName) {

		String originalFilename = file.getOriginalFilename();
		String fileName = createFileName(dirName, originalFilename);

		try {
			s3Client.putObject(
				PutObjectRequest.builder().bucket(bucketName).key(fileName).contentType(file.getContentType()).build(),
				RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

			return getFileUrl(fileName);

		} catch (IOException e) {

			throw new RuntimeException("파일 업로드 실패", e);

		}
	}

	private String createFileName(String dirName, String originalFilename) {

		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		String encodedName = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);

		return dirName + "/" + timestamp + "_" + encodedName;

	}

	private String getFileUrl(String fileName) {
		return "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
	}
}
