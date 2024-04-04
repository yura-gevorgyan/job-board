package am.itspace.jobboard.util;

import am.itspace.jobboard.entity.Resume;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class PictureUtil {
    public static void processImageUploadResume(Resume resume, MultipartFile multipartFile, String uploadDirectory) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            resume.setPicName(picName);
        }
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
}
