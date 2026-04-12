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
public class UniversalCommandInjectionController {

    // Определение операционной системы
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final boolean IS_WINDOWS = OS.contains("win");
    private static final boolean IS_UNIX = OS.contains("nix") || OS.contains("nux") || OS.contains("mac");

    // Получение командной оболочки и флага для выполнения команды в зависимости от ОС
    private static String getShell() {
        if (IS_WINDOWS) {
            return "cmd /c";
        } else if (IS_UNIX) {
            return "sh -c";
        } else {
            throw new UnsupportedOperationException("Неподдерживаемая операционная система: " + OS);
        }
    }


    // TODO: problems with encoding in chrome
    @GetMapping("ping")
    public String executePingCommand(@RequestParam String ip) throws IOException {
        // http://localhost:7171/ping?ip=127.0.0.2 %26 dir %26 whoami /groups
        String pingCommand = getShell();
        if (IS_WINDOWS) {
            pingCommand += "ping -n 1 " + ip;
        } else {
            pingCommand += "ping -c 1 " + ip;
        }

        Process p = Runtime.getRuntime().exec(pingCommand);

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
        String pingCommand = getShell();
        if (IS_WINDOWS) {
            pingCommand += "ping -n 1 " + ip;
        } else {
            pingCommand += "ping -c 1 " + ip;
        }

        ProcessBuilder pb = new ProcessBuilder(pingCommand);
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
        List<String> command = new ArrayList<>();

        if (IS_WINDOWS) {
            command.add("ping");
            command.add("-n");
            command.add("1");
            command.add(ip);
        } else {
            command.add("ping");
            command.add("-c");
            command.add("1");
            command.add(ip);
        }

        ProcessBuilder pb = new ProcessBuilder(command);
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

        // Добавляем Windows-команды
        allowedCommands.add("dir");
        allowedCommands.add("type");
        allowedCommands.add("echo");
        allowedCommands.add("systeminfo");
        allowedCommands.add("tasklist");

        denyArguments.add("-exec");
        denyArguments.add("/exec");
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
    public String execRuntimeCommandSafety(@RequestParam String command) throws IOException {
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("Укажите команду");
        }

        if (Pattern.matches("^.*(([&|;$><`\\\\!'\"()])|(0x0[Aa])).*$", command)) {
            throw new IllegalArgumentException("Недопустимая команда");
        }

        String strip = command.replaceAll("[&|;$><`\\\\!'\"()]+", "");
        String strip2 = command.replaceAll("[^a-zA-Z 0-9]", "");
        String escape = command.replaceAll("[^a-zA-Z 0-9]", "_");

        final String[] cmd = command.split(" ");

        if (!allowedCommands.contains(cmd[0].toLowerCase())) {
            throw new IllegalArgumentException("Недопустимая команда");
        }
        for (String arg : cmd) {
            if (denyArguments.contains(arg.toLowerCase())) {
                throw new IllegalArgumentException("Недопустимый аргумент");
            }
        }

        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = input.readLine()) != null) {
            sb.append(line).append("\n");
        }
        input.close();

        return sb.toString();
    }

    // Вспомогательный метод для проверки ОС
    @GetMapping("os-info")
    public String getOSInfo() {
        return String.format("Операционная система: %s\nWindows: %s\nUnix: %s\nShell: %s",
                OS, IS_WINDOWS, IS_UNIX, getShell());
    }

    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }
}