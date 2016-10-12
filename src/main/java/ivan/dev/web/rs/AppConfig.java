package ivan.dev.web.rs;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import ivan.dev.web.rs.services.EntityBrowser;

@Configuration
public class AppConfig {
	
	private static Logger logger = LoggerFactory.getLogger(AppConfig.class);
	
	@Value("${port}")
	private int port;
	
	@Bean
	public Server server() {
        try {
            // Workaround for resources from JAR files
            Resource.setDefaultUseCaches( false );

            // Build the Swagger Bean.
            buildSwagger();

            // Holds handlers
            final HandlerList handlers = new HandlerList();

            // Handler for Swagger UI, static handler.
            handlers.addHandler( buildSwaggerUI() );

            // Handler for Entity Browser and Swagger
            handlers.addHandler( buildContext() );

            // Start server
            Server server = new Server(port);
            server.setHandler( handlers );
            server.start();
            server.join();
            return server;
        } catch ( Exception e ) {
        	logger.error( "There was an error starting up the Entity Browser", e );
        }
		return null;
	}
	
    private static void buildSwagger() {
        // This configures Swagger
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion( "1.0.0" );
        beanConfig.setResourcePackage( EntityBrowser.class.getPackage().getName() );
        beanConfig.setScan( true );
        beanConfig.setBasePath( "/" );
        beanConfig.setDescription( "Simple Swagger app with Jersey2 in a Jetty instance." );
        beanConfig.setTitle( "Entity Browser" );
    }

    private static ContextHandler buildContext() {
        ResourceConfig resourceConfig = new ResourceConfig();
        // Replace EntityBrowser with your resource class
        // io.swagger.jaxrs.listing loads up Swagger resources
        resourceConfig.packages( EntityBrowser.class.getPackage().getName(), ApiListingResource.class.getPackage().getName() );
        ServletContainer servletContainer = new ServletContainer( resourceConfig );
        ServletHolder entityBrowser = new ServletHolder( servletContainer );
        ServletContextHandler entityBrowserContext = new ServletContextHandler( ServletContextHandler.SESSIONS );
        entityBrowserContext.setContextPath( "/" );
        entityBrowserContext.addServlet( entityBrowser, "/*" );
        return entityBrowserContext;
    }

    // This starts the Swagger UI at http://localhost:port/docs
    private static ContextHandler buildSwaggerUI() throws Exception {
        final ResourceHandler swaggerUIResourceHandler = new ResourceHandler();
        swaggerUIResourceHandler.setResourceBase( AppConfig.class.getClassLoader().getResource( "swaggerui" ).toURI().toString() );
        final ContextHandler swaggerUIContext = new ContextHandler();
        swaggerUIContext.setContextPath( "/docs/" );
        swaggerUIContext.setHandler( swaggerUIResourceHandler );
        return swaggerUIContext;
    }

}
