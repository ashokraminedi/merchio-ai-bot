package com.merchio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MerchioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MerchioApplication.class, args);

        System.out.println("""
            
            ╔═══════════════════════════════════════╗
            ║                                       ║
            ║     🛍️  Merchio is Running! 🛍️       ║
            ║                                       ║
            ║  Your Intelligent Shopping Companion  ║
            ║                                       ║
            ║  API: http://localhost:8080          ║
            ║                                       ║
            ╚═══════════════════════════════════════╝
            
            """);
    }

}
