package ivan.dev.web.config.model;

import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class RuntimeConfig {
    @Option(name="-p",usage="port number", metaVar="PORT", required=false)
    private int port = 8080;

    @Argument
    private List<String> arguments = new ArrayList<String>();

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}
}
