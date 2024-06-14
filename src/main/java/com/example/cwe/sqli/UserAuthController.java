package com.example.cwe.sqli;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.*;

@RestController
@RequestMapping("sqli/auth")
public class UserAuthController {
    private static final String url = "jdbc:mysql://192.168.10.12:33060/DB1";
    private static final String sql_user = "user";
    private static final String sql_password = "resu";

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static CallableStatement callableStatement;
    private static ResultSet resultSet;

    public static void main(String[] args) {

        UserLogin user = UserLogin.createUser();
        user.loginFromConsole();

        //user.sanitize();
        //user.loginFromUrl(url_str);

        queryAndStatement(user);
        queryAndPreparedStatement(user);
        callableProcedure(user);
        queryAndWhitelist(user);
        queryESAPI(user);

    }

    public static void queryAndStatement(UserLogin user) {
        if (exist(user)) {
            String query = "SELECT id, userName, email, cash FROM users2 WHERE userName = '" + user.userName +
                    "' AND password_md5 = '" + DigestUtils.md5Hex(user.password) + "'";
            try {
                connection = DriverManager.getConnection(url, sql_user, sql_password);
                statement = connection.createStatement();
                System.out.println("\nYour query: " + query);//try "user' or '1'='1' -- "
                resultSet = statement.executeQuery(query);
                System.out.println("Результат SQL запроса с помощью Statement:\n");

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
                    if (statement != null) statement.close();
                } catch (SQLException ignored) {
                }
                try {
                    if (resultSet != null) resultSet.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public static void queryAndPreparedStatement(UserLogin user) {
        if (exist(user)) {
            String query = "SELECT id, userName, email, cash FROM users2 WHERE userName = ? AND password_md5 = ?";
            try {
                connection = DriverManager.getConnection(url, sql_user, sql_password);
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, user.userName);
                preparedStatement.setString(2, DigestUtils.md5Hex(user.password));

                String str = preparedStatement.toString();
                System.out.println("\nYour query: " + str.substring(str.indexOf(":") + 2));

                resultSet = preparedStatement.executeQuery();
                System.out.println("Результат SQL запроса с помощью Prepared Statement:\n");

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
                    if (preparedStatement != null) preparedStatement.close();
                } catch (SQLException ignored) {
                }
                try {
                    if (resultSet != null) resultSet.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

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
    }

}
