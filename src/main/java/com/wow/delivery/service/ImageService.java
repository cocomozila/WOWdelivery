package com.wow.delivery.service;

import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.FileException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

@Service
public class ImageService {

    private static final String IMAGE_DIRECTORY = "C:\\Users\\josey\\Desktop\\mentoring\\wowbd\\images\\";
    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB
    private static final String DEFAULT_IMAGE = "default.jpg";

    /**
     * 이미지 파일을 지정된 좌표와 크기로 잘라 정방형으로 변환하여 저장하고, 파일명을 반환한다.
     *
     * @param file   업로드된 이미지 파일
     * @param x      자를 시작 x 좌표
     * @param y      자를 시작 y 좌표
     * @param length 자를 한 변의 길이(정사각형)
     * @return 저장된 파일의 고유 파일명
     */
    public String getImagePath(String className, Long id, MultipartFile file, int x, int y, int length) {
        if (file.isEmpty()) {
            return DEFAULT_IMAGE;
        }
        return saveFile(className, id, file, x, y, length);
    }

    private String saveFile(String className, Long id, MultipartFile file, int x, int y, int length) {
        validateFile(file);

        try {
            String extension = getFileExtension(file);
            String uniqueFilename = generateFilename(className, id, extension);

            Path directoryPath = Paths.get(IMAGE_DIRECTORY);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(uniqueFilename);
            while (Files.exists(filePath)) {
                uniqueFilename = generateFilename(className, id, extension);
                filePath = directoryPath.resolve(uniqueFilename);
            }

            cropAndSaveImage(file, filePath, x, y, length);

            return uniqueFilename;
        } catch (IOException e) {
            throw new FileException(ErrorCode.FILE_ERROR, "올바르지 못한 파일입니다.");
        }
    }

    private void validateFile(MultipartFile file) {
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

    private String generateFilename(String className, Long id, String extension) {
        return className + id + (extension != null ? "." + extension : "");
    }

    private void cropAndSaveImage(MultipartFile file, Path filePath, int x, int y, int length) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage croppedImage = originalImage.getSubimage(x, y, length, length);

        BufferedImage resizedImage = new BufferedImage(length, length, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // 고품질 렌더링 설정
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 알파 채널 처리
        g2d.setComposite(AlphaComposite.Src);

        g2d.drawImage(croppedImage, 0, 0, length, length, null);
        g2d.dispose();

        String extension = getFileExtension(file);
        if ("jpg".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension)) {
            // JPEG 압축 품질 설정
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(extension);
            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.95f); // 높은 품질 설정 (0.0 - 1.0)

            try (ImageOutputStream output = ImageIO.createImageOutputStream(new File(filePath.toString()))) {
                writer.setOutput(output);
                IIOImage iioImage = new IIOImage(resizedImage, null, null);
                writer.write(null, iioImage, param);
            } finally {
                writer.dispose();
            }
        } else {
            // PNG 등 다른 형식의 경우 기본 저장 방식 사용
            ImageIO.write(resizedImage, extension, new File(filePath.toString()));
        }
    }
}
