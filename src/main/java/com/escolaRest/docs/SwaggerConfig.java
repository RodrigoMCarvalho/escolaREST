package com.escolaRest.docs;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
	
	@Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                	.apis(RequestHandlerSelectors.basePackage("com.escolaRest.resource"))
                	.paths(regex("/v1.*"))
                	.build()
                .globalOperationParameters(Collections.singletonList(new ParameterBuilder()
                		.name("Authorization")
                		.description("Bearer token")
                		.modelRef(new ModelRef("string"))
                		.parameterType("header")
                		.required(true)
                		.build()))
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {
    	
    	return new ApiInfoBuilder()
    			.title("ESCOLA API REST")
    			.description("API REST EscolaRest")
    			.version("1.0")
    			.contact(new Contact("Rodrigo Moreira Carvalho", "https://github.com/RodrigoMCarvalho",
                       "rodrigo_moreira84@hotmail.com"))
    			.license("Apache License Version 2.0")
    			.licenseUrl("https://www.apache.org/licesen.html")
    			.build();
    			

//		ApiInfo apiInfo = new ApiInfo(
//                "ESCOLA API REST",
//                "API REST EscolaRest",
//                "1.0",
//                "Terms of Service",
//                new Contact("Rodrigo Moreira Carvalho", "https://github.com/RodrigoMCarvalho",
//                        "rodrigo_moreira84@hotmail.com"),
//                "Apache License Version 2.0",
//                "https://www.apache.org/licesen.html", new ArrayList<VendorExtension>()
//        );

//       return apiInfo;
}

}
