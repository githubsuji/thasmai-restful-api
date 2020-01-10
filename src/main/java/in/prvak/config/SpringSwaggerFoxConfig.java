package in.prvak.config;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import in.prvak.common.annotations.RestApiController;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
@PropertySource("classpath:swagger.properties")

public class SpringSwaggerFoxConfig {
	   @Bean
	    public Docket apiDocket() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .select()
	                //.apis(RequestHandlerSelectors.basePackage("in.prvak"))
	                .apis(RequestHandlerSelectors.withClassAnnotation(RestApiController.class))
//	                .paths(PathSelectors.ant("/v1/**"))
	                .paths(PathSelectors.any())
	                .build()
	                .apiInfo(getApiInfo());
	    }
//	   @Bean
//	    public Docket apiDocket() {
//	        return new Docket(DocumentationType.SWAGGER_2)
//	                .select()
//	                .apis(RequestHandlerSelectors.any())
//	                .paths(PathSelectors.any())
//	                .build() .apiInfo(getApiInfo());
//	    }
	    private ApiInfo getApiInfo() {
	        return new ApiInfo(
	                "Restful Service Using Spring Boot and Spring JPA",
	                "This application demonstrates Restful Service.",
	                "1.0.0",
	                "TERMS OF SERVICE URL",
	                new Contact("PRVAK Technology Solutions (P) Ltd.", "http://www.prvak.in", "sujits82228@gmail.com"),
	                "MIT License",
	                "https://github.com/sujitsgithub/springboot-2.2-restful-startup-kit/blob/master/LICENSE",
	                Collections.emptyList()
	        );
	    }
}
