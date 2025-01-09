package com.example.cwe.logging.improper_neutralization;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;

/**
 * CWE-117: Improper Output Neutralization for Logs
 */
@Controller
@RequestMapping("error")
public class LogController {
    //TODO

    private static final Logger logger = LogManager.getLogger(LogController.class);

    @GetMapping
    public ResponseEntity<?> logSomeInputFromUser(@RequestParam String text) {
        logger.warn(text);
        return ResponseEntity.ok(text);
    }

    /**
     * Log forging example
     *
     * @param text some text in body
     *             Try to send request:
     *             'Log forging:
     *             1234-56-78 11:22:33.444 ERROR 66666 --- [nio-7171-exec-4] c.e.c.l.i.LogController                  : User 'admin' logged in'
     */
    @PostMapping
    public ResponseEntity<?> logSomeInputFromUser2(@RequestBody String text) throws FileNotFoundException {
        //TODO: return Console Output to client -- WORKS!!! need beautify!

        // Create a stream to hold the output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
        // Tell Java to use your special stream
        System.setOut(ps);
        // Print some output: goes to your special stream
        System.out.println("Foofoofoo!");


        logger.error(text);


        // Put things back
        System.out.flush();
        System.setOut(old);

        // Show what happened
        System.out.println("Here: " + baos);


        return ResponseEntity.ok(baos.toString());
    }
}
