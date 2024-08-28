package com.example.cwe.sqli;

import com.example.cwe.sqli.entity.AuthLoginForm;
import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.*;

@RestController
@RequestMapping("sqli/user-info")
public class UserInfoController {
    private static final String url = "jdbc:h2:file:./data/demo;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE;";
    private static final String sql_user = "sa";
    private static final String sql_password = "";

    //TODO: all params from property file
    @GetMapping("/")
    ModelAndView loginTest() {
        return new ModelAndView("login.jsp");
    }


    /**
     * POST /sqli/user-info/statement
     * SQL injection example:
     * {
     * "username": "admin' or '1'='1' --",
     * "password": " "
     * }
     */
    @PostMapping(value = "statement", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserInfoWithStatement(@RequestBody AuthLoginForm loginForm) {

        if (!exist(loginForm)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty login/password");
        }

        String query = "SELECT id, userName, email, cash FROM Wallets WHERE username = '"
                + loginForm.username() + "' AND password = '"
                + DigestUtils.md5Hex(loginForm.password()) + "'";

        try (Connection connection = DriverManager.getConnection(url, sql_user, sql_password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            return ResponseEntity.ok("Query: " + query + "\n" +
                    "Результат SQL запроса с помощью Statement:\n" + printResult(resultSet));

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("SQLException: " + e.getMessage()); //bad idea, only for exp
        }
    }

    /**
     * POST /sqli/user-info/prepared-statement
     * No SQL injection:
     * {
     * "username": "admin' or '1'='1' --",
     * "password": " "
     * }
     */
    @PostMapping(value = "prepared-statement", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserInfoWithPreparedStatement(@RequestBody AuthLoginForm loginForm) {

        if (!exist(loginForm))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty login/password");

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


    //callable statement example in H2DB:
    //https://stackoverflow.com/questions/11718865/stored-procedure-in-h2-database
    //The example below only works in mysql and similar databases.

    //@PostMapping(value = "callable-statement", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserInfoWithCallableStatement(@RequestBody AuthLoginForm loginForm) {
        if (!exist(loginForm)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty login/password");
        }

        try (Connection connection = DriverManager.getConnection(url, sql_user, sql_password);
             CallableStatement statement = connection.prepareCall("{call userinfo(?,?)}")) {

            statement.setString(1, loginForm.username());
            statement.setString(2, DigestUtils.md5Hex(loginForm.password()));
            ResultSet resultSet = statement.executeQuery();
            String query = statement.toString();

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
*/

    @PostMapping(value = "statement", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doSql(@RequestBody String login) {

        String query = "SELECT id, userName, email, cash FROM Wallets WHERE username = '" + login;

        try (Connection connection = DriverManager.getConnection(url, sql_user, sql_password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            return ResponseEntity.ok("Query: " + query + "\n" +
                    "Результат SQL запроса с помощью Statement:\n" + printResult(resultSet));

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("SQLException: " + e.getMessage()); //bad idea, only for exp
        }
    }

}
