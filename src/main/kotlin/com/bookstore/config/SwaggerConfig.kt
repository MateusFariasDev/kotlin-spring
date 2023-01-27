package com.bookstore.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

//@Configuration
//@EnableSwagger2
//@EnableWebMvc
//class SwaggerConfig : WebMvcConfigurer {
//    @Bean
//    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
//        .select()
//        .apis(RequestHandlerSelectors.basePackage("com.bookstore.controller"))
//        .paths(PathSelectors.any())
//        .build()
//
//        .apiInfo(ApiInfoBuilder()
//            .title("Book Store")
//            .description("Book Store API")
//            .build()
//        )
//}