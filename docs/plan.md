# Migration Plan: Spring Boot → Quarkus 3

This document outlines the steps to migrate the Spring PetClinic REST application from Spring Boot 2.6.2 to Quarkus 3.

## Steps

### Step 1: Update pom.xml - Build Configuration
- Replace `spring-boot-starter-parent` with Quarkus BOM (`quarkus-bom`)
- Add Quarkus Maven plugin
- Update dependency coordinates:
  - `spring-boot-starter-web` → `quarkus-resteasy` + `quarkus-resteasy-jackson`
  - `spring-boot-starter-data-jpa` → `quarkus-hibernate-orm-panache`
  - `spring-boot-starter-jdbc` → `quarkus-jdbc-h2` (for HSQLDB compat) + `quarkus-jdbc-postgresql` + `quarkus-jdbc-mysql`
  - `spring-boot-starter-security` → `quarkus-vertx-http-extension-security` or `quarkus-smallrye-jwt`
  - `spring-boot-starter-validation` → `quarkus-hibernate-validator`
  - `spring-boot-starter-actuator` → `quarkus-micrometer-registry-prometheus`
  - `spring-boot-starter-aop` → `quarkus-smallrye-context-propagation` (aspects not needed, use CDI)
  - `spring-boot-starter-cache` → `quarkus-smallrye-health`
  - `springfox-swagger` → `quarkus-smallrye-openapi`
  - `spring-boot-starter-test` → `quarkus-junit5` + `rest-assured`
  - `spring-security-test` → `quarkus-junit5-security`
- Update mapstruct version to latest compatible
- Remove `openapi-generator-maven-plugin` and `build-helper-maven-plugin` (use quarkus-openapi-generator if needed)
- Update maven-compiler-plugin for Quarkus

### Step 2: Update Application Configuration
- Replace `application.properties` with `application.properties` for Quarkus
- Map Spring Boot properties to Quarkus equivalents:
  - `spring.profiles.active` → `quarkus.profile`
  - `server.port` → `quarkus.http.port`
  - `server.servlet.context-path` → `quarkus.http.root-path`
  - `spring.mvc.pathmatch.matching-strategy` → removed (not needed in Quarkus)
  - `spring.jpa.*` → `quarkus.hibernate-orm.*`
  - `spring.datasource.*` → `quarkus.datasource.*`
  - `spring.messages.*` → `quarkus.i18n.*`
  - `petclinic.security.*` → keep as custom config

### Step 3: Update Main Application Class
- Replace `@SpringBootApplication` with Quarkus annotations
- Remove `SpringBootServletInitializer`

### Step 4: Migrate JPA Entities (javax.persistence → jakarta.persistence)
All entity classes need their imports changed from `javax.persistence.*` to `jakarta.persistence.*`

### Step 5: Migrate REST Controllers
Change `javax.ws.rs.*` and `javax.annotation.*` imports to `jakarta.ws.rs.*` and `jakarta.annotation.*`

### Step 6: Migrate Security Configuration
- Replace Spring Security with Quarkus security
- `BasicAuthenticationConfig` → Quarkus security config
- `DisableSecurityConfig` → Quarkus security config
- `Roles` → Quarkus security roles

### Step 7: Migrate Service Layer
- Replace Spring Data JPA interfaces with Panache repositories
- Update `clinicService` and `userService` implementations

### Step 8: Replace Springfox Swagger with SmallRye OpenAPI
- Remove `springfox-swagger` dependency
- Use `quarkus-smallrye-openapi` instead
- Update `ApplicationSwaggerConfig` → SmallRye OpenAPI annotations

### Step 9: Migrate Cross-Cutting Concerns (Aspects)
- `CallMonitoringAspect` → Use Quarkus CDI interceptor or Micrometer

### Step 10: Migrate Repository Layer
- JDBC repositories: Replace with Panache or CDI-based alternatives
- JPA repositories: Replace with Panache repositories
- Spring Data JPA interfaces: Replace with Panache repositories

### Step 11: Migrate Mapper Classes
- Update MapStruct mappers if needed for Jakarta EE namespace

### Step 12: Update Resources and Test Configuration
- Update test application.properties
- Remove Spring-specific test configurations
