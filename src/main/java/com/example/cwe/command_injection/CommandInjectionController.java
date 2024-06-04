package com.example.cwe.command_injection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@RestController
@RequestMapping("cmd")
public class CommandInjectionController {

    @GetMapping("ping")
    public String executePingCommand(@RequestParam String ip) throws IOException {
        // http://localhost:7171/ping?ip=127.0.0.2 %26 dir %26 whoami /groups
        String command = "cmd /c ping -n 1 " + ip;
        Process p = Runtime.getRuntime().exec(command);

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = input.readLine()) != null) {
            sb.append(line).append("\n");
        }
        input.close();
        return sb.toString();
    }

    @GetMapping("pingPb1")
    public String pingProcessUnsafe(@RequestParam String ip) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "ping -n 1 " + ip);
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

    @GetMapping("pingPb2")
    public String pingProcessSafe(@RequestParam String ip) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "ping -n 1 ", ip);
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

    @GetMapping("cmdSafety")
    public String execCommandSafety(
            @RequestParam(value = "command") String command,
            @RequestParam(value = "args[]", required = false) List<String> args) throws IOException {
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

        // ... start process, handle exit value, input and error streams, return result

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

    @GetMapping("cmdRuntimeSafe")
    public String execRuntimeCommandSafety(@RequestParam String inputCmd) throws IOException {
        if (inputCmd == null || inputCmd.isEmpty()) {
            throw new IllegalArgumentException("Укажите команду");
        }

        if (Pattern.matches("^.*(([&|;$><`\\\\!'\"()])|(0x0[Aa])).*$", inputCmd)) {
            System.out.println("Недопустимая команда");
        }
        String strip = inputCmd.replaceAll("[&|;$><`\\\\!'\"()]+","");
        String strip2 = inputCmd.replaceAll("[^a-zA-Z 0-9]","");
        String escape = inputCmd.replaceAll("[^a-zA-Z 0-9]","_");

        final String[] command = inputCmd.split(" ");

        if (!allowedCommands.contains(command[0].toLowerCase())) {
            throw new IllegalArgumentException("Недопустимая команда");
        }
        for (String arg : command) {
            if (denyArguments.contains(arg.toLowerCase())) {
                throw new IllegalArgumentException("Недопустимый аргумент");
            }
        }
        // ... start process, handle exit value, input and error streams, return result

        Process p = Runtime.getRuntime().exec(command);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = input.readLine()) != null) {
            sb.append(line).append("\n");
        }
        input.close();

        return sb.toString();
    }

    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }

}
