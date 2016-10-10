package ivan.dev.web;

import org.eclipse.jetty.server.Server;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import ivan.dev.web.config.AppConfig;
import ivan.dev.web.config.model.RuntimeConfig;

public class Main {
	
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        // Parse Command Line Arguments
        RuntimeConfig runtimeConfig = new RuntimeConfig();
		CmdLineParser parser = new CmdLineParser(runtimeConfig);
		try {
			parser.parseArgument(args);
	    } catch( CmdLineException e ) {
	        System.err.println(e.getMessage());
	        parser.printUsage(System.err);
	        return;
	    }

		// Pass CLI arguments to Spring Context
		ConfigurableEnvironment env = context.getEnvironment();
		MutablePropertySources propertySources = env.getPropertySources();
		propertySources.addFirst(new MapPropertySource("cli", runtimeConfig.getProperties()));
		
		String envProperties = "classpath:/ivan/dev/web/config/env/env-${env}.properties";
		envProperties = envProperties.replace("${env}", env.getProperty("env"));
		ResourcePropertySource envPropertiesRessources = new ResourcePropertySource("envProperties", envProperties);
		propertySources.addFirst(envPropertiesRessources);
		
        logger.info("Port : {}", runtimeConfig.getPort());
        logger.info("Env : {}", runtimeConfig.getEnv());
        logger.info("Property project : {}", env.getProperty("project"));
        logger.info("Property env : {}", env.getProperty("env"));
        logger.info("Property port : {}", env.getProperty("port"));
        logger.info("Property topic : {}", env.getProperty("topic"));
        logger.info("Property server : {}", env.getProperty("server"));

    	Server server = new Server(Integer.parseInt(env.getProperty("port")));
    	server.setHandler(context.getBean(JettyService.class));
    	server.start();
    	server.join();
    }
}
