package bok.labexercise4;
// Command wrapper class  

import java.io.IOException;


public class JoinServerCommand extends ServerCommand {
	public JoinServerCommand(ConnectionPoint cp1, ConnectionPoint cp2) {
		super(cp1, cp2);
	}
	
	@Override
	public Object Execute(PhonebookServer server) throws IOException{
		// 
		return server.addConnectionPoint(this);
	}
}
