package com.example.cwe.path_traversal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;


public class PathTraversalControllerTest {


    @Test
    public void downloadResourceTest() throws IOException {
        String file = new String(getClass().getClassLoader()
                .getResourceAsStream("xml/lol.xml")
//                .getResourceAsStream("c:/temp/jpeg.jpeg")
                .readAllBytes(), StandardCharsets.UTF_8);

        System.out.println(file);

    }

    @Test
    void resolveTest() {
        String basedir = "C:\\temp\\1";
        String inputDir = "C:\\temp\\1\\..\\2";

        Path exportFile = Path.of(basedir).resolve(Path.of(inputDir));
        System.out.println(exportFile);

        String META_FILE_NAME = "y.meta.json";

/*        try (FileSystem zip = FileSystems.newFileSystem(exportFile, (ClassLoader) null)) {
            Path metaFile = zip.getPath(META_FILE_NAME);

            System.out.println(metaFile);

//            byte[] metaContent = Files.readAllBytes(metaFile);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            Pis1MetaFile meta = objectMapper.readValue(metaContent, Pis1MetaFile.class);
//            Pis1MetaFile.Attachment attachment = meta.getContent().get(0);
//            String fileName = attachment.getFileName();
//            UUID fileIdentity = attachment.getFileIdentity();
//            Path nestedExportFile = zip.getPath(fileIdentity.toString());
//
//            checkPath(nestedExportFile);
//
//            byte[] nestedExportFileContent = Files.readAllBytes(nestedExportFile); //returns

        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    @Test
    void resolveTest2() {
        Path file = Paths.get("C:\\temp\\1\\11\\11.txt\\..\\..\\..\\2\\22.txt");

        System.out.println(file.getFileName());
        System.out.println(file.toAbsolutePath() + "\n");
        Path fileOld = file.resolveSibling(file.getFileName() + ".old");
        System.out.println(fileOld);
        System.out.println(fileOld.getFileName() + "\n");

        Path inj = Paths.get("C:\\temp\\1\\11\\11.txt");

        System.out.println(inj.resolve(file));
        System.out.println(file.resolve(inj));

    }

    @Test
    void testTempDir() {
        String BASE_DIRECTORY = System.getProperty("java.io.tmpdir");
        System.out.println(BASE_DIRECTORY);

        String requestFileName = "temp.txt";
        File resultFile = new File(BASE_DIRECTORY + File.separator + requestFileName);
        System.out.println(resultFile);


    }

}