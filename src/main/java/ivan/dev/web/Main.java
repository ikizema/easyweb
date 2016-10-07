package ivan.dev.web;

import static org.kohsuke.args4j.ExampleMode.ALL;
import org.eclipse.jetty.server.Server;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ivan.dev.web.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class Main {
    @Option(name="-p",usage="port number", metaVar="PORT", required=false)
    private int port = 8080;

    @Argument
    private List<String> arguments = new ArrayList<String>();
    
    ApplicationContext context;
    
    Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {    	
    	new Main().parseArgs(args);
    }
    
    private void parseArgs(String[] args) throws Exception {
    	CmdLineParser parser = new CmdLineParser(this);
    	try {
    		parser.parseArgument(args);
        } catch( CmdLineException e ) {
            System.err.println(e.getMessage());
            System.err.println("java SampleMain [options...] arguments...");
            parser.printUsage(System.err);
            return;
        }

    	loadSpringContext();
    	startJettyService();
    }
    
    private void loadSpringContext() {
    	this.context = new ClassPathXmlApplicationContext(new String[] {"classpath:/ivan/dev/web/config/spring-context.xml"});
//    	this.context = new AnnotationConfigApplicationContext(AppConfig.class);
    }
    
    private void startJettyService() throws Exception {
    	Server server = new Server(this.port);
        server.setHandler(context.getBean(JettyService.class));
        server.start();
        server.join();
    }
}
