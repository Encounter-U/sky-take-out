/*
package com.sky.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

*/
/**
 * @author Encounter
 * @date 2024/09/05 18:44<br/>
 *//*


@Configuration
public class OpenAPIConfig implements WebMvcConfigurer
    {
        @Bean
        public OpenAPI springShopOpenAPI()
            {
                return new OpenAPI().info(new Info().title("Spring Boot 中使用 Swagger UI 构建 RESTful API").contact(new Contact()).description("Sun提供的 RESTful API").version("v1.0.0").license(new License().name("Apache 2.0").url("http://springdoc.org"))).externalDocs(new ExternalDocumentation().description("外部文档").url("https://springshop.wiki.github.org/docs"));
            }
        
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry)
            {
                registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/").resourceChain(false);
                registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
                registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
                registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
            }
    }

*/
