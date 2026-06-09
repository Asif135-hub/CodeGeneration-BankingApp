package nl.inholland.codegen.bankingapp;

import nl.inholland.codegen.bankingapp.seeder.DataSeederService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankingappApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingappApplication.class, args);
    }

    @Bean
    CommandLineRunner run(DataSeederService seeder) {
        return args -> seeder.seed();
    }
}
