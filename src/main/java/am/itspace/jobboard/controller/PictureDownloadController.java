package am.itspace.jobboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;

@Controller
@RequiredArgsConstructor
@RequestMapping("/download/picture")
public class PictureDownloadController {

    @Value("${program.pictures.file.path}")
    private String FILE_PATH;

    @GetMapping(value = "/{picName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> downloadPicture(@PathVariable("picName") String picName) {
        Resource resource = new FileSystemResource(FILE_PATH + File.separator + picName);
        if (resource.exists()) {
            return ResponseEntity.ok().body(resource);
        }
        return ResponseEntity.ok().body(null);
    }
}
