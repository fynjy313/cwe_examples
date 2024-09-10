package com.example.cwe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Scanner;

public class Temp {


    public static void injection() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Username");
        String user = sc.nextLine();

        String user_path = ".\\Data\\" + user;

        File file = new File(user_path);

        try {
            System.out.println("\n****** Output from library ******\n");

            Files.list(new File(user_path).toPath())
                    .limit(10)
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error executing command");

        }
        try {
            String comm = "cmd.exe /c dir " + user_path;
            System.out.println("\n****** Output from command line ******\n");

            Process process = Runtime.getRuntime().exec(comm);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException e) {
            System.out.println("Error executing command");

        }
    }

}
