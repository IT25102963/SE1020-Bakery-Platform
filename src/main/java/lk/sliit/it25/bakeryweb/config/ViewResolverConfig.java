package lk.sliit.it25.bakeryweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class ViewResolverConfig {

    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setViewNames(
                "admin/*",
                "catalog/*",
                "customer/*",
                "delivery/*",
                "customrequests/*",
                "orders/*"
        );
        resolver.setOrder(1);
        return resolver;
    }
}
