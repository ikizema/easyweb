package ivan.dev.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ivan.dev.web.JettyService;

@Configuration
public class AppConfig {
	
	@Bean
    public JettyService jettyService() {
        return new JettyService();
    }
}
