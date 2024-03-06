package am.itspace.jobboard.util;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class PictureUtil {
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
