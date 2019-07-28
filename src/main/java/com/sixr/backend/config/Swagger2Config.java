package com.sixr.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

// http://localhost:2019/swagger-ui.html
@Configuration
public class Swagger2Config
{
    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2).select()
//                                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.sixr.backend"))
                .paths(PathSelectors.any()).build().useDefaultResponseMessages(false) // Allows only my exception responses
                .ignoredParameterTypes(Pageable.class) // allows only my paging parameter list
                .apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo()
    {
        return new ApiInfoBuilder().title("Java Spring Back End Sixr")
                .description("Java Spring Back End for Sixr Project")
                .contact(new Contact("Brandon Porter", "http://www.lambdaschool.com", "brandon.porter.10.06@google.com"))
                .license("MIT").licenseUrl("https://github.com/LambdaSchool/java-starthere/blob/master/LICENSE").version("1.0.0").build();
    }
}
