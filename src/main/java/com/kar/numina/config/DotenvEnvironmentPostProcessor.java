package com.kar.numina.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String profile = environment.getProperty("spring.profiles.active", "development");
        String envFile = ".env." + profile;
        Dotenv dotenv = Dotenv.configure().filename(envFile).ignoreIfMissing().load();
        Map<String, Object> dotenvMap = new HashMap<>();
        dotenv.entries().forEach(entry -> dotenvMap.put(entry.getKey(), entry.getValue()));
        environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", dotenvMap));
    }
}
