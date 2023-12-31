package ru.softplat.security.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket mainApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.softplat.security.server.dto")
                        .or(RequestHandlerSelectors.basePackage("ru.softplat.security.server.web.controller"))
                        .or(RequestHandlerSelectors.basePackage("ru.softplat.security.dto"))
                        .or(RequestHandlerSelectors.basePackage("ru.softplat.stats.dto"))
                        .or(RequestHandlerSelectors.basePackage("ru.softplat.main.dto"))
                        .or(RequestHandlerSelectors.basePackage("ru.softplat.stats.server.dto")))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Softplat Backend REST API",
                "Describes API for backend of Softplat Project",
                null, null, null, null, null, Collections.emptyList()
        );
    }
}
