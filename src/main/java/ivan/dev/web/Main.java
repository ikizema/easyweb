package ivan.dev.web;

import org.eclipse.jetty.server.Server;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ivan.dev.web.config.model.RuntimeConfig;

public class Main {   
    ApplicationContext context;
    RuntimeConfig runtimeConfig;
    
    Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception { 	
    	new Main().run(args);
    }
    
    private void run(String[] args) throws Exception {
    	loadSpringContext();
    	
    	CmdLineParser parser = new CmdLineParser(this.runtimeConfig);
    	try {
    		parser.parseArgument(args);
        } catch( CmdLineException e ) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            return;
        }

    	startJettyService();
    }
    
    private void loadSpringContext() {
    	this.context = new ClassPathXmlApplicationContext(new String[] {"classpath:/ivan/dev/web/config/spring-context.xml"});
//    	this.context = new AnnotationConfigApplicationContext(AppConfig.class);
    	
    	this.runtimeConfig=context.getBean(RuntimeConfig.class);
    }
    
    private void startJettyService() throws Exception {
    	Server server = new Server(runtimeConfig.getPort());
        server.setHandler(context.getBean(JettyService.class));
        server.start();
        server.join();
    }
}
