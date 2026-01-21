package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

public class TestBase {

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "https://demoqa.com";
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");
        Configuration.pageLoadStrategy = "eager";

        String remote = System.getProperty("remote");
        if (remote != null && !remote.isBlank()) {
            Configuration.remote = remote;

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("selenoid:options", Map.of(
                    "enableVNC", true,
                    "enableVideo", true
            ));
            Configuration.browserCapabilities = capabilities;
        }
    }

    @AfterAll
    static void writeAllureEnvironment() {
        String resultsDir = System.getProperty(
                "allure.results.directory",
                "build/allure-results"
        );

        Properties props = new Properties();
        props.setProperty("env", "prod");
        props.setProperty("baseUrl", Configuration.baseUrl);
        props.setProperty("browser", Configuration.browser);
        props.setProperty("browserSize", Configuration.browserSize);
        props.setProperty(
                "remote",
                Configuration.remote == null ? "local" : Configuration.remote
        );
        props.setProperty(
                "DEMOQA_USER",
                System.getenv("DEMOQA_USER") == null ? "not set" : System.getenv("DEMOQA_USER")
        );

        Path out = Paths.get(resultsDir, "environment.properties");

        try {
            Files.createDirectories(out.getParent());
            try (var os = Files.newOutputStream(out)) {
                props.store(os, "Allure environment");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Allure environment", e);
        }
    }
}






