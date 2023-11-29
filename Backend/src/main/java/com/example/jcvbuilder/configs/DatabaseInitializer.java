package com.example.jcvbuilder.configs;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements InitializingBean {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            createUsersTable();
            //createSesssionTable();
            System.out.println("DB INITIALIZED");
        }
        catch(Exception e){
            e.fillInStackTrace();
        }
    }

    private void createUsersTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(40) UNIQUE, password VARCHAR(40))";
        jdbcTemplate.execute(createTableSQL);

        // Insert default user if not exists
        String insertUserSQL = "INSERT IGNORE INTO users (username, password) VALUES ('admin', 'admin')";
        jdbcTemplate.execute(insertUserSQL);
    }
}


