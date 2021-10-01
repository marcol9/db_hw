package com.example.db_hw;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyDB {

    String url = "jdbc:mysql://".concat(DbHwApplication.dbinfo[0]).concat(":3306/") ;
    String username = DbHwApplication.dbinfo[1];
    String password = DbHwApplication.dbinfo[2];
//    String url = "jdbc:mysql://1.2.3.4:3306/?useSSL=false";
    String schemaName = "mydb";
    String tableName = "persons";
    List<String > persons = new ArrayList();
    JdbcTemplate jdbcTemplate = new JdbcTemplate(mysqlDataSource());


    public MyDB(){
        connectAndQuery();
    }

    private void connectAndQuery(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try(Connection conn = DriverManager.getConnection(url, username,password)){
            if(!conn.isClosed()){
                System.out.println("DB Conn ok ");
                initializeDatabase(conn,jdbcTemplate);
//                // Get the rows:
                String sql = "SELECT * FROM " + schemaName +"."+ tableName;
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet resultSet = ps.executeQuery();
                persons.clear();
                while (resultSet.next()){
                    String firstName = resultSet.getString("name");
                    System.out.println("Name: " + firstName);
                    persons.add(firstName);
                }
            }
        }catch (Exception e){
            System.out.println("Error " + e.getMessage());
        }
    }
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    private void initializeDatabase(Connection conn, JdbcTemplate jdbcTemplate) throws Exception{
        boolean empty = true;

        System.out.println("Initializing database");
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS `mydb` ; ");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `mydb`.`persons` (\n" +
                "  `idpersons` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,\n" +
                "  `name` VARCHAR(45) NULL); ");

        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM `mydb`.`persons`");

        if(rs.next()==false) {
            jdbcTemplate.execute(" INSERT INTO  mydb.persons (name) VALUES ('Anna') ");
            jdbcTemplate.execute(" INSERT INTO  mydb.persons (name) VALUES ('Bent') ");
        }

        // 1. make sure there exists a schema, named mydb. If not, create one
        // 2. make sure there exists a table, named persons. If not, create one:
        //    Primary key: idpersons INT AUTO_INCREMENT
        //    Column: name VARCHAR(45)
        // 3. If there was no table named persons, then insert two rows into the new table: "Anna" and "Bent"

    }

    public List<String> getPersons() {
        return persons;
    }

}
