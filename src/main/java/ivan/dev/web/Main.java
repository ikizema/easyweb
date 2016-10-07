package ivan.dev.web;

import org.eclipse.jetty.server.Server;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ivan.dev.web.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class Main {
    @Option(name="-p",usage="port number", metaVar="PORT")
    private int port = 8080;

    @Argument
    private List<String> arguments = new ArrayList<String>();
    
    ApplicationContext context;

    public static void main(String[] args) throws Exception {    	
    	new Main().parseArgs(args);
    }
    
    private void parseArgs(String[] args) throws Exception {
    	CmdLineParser parser = new CmdLineParser(this);
    	parser.parseArgument(args);
    	
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
