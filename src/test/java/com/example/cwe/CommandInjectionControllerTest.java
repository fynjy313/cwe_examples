package com.example.cwe;

import org.testng.annotations.Test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class CommandInjectionControllerTest {
    @Test
    public void executePingCommandTest() throws IOException {
        System.out.print("Input IP: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String ip = reader.readLine();
        System.out.println(ip);

//        String command = "cmd -c ping " + ip;
//        Process p = Runtime.getRuntime().exec(command);
//
//        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
//
//        String line;
//        StringBuilder sb = new StringBuilder();
//
//        while ((line = input.readLine()) != null) {
//            System.out.println(line);
//            sb.append(line);
//        }
//        input.close();
//        return sb.toString();
    }


    @Test
    public void pingProcess1() throws IOException {
//        String ip = "127.0.0.1";
        String ip = "127.0.0.1 & whoami";

        //String command = "cmd /c ping -n 1 " + ip;

        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "ping -n 1 " + ip);
        Process p = pb.start();

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = input.readLine()) != null) {
            sb.append(line).append("\n");
        }
        input.close();
        System.out.println(sb);
    }

    static Set<String> allowedCommands = new HashSet<>();
    static Set<String> denyArguments = new HashSet<>();

    static {
        allowedCommands.add("pwd");
        allowedCommands.add("ls");
        allowedCommands.add("ps");
        allowedCommands.add("find");
        allowedCommands.add("uname");
        allowedCommands.add("free");
        allowedCommands.add("df");
        allowedCommands.add("locate");
        allowedCommands.add("hostname");

        denyArguments.add("-exec");
    }

    @Test
    public String pingProcessSafety(String command, List<String> args) throws IOException {
        final List<String> cmd = new ArrayList<>();

        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("Укажите команду");
        }
        command = command.trim().toLowerCase();

        if (!allowedCommands.contains(command)) {
            throw new IllegalArgumentException("Недопустимая команда");
        }

        cmd.add(command);

        if (args != null) {
            for (String arg : args) {
                if (denyArguments.contains(arg.trim().toLowerCase())) {
                    throw new IllegalArgumentException("Недопустимый аргумент");
                }
            }
            cmd.addAll(args);
        }

        ProcessBuilder pb = new ProcessBuilder(cmd);

        System.out.println(pb.command());

        Process p = pb.start();

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = input.readLine()) != null) {
            sb.append(line).append("\n");
        }
        input.close();
        return sb.toString();
    }

    //    @Test
//    public void qwe() throws IOException {
    public static void main(String[] args) throws IOException {
        System.out.println(" FinD ".trim().toLowerCase());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Input command: ");
            String inputCmd = reader.readLine();

//            String strip = inputCmd.replaceAll("[&|;$><`!'\"\\(\\)]+","");
//            String strip2 = inputCmd.replaceAll("[^a-zA-Z 0-9]","");
//            String escape = inputCmd.replaceAll("[^a-zA-Z 0-9]","_");

//            if (Pattern.matches(".*[&|;$><`\\\\!'\"\\(\\)]+.*", inputCmd)) {
            if (Pattern.matches("^.*(([&|;$><`\\\\!'\"()])|(0x0[Aa])).*$", inputCmd)) {
                System.out.println("Недопустимая команда");
                continue;
            }
            final String[] command = inputCmd.split(" ");

            if (!allowedCommands.contains(command[0].toLowerCase())) {
                System.out.println("Недопустимая первая команда");
            }
            for (String arg : command) {
                if (denyArguments.contains(arg.toLowerCase())) {
                    System.out.println("Недопустимый аргумент");
                }
            }

        }
    }


}
