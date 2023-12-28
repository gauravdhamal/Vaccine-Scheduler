package com.vaccinescheduler.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfiguration {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .securityContexts(securityContexts())
                .securitySchemes(Arrays.asList(apiKeys()))
                .directModelSubstitute(LocalTime.class, Long.class)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
    private ApiKey apiKeys() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }
    private List<SecurityContext> securityContexts() {
        return Arrays.asList(
                SecurityContext.builder()
                .securityReferences(securityReferences())
                .build());
    }
    private List<SecurityReference> securityReferences() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        return Arrays.asList(new SecurityReference("JWT", new AuthorizationScope[]{authorizationScope}));
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
