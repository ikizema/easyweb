package ivan.dev.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.ClassPathResource;

import ivan.dev.web.JettyService;
import ivan.dev.web.config.model.RuntimeConfig;

@Configuration
@PropertySource("classpath:/ivan/dev/web/config/env/env-global.properties")
public class AppConfig {
	@Bean
    public JettyService jettyService() {
        return new JettyService();
    }
	
//	@Bean
//    public RuntimeConfig runtimeConfig() {
//        return new RuntimeConfig();
//    }
}
