package bok.labexercise4;
// Command wrapper class  

import java.io.IOException;
import java.net.InetSocketAddress;


public class JoinServerCommand extends ServerCommand {
	public JoinServerCommand(InetSocketAddress cp1, InetSocketAddress cp2) {
		super(cp1, cp2);
	}

	@Override
	public Object Execute(PhonebookServer server) throws IOException{ 
		return server.addConnectionPoint(this);
	}
}
