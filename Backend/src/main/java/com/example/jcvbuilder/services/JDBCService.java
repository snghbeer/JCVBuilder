package com.example.jcvbuilder.services;

import com.example.jcvbuilder.models.DTO.UserDTO;
import com.example.jcvbuilder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
@Service
public class JDBCService {

    private static Connection connection;

    // JDBC URL, username, and password of MySQL server
    @Value("spring.datasource.url")
    private static String jdbcUrl = "jdbc:mysql://root:Lolpapa1@localhost:3306/jcvbuilder";
    @Value("spring.datasource.username")
    private static String username;
    @Value("spring.datasource.password")
    private static String password;

    public JDBCService() throws SQLException{
        try{
            connection = DriverManager.getConnection(jdbcUrl, username, password );
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet executeFindQuery(String query, Object... params) throws SQLException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Bind parameters if any
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            // Handle the exception or rethrow it
            throw e;
        }
    }


    public UserDTO findUserByUsername(String username, String inputPassword) {
        String query = "SELECT * FROM users WHERE username = ?";
        //        String query = String.format("SELECT * FROM users WHERE username = %s!", username);
        try (ResultSet resultSet = executeFindQuery(query, username)) {
            if (resultSet.next()) {
                String userPassword = resultSet.getString("password");
                if(!userPassword.equals(inputPassword))  return null;
                return new UserDTO(username, inputPassword);
            } else return null;
        } catch (SQLException e) {
            // Handle the exception or log it
            e.printStackTrace();
            return null;
        }
    }

    public Boolean registerUser(String username, String inputPassword) {
        UserDTO aUser = this.findUserByUsername(username, password);
        if(aUser == null){
            //insert
            System.out.println("inserting...");
            String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, inputPassword);

                // Execute the insert statement
                int rowsAffected = preparedStatement.executeUpdate();

                // Check if the insert was successful
                return rowsAffected > 0;
            } catch (SQLException e) {
                // Handle the exception
                return false;
            }
        }
        else return false;
    }
}
