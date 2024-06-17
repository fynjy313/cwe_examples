package com.example.cwe.sqli;

import com.example.cwe.sqli.entity.AuthLoginForm;
import org.apache.commons.codec.digest.DigestUtils;
import org.testng.annotations.Test;

import java.sql.*;

public class UserAuthControllerTest {

    private static final String url = "jdbc:h2:file:./data/demo;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE;";
    private static final String sql_user = "sa";
    private static final String sql_password = "";

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static CallableStatement callableStatement;
    private static ResultSet resultSet;

    @Test
    void queryAndStatement() {
        AuthLoginForm loginForm = new AuthLoginForm("admin", "admin");

        if (exist(loginForm)) {
            String query = "SELECT id, userName, email, cash FROM Wallets WHERE username = '" + loginForm.username() +
                    "' AND password = '" + DigestUtils.md5Hex(loginForm.password()) + "'";
            try {
                connection = DriverManager.getConnection(url, sql_user, sql_password);
                statement = connection.createStatement();
                System.out.println("\nYour query: " + query); //try "user' or '1'='1' -- "
                resultSet = statement.executeQuery(query);
                System.out.println("Результат SQL запроса с помощью Statement:\n");

                printResult(resultSet);

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

    @Test
    void queryAndStatementCopy() {
        AuthLoginForm loginForm = new AuthLoginForm("admin' or 1=1 --", "admin");

        if (exist(loginForm)) {
            String query = "SELECT id, userName, email, cash FROM Wallets WHERE username = '" + loginForm.username() +
                    "' AND password = '" + DigestUtils.md5Hex(loginForm.password()) + "'";
            try (Connection connection = DriverManager.getConnection(url, sql_user, sql_password);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                System.out.println("\nYour query: " + query); //try "user' or '1'='1' -- "

                System.out.println("Результат SQL запроса с помощью Statement:\n");
                printResult(resultSet);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public static void queryAndPreparedStatement() {
        AuthLoginForm loginForm = new AuthLoginForm("admin", "admin");

        if (exist(loginForm)) {
            String query = "SELECT id, userName, email, cash FROM Wallets WHERE username = ? AND password = ?";
            try {
                connection = DriverManager.getConnection(url, sql_user, sql_password);
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, loginForm.username());
                preparedStatement.setString(2, DigestUtils.md5Hex(loginForm.password()));

                String str = preparedStatement.toString();
                System.out.println("\nYour query: " + str.substring(str.indexOf(":") + 2));

                resultSet = preparedStatement.executeQuery();
                System.out.println("Результат SQL запроса с помощью Prepared Statement:\n");

                printResult(resultSet);

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

    private static boolean exist(AuthLoginForm loginForm) {
        return (loginForm.username() != null) && (loginForm.password() != null);
    }

    public static void printResult(ResultSet resultSet) throws SQLException {    // Printer
        while (resultSet.next()) {
            System.out.printf("User ID: [%d]\t|\tUser name: [%s]\t|\teMail: [%s]\t|\t$cash: [%d]\n",
                    resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getInt("cash"));
        }
    }
}
