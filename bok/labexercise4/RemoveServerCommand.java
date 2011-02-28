package bok.labexercise4;
//Command wrapper class

import java.net.InetSocketAddress;

public class RemoveServerCommand extends ServerCommand {
	public RemoveServerCommand(InetSocketAddress cp1, InetSocketAddress cp2) {
		super(cp1, cp2);		
	}

	@Override
	public Object Execute(PhonebookServer server) {
		return server.removeConnectionPoints(this);
	}
}
