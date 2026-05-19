package lk.sliit.it25.bakeryweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class UploadResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(
                        fileLocation("data/uploads"),
                        fileLocation("src/main/resources/static/uploads"),
                        fileLocation("src/main/webapp/uploads")
                );
    }

    private String fileLocation(String relativePath) {
        Path absolutePath = Path.of(relativePath).toAbsolutePath().normalize();
        return absolutePath.toUri().toString();
    }
}
