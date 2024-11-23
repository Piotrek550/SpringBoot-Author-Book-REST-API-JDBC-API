package com.bednarskibrzozowski.database;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log
public class BookApp {

    public static void main(String[] args) {
        SpringApplication.run(BookApp.class, args);
    }
}
