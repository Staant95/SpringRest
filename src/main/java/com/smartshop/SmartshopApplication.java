package com.smartshop;

import com.smartshop.seeders.DatabaseSeeder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartshopApplication implements CommandLineRunner {

    private final DatabaseSeeder seeder;


    public static void main(String[] args) {

        SpringApplication.run(SmartshopApplication.class, args);

    }


    public SmartshopApplication(DatabaseSeeder seeder) {
        this.seeder = seeder;
    }


    @Override
    public void run(String... args) {
        // you might want to comment this out
        this.seeder.seed();
    }
}
