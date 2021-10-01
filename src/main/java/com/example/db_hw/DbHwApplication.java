package com.example.db_hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbHwApplication {

    public static String[] dbinfo = new String[3];


    public static void main(String[] args) {
        try {
            System.out.println("url is " + args[0]);
            System.out.println("username is " + args[1]);
            System.out.println("password is " + args[2]);
            dbinfo[0] = args[0];
            dbinfo[1] = args[1];
            dbinfo[2] = args[2];
        }catch (Exception e){
            System.out.println("No datasource info provided");
            System.exit(0);
        }
        SpringApplication.run(DbHwApplication.class, args);
    }

}
