package lk.sliit.it25.bakeryweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"lk.sliit.it25.bakeryweb", "com.bakery.feedback"})
public class OrderbookingsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderbookingsApplication.class, args);
    }
}
