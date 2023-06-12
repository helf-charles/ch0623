package com.chahel.ch0623.configuration;

import com.chahel.ch0623.repository.ToolRentalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolRentalConfig {
    private static final Logger log = LoggerFactory.getLogger(ToolRentalConfig.class);

    @Bean
    CommandLineRunner initToolRentalApp(ToolRentalRepository repo) {
        return args -> {
            log.info("Application running! Yay!");
        };
    }
}
