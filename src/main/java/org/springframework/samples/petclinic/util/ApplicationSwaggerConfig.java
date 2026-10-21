/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.util;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Java config for SpringDoc OpenAPI 3 documentation plugin
 *
 * @author Vitaliy Fedoriv
 *
 */

@Configuration
public class ApplicationSwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("REST Petclinic backend Api Documentation")
                .description("This is REST API documentation of the Spring Petclinic backend. " +
                    "If authentication is enabled, when calling the APIs use admin/admin")
                .version("1.0")
                .termsOfService("Petclinic backend terms of service")
                .contact(new Contact()
                    .name("Vitaliy Fedoriv")
                    .url("https://github.com/spring-petclinic/spring-petclinic-rest")
                    .email("vitaliy.fedoriv@gmail.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0")));
    }

}
