package am.itspace.jobboard.util;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class PictureUtil {

    private final TaskExecutor taskExecutor;

    public PictureUtil(@Qualifier("myTaskExecutorBean") TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void processImageUpload(UploadAble uploadAble, MultipartFile multipartFile, String uploadDirectory) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {

            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);

            if (multipartFile.getSize() > 2097152) {
                taskExecutor.execute(() -> {
                    reduceImageQualityAsync(file);
                });
            }
            uploadAble.setPicName(picName);
        }
    }

    @Async
    @SneakyThrows
    public void reduceImageQualityAsync(File file) {
        BufferedImage img = ImageIO.read(file);

        int newWidth = (int) (img.getWidth() * 0.5);
        int newHeight = (int) (img.getHeight() * 0.5);

        BufferedImage scaledImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledImg.createGraphics();
        g2d.drawImage(img, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        ImageIO.write(scaledImg, "jpg", file);
    }

    public static byte[] getImage(String picName, String uploadDirectory) throws IOException {
        File file = new File(uploadDirectory, picName);
        if (file.exists()) {
            return IOUtils.toByteArray(new FileInputStream(file));
        }
        return null;
    }

    public static void deletePicture(String uploadDirectory, String picName) {
        File file = new File(uploadDirectory, picName);
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean isFileSizeValid(MultipartFile file) {
        long maxSizeInBytes = 10 * 1024 * 1024; //10MB
        return file != null && file.getSize() <= maxSizeInBytes;
    }
}
