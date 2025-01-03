plugins {
	java
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.study"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Boot
	implementation("org.springframework.boot:spring-boot-starter-web")

	// JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// PostgreSQL
	runtimeOnly("org.postgresql:postgresql")

	// MySQL
	runtimeOnly("com.mysql:mysql-connector-j")

	// Redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis")

	// Caffeine (Cache)
	implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

	// Kryo (Serialization)
	implementation("com.esotericsoftware:kryo:5.6.2")

	// Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
