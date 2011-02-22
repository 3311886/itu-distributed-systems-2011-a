package bok.labexercise4;

import java.io.IOException;


public class JoinServerCommand extends ServerCommand {
	public JoinServerCommand(ConnectionPoint cp1, ConnectionPoint cp2) {
		super(cp1, cp2);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object Execute(ReplicatedPhoneBookServer server) throws IOException{
		return server.addConnectionPoint(this);
	}
}
