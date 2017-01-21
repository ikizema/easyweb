package ivan.dev.web.rs;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

@Configuration
@Profile("rest")
public class RestConfig {
	
	private static Logger logger = LoggerFactory.getLogger(RestConfig.class);
	
	@Value("${rest.service.base.path}")
	private String restservicebasepath;
	
	@Value("${rest.service.relative.swagger.docs}")
	private String swaggerpath;

	@Inject
	private HandlerList handlerList;
	
	@PostConstruct
	private void initRest() throws Exception {
        // Build the Swagger Bean.
        buildSwagger();

        // Handler for Swagger UI, static handler.
        handlerList.addHandler( buildSwaggerUI() );

        // Handler for Entity Browser and Swagger
        handlerList.addHandler( buildContext() );
        
        logger.debug("Swager loaded");
	}
	
    private void buildSwagger() {
        // This configures Swagger
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion( "1.0.0" );
        beanConfig.setResourcePackage( RestConfig.class.getPackage().getName().concat(".services") );
        beanConfig.setScan( true );
        beanConfig.setBasePath( "/" + restservicebasepath );
        beanConfig.setDescription( "Simple Swagger app with Jersey2 in a Jetty instance." );
        beanConfig.setTitle( "Services Browser" );
    }

    private ContextHandler buildContext() {
        ResourceConfig resourceConfig = new ResourceConfig();
        // Replace EntityBrowser with your resource class
        // io.swagger.jaxrs.listing loads up Swagger resources
        resourceConfig.packages( RestConfig.class.getPackage().getName().concat(".services"), ApiListingResource.class.getPackage().getName() );
        ServletContainer servletContainer = new ServletContainer( resourceConfig );
        ServletHolder entityBrowser = new ServletHolder( servletContainer );
        ServletContextHandler entityBrowserContext = new ServletContextHandler( ServletContextHandler.SESSIONS );
        entityBrowserContext.setContextPath( "/" + restservicebasepath + "/" );
        entityBrowserContext.addServlet( entityBrowser, "/*" );
        return entityBrowserContext;
    }

    // This starts the Swagger UI at http://localhost:port/docs
    private ContextHandler buildSwaggerUI() throws Exception {
        final ResourceHandler swaggerUIResourceHandler = new ResourceHandler();
        swaggerUIResourceHandler.setResourceBase( RestConfig.class.getClassLoader().getResource( "swaggerui" ).toURI().toString() );
        final ContextHandler swaggerUIContext = new ContextHandler();
        swaggerUIContext.setContextPath( "/" + restservicebasepath + "/" + swaggerpath + "/" );
        swaggerUIContext.setHandler( swaggerUIResourceHandler );
        return swaggerUIContext;
    }

}
