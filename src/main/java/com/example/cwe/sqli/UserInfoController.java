package com.example.cwe.sqli;

import com.example.cwe.sqli.entity.AuthLoginForm;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

@RestController
@RequestMapping("sqli/user-info")
public class UserInfoController {
    private static final String url = "jdbc:h2:file:./data/demo;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE;";
    private static final String sql_user = "sa";
    private static final String sql_password = "";

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static CallableStatement callableStatement;
    private static ResultSet resultSet;

    //SQL injection: user' or '1'='1' --
    @PostMapping(value = "statement", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserInfoWithStatement(@RequestBody AuthLoginForm loginForm) {

        if (!exist(loginForm)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty login/password");
        }

        String query = "SELECT id, userName, email, cash FROM Wallets WHERE username = '" + loginForm.username() +
                "' AND password = '" + DigestUtils.md5Hex(loginForm.password()) + "'";

        try (Connection connection = DriverManager.getConnection(url, sql_user, sql_password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("\nYour query: " + query);

            return ResponseEntity.ok("Query: " + query + "\n" +
                    "Результат SQL запроса с помощью Statement:\n" + printResult(resultSet));

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("SQLException: " + e.getMessage()); //bad idea, only for exp
        }
    }

    @PostMapping(value = "prepared-statement", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserInfoWithPreparedStatement(@RequestBody AuthLoginForm loginForm) {

        if (!exist(loginForm)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty login/password");
        }

        String query = "SELECT id, userName, email, cash FROM Wallets WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(url, sql_user, sql_password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, loginForm.username());
            statement.setString(2, DigestUtils.md5Hex(loginForm.password()));

            ResultSet resultSet = statement.executeQuery();
            query = statement.toString().substring(query.indexOf(":") + 2);

            return ResponseEntity.ok("Query: " + query + "\n" +
                    "Результат SQL запроса с помощью Prepared Statement:\n" + printResult(resultSet));

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("SQLException: " + e.getMessage()); //bad idea, only for exp
        }
    }


    //TODO: callable statement

    @PostMapping(value = "callable-statement", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserInfoWithCallableStatement(@RequestBody AuthLoginForm loginForm) {
        if (!exist(loginForm)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty login/password");
        }

        String query = "SELECT id, userName, email, cash FROM Wallets WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(url, sql_user, sql_password);
             CallableStatement statement = connection.prepareCall("{call userinfo(?,?)}")) {

            statement.setString(1, loginForm.username());
            statement.setString(2, DigestUtils.md5Hex(loginForm.password()));

            ResultSet resultSet = statement.executeQuery();

            System.out.println("\nРезультат SQL запроса с помощью Callable Procedure:\n");

            query = statement.toString().substring(query.indexOf(":") + 2);

            return ResponseEntity.ok("Query: " + query + "\n" +
                    "Результат SQL запроса с помощью Callable Procedure:\n" + printResult(resultSet));

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("SQLException: " + e.getMessage()); //bad idea, only for exp
        }
    }

    private static boolean exist(AuthLoginForm loginForm) {
        return (loginForm.username() != null) && (!loginForm.username().isEmpty())
                && (loginForm.password() != null) && (!loginForm.password().isEmpty());
    }

    public static String printResult(ResultSet resultSet) throws SQLException {
        StringBuilder sb = new StringBuilder();
        while (resultSet.next()) {
            sb.append(String.format("User ID: [%d]\t|\tUsername: [%s]\t|\temail: [%s]\t|\t$cash: [%d]\n",
                    resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getInt("cash")));
        }
        return sb.toString();
    }


/*
    public static void callableProcedure(UserLogin user) {
        if (exist(user)) {
            try {
                connection = DriverManager.getConnection(url, sql_user, sql_password);
                callableStatement = connection.prepareCall("{call proc2(?,?)}");
                callableStatement.setString(1, user.userName);
                callableStatement.setString(2, DigestUtils.md5Hex(user.password));
                resultSet = callableStatement.executeQuery();
                System.out.println("\nРезультат SQL запроса с помощью Callable Procedure:\n");

                while (resultSet.next()) {
                    printResult(resultSet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) connection.close();
                } catch (SQLException ignored) {
                }
                try {
                    if (callableStatement != null) callableStatement.close();
                } catch (SQLException ignored) {
                }
                try {
                    if (resultSet != null) resultSet.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public static void queryAndWhitelist(UserLogin user) {
        String value; //можно сделать enum и проверять на наличие value в enum
        try {
            value = switch (user.userName) {
                case "Administrator" -> "admin";
                case "User" -> "user";
                case "Sanya" -> "sanek";
                case "User2" -> "user2";
                default -> throw new InputValidationException("Unexpected value provided for user " +
                        "\"" + user.userName + "\"");
            };
        } catch (InputValidationException e) {
            e.printStackTrace();
        }

        String query = "SELECT id, userName, email, cash FROM users2 WHERE userName = '" + value +
                "' AND password_md5 = '" + DigestUtils.md5Hex(user.password) + "'";
        try {
            connection = DriverManager.getConnection(url, sql_user, sql_password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            System.out.println("Результат SQL запроса с использованием Whitelist:\n");
            while (resultSet.next()) {
                printResult(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Результат SQL запроса с использованием Whitelist:\n");
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException ignored) {
            }
            try {
                if (statement != null) statement.close();
            } catch (SQLException ignored) {
            }
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException ignored) {
            }
        }

    }

    public static void queryESAPI(UserLogin user) {
        if (exist(user)) {
            PrintStream consoleStream = System.out;
            try {
                PrintStream esapiLogStream = new PrintStream(
                        new FileOutputStream("esapiLog.txt", true));
                System.setOut(esapiLogStream);
                System.setErr(esapiLogStream);
                MySQLCodec codec = new MySQLCodec(MySQLCodec.Mode.STANDARD);
                Encoder encoder = ESAPI.encoder();

                String query = "SELECT id, userName, email, cash FROM users2 WHERE userName = '" +
                        encoder.encodeForSQL(codec, user.userName) + "' AND password_md5 = '" +
                        DigestUtils.md5Hex(user.password) + "'";
                System.setOut(consoleStream);
                System.setErr(consoleStream);

                connection = DriverManager.getConnection(url, sql_user, sql_password);
                statement = connection.createStatement();
                System.out.println("\nYour query: " + query);
                resultSet = statement.executeQuery(query);
                System.out.println("Результат SQL запроса с помощью Statement, обработанный ESAPI:\n");
                while (resultSet.next()) {
                    printResult(resultSet);
                }
                esapiLogStream.close();

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null)
                        connection.close();
                } catch (SQLException ignored) {
                }
                try {
                    if (statement != null)
                        statement.close();
                } catch (SQLException ignored) {
                }
                try {
                    if (resultSet != null)
                        resultSet.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private static boolean exist(UserLogin user) {
        if ((user.userName != null) && (user.password != null)) return true;
        return false;
    }

    public static void printResult(ResultSet resultSet) throws SQLException {    // Printer
        System.out.printf("User ID: [%d]\t|\tUser name: [%s]\t|\teMail: [%s]\t|\t$cash: [%d]\n",
                resultSet.getInt("id"),
                resultSet.getString("userName"),
                resultSet.getString("email"),
                resultSet.getInt("cash"));
    }*/

}
