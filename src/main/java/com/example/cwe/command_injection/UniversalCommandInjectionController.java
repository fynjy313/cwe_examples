package com.example.cwe.command_injection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("cmd")
public class UniversalCommandInjectionController {

    // Определение операционной системы
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final boolean IS_WINDOWS = OS.contains("win");
    private static final boolean IS_UNIX = OS.contains("nix") || OS.contains("nux") || OS.contains("mac");


    @GetMapping("ping")
    public String ping(@RequestParam String ip) {
        //Runtime.getRuntime().exec() по умолчанию не интерпретирует спецсимволы shell (;, |, &, $, `). Для настоящего RCE нужно явно вызвать shell через /bin/sh -c или cmd.exe /c.
        //Example: /cmd/ping?ip=127.0.0.1; pwd; id; cat /etc/passwd

        try {
            String[] command;
            if (IS_WINDOWS) {
                command = new String[]{"cmd", "/c", "ping -n 1 " + ip};
            } else {
                command = new String[]{"/bin/sh", "-c", "ping -c 1 " + ip};
            }

            System.out.println("\n\tCommand: " + Arrays.toString(command));

            Process process = Runtime.getRuntime().exec(command);

            // Читаем вывод
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Читаем ошибки
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                output.append("ERROR: ").append(line).append("\n");
            }

            int exitCode = process.waitFor();
            output.append("Exit code: ").append(exitCode);

            return output.toString();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    @GetMapping("pingPb1")
    public String pingProcessUnsafe(@RequestParam String ip) throws IOException {
        try {
            String[] command;
            if (IS_WINDOWS) {
                command = new String[]{"cmd", "/c", "ping -n 1 " + ip};
            } else {
                command = new String[]{"/bin/sh", "-c", "ping -c 1 " + ip};
            }

            ProcessBuilder pb = new ProcessBuilder(command);
            System.out.println(pb.command());
            Process p = pb.start();

            // Читаем вывод
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Читаем ошибки
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                output.append("ERROR: ").append(line).append("\n");
            }

            int exitCode = p.waitFor();
            output.append("Exit code: ").append(exitCode);

            return output.toString();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("pingPb2")
    public String pingProcessSafe(@RequestParam String ip) throws IOException {
        List<String> command = new ArrayList<>();
        try {
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

            // Читаем вывод
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Читаем ошибки
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                output.append("ERROR: ").append(line).append("\n");
            }

            int exitCode = p.waitFor();
            output.append("Exit code: ").append(exitCode);

            return output.toString();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    static Set<String> allowedCommands = new HashSet<>();
    static Set<String> deniedArguments = new HashSet<>();

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

        deniedArguments.add("-exec");
        deniedArguments.add("/exec");
    }

    /**
     *
     * @param command exp: cmd/cmdSafety?command=uname&args=-a  -   good
     *                exp: cmd/cmdSafety?command=find&args=.&args=-exec&args=whoami%20%5C%3B    -   bad (find . -exec whoami \;)
     * @param args
     * @return
     * @throws IOException
     */
    @GetMapping("cmdSafety")
    public String execCommandSafety(@RequestParam(value = "command") String command,
                                    @RequestParam(value = "args", required = false) List<String> args) throws IOException {
        final List<String> cmd = new ArrayList<>();

        if (command == null || command.isEmpty()) {
            return "Укажите команду";
            //throw new IllegalArgumentException("Укажите команду");
        }
        command = command.trim().toLowerCase();

        if (!allowedCommands.contains(command)) {
            return "Недопустимая команда:" + command;
        }

        cmd.add(command);

        if (args != null) {
            for (String arg : args) {
                if (deniedArguments.contains(arg.trim().toLowerCase())) {
                    return "Недопустимый аргумент: " + arg;
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


    /**
     * exp: cmd/cmdRuntimeSafe?command=uname -a  -   good
     * exp: cmd/cmdRuntimeSafe?command=find -exec    -   bad
     * @param command
     * @return
     * @throws IOException
     */
    @GetMapping("cmdRuntimeSafe")
    public String execRuntimeCommandSafety(@RequestParam String command) throws IOException {
        if (command == null || command.isEmpty()) {
            return "Укажите команду";
        }

        if (Pattern.matches("^.*(([&|;$><`\\\\!'\"()])|(0x0[Aa])).*$", command)) {
            return "Недопустимая команда:" + command;
        }

        String strip = command.replaceAll("[&|;$><`\\\\!'\"()]+", "");
        String strip2 = command.replaceAll("[^a-zA-Z 0-9]", "");
        String escape = command.replaceAll("[^a-zA-Z 0-9]", "_");

        final String[] cmd = command.split(" ");

        if (!allowedCommands.contains(cmd[0].toLowerCase())) {
            return "Недопустимая команда:" + command;
        }
        for (String arg : cmd) {
            if (deniedArguments.contains(arg.toLowerCase())) {
                return "Недопустимый аргумент: " + arg;
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
        return String.format("Операционная система: %s\nWindows: %s\nUnix: %s", OS, IS_WINDOWS, IS_UNIX);
    }

    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }
}