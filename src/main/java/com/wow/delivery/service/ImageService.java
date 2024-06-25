package com.wow.delivery.service;

import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.FileException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private static final String IMAGE_DIRECTORY = "C:\\Users\\josey\\Desktop\\mentoring\\wowbd\\images\\";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public String getImagePath(MultipartFile file) {
        return saveFile(file);
    }

    private String saveFile(MultipartFile file) {
        validateFile(file);

        try {
            String extension = getFileExtension(file);
            String uniqueFilename = generateUniqueFilename(extension);

            Path directoryPath = Paths.get(IMAGE_DIRECTORY);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(uniqueFilename);
            while (Files.exists(filePath)) {
                uniqueFilename = generateUniqueFilename(extension);
                filePath = directoryPath.resolve(uniqueFilename);
            }

            Files.write(filePath, file.getBytes());

            return uniqueFilename;
        } catch (IOException e) {
            throw new FileException(ErrorCode.FILE_ERROR, "올바르지 못한 파일입니다..");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileException(ErrorCode.FILE_ERROR, "파일이 비어있습니다.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileException(ErrorCode.FILE_ERROR, "파일 크기가 제한을 초과했습니다.");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new FileException(ErrorCode.FILE_ERROR, "이미지 파일만 업로드 가능합니다.");
        }
    }

    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return StringUtils.getFilenameExtension(originalFilename);
    }

    private String generateUniqueFilename(String extension) {
        return UUID.randomUUID().toString() + (extension != null ? "." + extension : "");
    }
}
