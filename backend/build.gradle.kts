plugins {
	java
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<Exec>("installFrontend") {
	inputs.file("../frontend/package.json")
	inputs.file("../frontend/package-lock.json")
	outputs.dir("../frontend/node_modules")
	environment("NODE_ENV", "development") // Needed so that "vite" is installed (e.g. we are able to build!)
	commandLine("env")
	commandLine("npm", "--prefix", "../frontend", "install", "../frontend")
}

tasks.register<Exec>("buildFrontend") {
	dependsOn("installFrontend")
	inputs.dir("../frontend")
	outputs.dir("../frontend/dist")
	environment("NODE_ENV", "production")
	commandLine("npm", "--prefix", "../frontend", "run", "build")
}

tasks.register<Sync>("copyFrontend") {
	dependsOn("buildFrontend")
	from("../frontend/dist")
	into("$buildDir/resources/main/static")
	doLast {
		println("copied built frontend to static resources")
	}
}

tasks.register<Delete>("cleanFrontend") {
	delete("../frontend/dist")
	delete("../src/main/resources/static")
}

// Needed for Gradle v8
tasks.resolveMainClassName {
	dependsOn("copyFrontend")
}

tasks.jar {
	dependsOn("copyFrontend")
}

tasks.bootRun {
	dependsOn("copyFrontend")
}

tasks.clean {
	dependsOn("cleanFrontend")
}
