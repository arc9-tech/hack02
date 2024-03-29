package tech.arc9.gateway.configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
//                .paths((Predicate<String>) PathSelectors.regex("/error.*").negate())
//                .paths(PathSelectors.regex("/error.*").negate())
//                .paths(PathSelectors.ant("/error/**").negate())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))

                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(apiEndPointsInfo())
                .securitySchemes(securitySchemes());
    }

    private static List<? extends SecurityScheme> securitySchemes() {
        return Arrays.asList(new ApiKey("Bearer", "Authorization", "header"));
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Sample Gateway App")
                .license("Arc9")
                .licenseUrl("")
                .version("0.0.1")
                .description("REST Gateway")
                .build();
    }
}
