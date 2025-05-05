package com.bookmyshow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bookMyShowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book My Show API")
                        .description("Backend APIs for managing bookings, movies, seats, and shows.")
                        .version("1.0"));
    }
}
