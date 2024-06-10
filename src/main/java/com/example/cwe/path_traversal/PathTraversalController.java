package com.example.cwe.path_traversal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

@RestController
@RequestMapping("path-traversal")
public class PathTraversalController {
    static final String BASE_DIRECTORY = "c:\\temp\\";


    @GetMapping("download-image-unsafe")
    public void downloadImageUnsafe(@RequestParam("filename") String fileName, HttpServletResponse response) throws IOException {
        //TODO: getClass().getResourceAsStream(filename)

        File f = new File(BASE_DIRECTORY + fileName);

        if (f.exists() && !f.isDirectory()) {
            MediaType mediaType = MediaTypeFactory.getMediaType(fileName)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);

//            ContentDisposition disposition = ContentDisposition
//                    .inline() // or .attachment()
//                    .filename(f.getName())
//                    .build();
//            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition.toString());

            response.setContentType(mediaType.toString());
            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());

            response.getOutputStream()
                    .write(("Resolved file name: " + f + "\n" + "Canonical pathname: " + f.getCanonicalPath() + "\n\n")
                            .getBytes());
            response.getOutputStream().write(Files.readAllBytes(f.toPath()));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("File doesn't exist or is not a file.");
        }
    }


    @GetMapping("download-image-safe")
    public void downloadImageSafe(@RequestParam("filename") String fileName, HttpServletResponse response) throws IOException {

        File f = new File(BASE_DIRECTORY + fileName);

        if (!f.getCanonicalPath().toLowerCase().startsWith(BASE_DIRECTORY)
                || (!f.exists() && f.isDirectory())) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("File doesn't exist or is not a file.");
        } else {
            MediaType mediaType = MediaTypeFactory.getMediaType(fileName)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);
            response.setContentType(mediaType.toString());
            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
            response.getOutputStream()
                    .write(("Resolved file name: " + f + "\n" + "Canonical pathname: " + f.getCanonicalPath() + "\n\n")
                            .getBytes());
            response.getOutputStream().write(Files.readAllBytes(f.toPath()));
        }
    }


    @PostMapping("upload-image-unsafe")
    public ResponseEntity<?> uploadImageUnsafe(@RequestParam("file") MultipartFile file) {
        if (file == null) return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("File missing");
        try {
            String requestFileName = file.getOriginalFilename();
            File resultFile = new File(System.getProperty("java.io.tmpdir") + requestFileName);

            file.transferTo(resultFile);

            String result = String.format("%s\tFile with requestFileName %s transferred to %s (resultPath: %s)"
                    , new Date(), requestFileName, resultFile, resultFile.getCanonicalPath());

            return ResponseEntity.ok().body(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
