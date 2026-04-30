package com.example.cwe.path_traversal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@SuppressWarnings("unused")
public class PathTraversalController {
    static final String BASE_DIRECTORY = System.getProperty("java.io.tmpdir");
    private static final Logger logger = LogManager.getLogger(PathTraversalController.class);

    @GetMapping("download-file-unsafe")
    public void downloadFileUnsafe(@RequestParam("filename") String fileName, HttpServletResponse response) throws IOException {
        File f = new File(BASE_DIRECTORY + File.separator + fileName);

        if (f.exists() && !f.isDirectory()) {

            MediaType mediaType = MediaTypeFactory.getMediaType(fileName)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);

            /*ContentDisposition disposition = ContentDisposition
                    .inline() // or .attachment()
                    .filename(f.getName()).build();
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition.toString());*/

            response.setContentType(mediaType.toString());
            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());

            response.getOutputStream().write(("Resolved file name: " + f + "\n" + "Canonical pathname: "
                    + f.getCanonicalPath() + "\n\n").getBytes());
            response.getOutputStream().write(Files.readAllBytes(f.toPath()));
            logger.info("File " + f + " downloaded.");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Resolved file name: " + f + " | Canonical pathname: " + f.getCanonicalPath()
                    + " - file doesn't exist or is not a file.");
        }
    }


    @GetMapping("download-file-safe")
    public void downloadFileSafe(@RequestParam("filename") String fileName, HttpServletResponse response) throws IOException {

        File f = new File(BASE_DIRECTORY + File.separator + fileName);

        if (!f.getCanonicalPath().toLowerCase().startsWith(BASE_DIRECTORY.toLowerCase())
                || (!f.exists() && f.isDirectory())) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("File " + fileName + " doesn't exist or is not a file.");
        } else {
            MediaType mediaType = MediaTypeFactory.getMediaType(fileName)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);
            response.setContentType(mediaType.toString());
            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
            response.getOutputStream()
                    .write(("Resolved file name: " + f + "\n" + "Canonical pathname: "
                            + f.getCanonicalPath() + "\n\n").getBytes());
            response.getOutputStream().write(Files.readAllBytes(f.toPath()));
        }
    }

    //TODO: POC
    @PostMapping("upload-file-unsafe")
    public ResponseEntity<?> uploadFileUnsafe(@RequestParam("file") MultipartFile file) {
        if (file == null) return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("File missing");
        try {
            String requestFileName = file.getOriginalFilename();
            File resultFile = new File(BASE_DIRECTORY + File.separator + requestFileName);

            System.out.println(resultFile);

            file.transferTo(resultFile);

            String result = String.format("%s\tFile with requestFileName %s transferred to %s (resultPath: %s)"
                    , new Date(), requestFileName, resultFile, resultFile.getCanonicalPath());

            System.out.println(result);

            return ResponseEntity.ok().body(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("upload-file-safe")
    public ResponseEntity<?> uploadFileSafe(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null) return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("File missing");

        String requestFileName = file.getOriginalFilename();

        if (!checkFileName(requestFileName)) {
            return ResponseEntity.internalServerError().body("Не валидное имя файла: " + requestFileName);
        }
        requestFileName = requestFileName.replaceAll("\\.\\.", "");

        File resultFile = new File(BASE_DIRECTORY + File.separator + requestFileName);
        file.transferTo(resultFile);

        String result = String.format("%s\tFile with requestFileName %s transferred to %s (resultPath: %s)"
                , new Date(), requestFileName, resultFile, resultFile.getCanonicalPath());

        return ResponseEntity.ok().body(result);
    }

    static boolean checkFileName(final String fileName) {
        final String pattern = "^[A-Za-z0-9.\\-\\_]{1,255}$";
        return fileName.matches(pattern);
    }

}
