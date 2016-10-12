package ivan.dev.web.config.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class RuntimeConfig {
	
	public static enum Env {DEV, QA, UAT, PROD}
	
    @Option(name="-e", aliases = { "--env" }, usage="Environnement <Opt>", metaVar="[DEV, QA, UAT, PROD]", required=false)
    private Env env = Env.DEV;
	
    @Option(name="-p",aliases = { "--port" }, usage="Port number <Opt>", metaVar="PORT", required=false)
    private int port = 8080;
    
    @Option(name="-h",aliases = { "--help" }, usage="Print help message.", hidden=true)
    private boolean help = false;

    @Argument
    private List<String> arguments = new ArrayList<String>();

	public int getPort() {
		return port;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public Env getEnv() {
		return env;
	}
	
	public boolean isHelp() {
		return help;
	}

	public Map<String, Object> getProperties() {
		Map<String,Object> properties = new HashMap<String, Object>();
		properties.put("env", env);
		properties.put("port", port);
		return properties;
	}
}
