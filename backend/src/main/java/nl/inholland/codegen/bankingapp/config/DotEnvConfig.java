package nl.inholland.codegen.bankingapp.config;

import java.io.BufferedReader;
import java.io.FileReader;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DotEnvConfig {
    static {
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) continue;
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    System.setProperty(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (Exception e) {
            System.out.println(".env not found");
        }
    }
}