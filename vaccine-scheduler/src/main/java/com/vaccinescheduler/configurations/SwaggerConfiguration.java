package com.vaccinescheduler.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalTime;
import java.util.Collections;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .directModelSubstitute(LocalTime.class, Long.class)
                .select().build();
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Vaccine Scheduler",
                "This is a spring-boot REST API.",
                "1.0",
                "urn:tos",
                new Contact(
                        "Gaurav",
                        "https://gauravdhamal.github.io",
                        "gauravdhamal11@gmail.com"
                ),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                Collections.emptyList()
        );
    }
}
