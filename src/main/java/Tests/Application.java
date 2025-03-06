package Tests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"Tests", "Gui", "Services"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}