package ivan.dev.web;

import org.eclipse.jetty.server.Server;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

public class Main {
    @Option(name="-p",usage="port number", metaVar="PORT")
    private int port = 8080;

    @Argument
    private List<String> arguments = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
    	new Main().parseArgs(args);
    }
    
    private void parseArgs(String[] args) throws Exception {
    	CmdLineParser parser = new CmdLineParser(this);
    	parser.parseArgument(args);
    	
    	startJettyService();
    }
    
    private void startJettyService() throws Exception {
    	Server server = new Server(this.port);
        server.setHandler(new JettyService());
        server.start();
        server.join();
    }
}
