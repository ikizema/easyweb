package ivan.dev.web.server;

import javax.inject.Inject;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "ivan.dev.web.rs", "ivan.dev.web.ui" })
public class AppConfig {
	
	private static Logger logger = LoggerFactory.getLogger(AppConfig.class);
	
	@Value("${port}")
	private int port;
	
	@Inject
	private HandlerList handlerList;
	
	@Bean
	public HandlerList handlerList() {
		return new HandlerList();
	}
	
	@Bean
	public Server server() {
        try {
            Resource.setDefaultUseCaches( false );

            // Start server
            Server server = new Server(port);
            server.setHandler( handlerList );
            server.start();
            server.join();
            return server;
        } catch ( Exception e ) {
        	logger.error( "There was an error starting up the Entity Browser", e );
        }
		return null;
	}

}
