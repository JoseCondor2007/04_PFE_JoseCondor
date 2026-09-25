package pe.edu.vallegrande;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "pe.edu.vallegrande")
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
