package bok.labexercise4;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

public class SynchronizeCommand extends ServerCommand implements Serializable {
	Set<ConnectionPoint> cpoints;
	SynchronizeStatus status;  
	
	public SynchronizeCommand(ConnectionPoint cp1, ConnectionPoint cp2) {
		
		super(cp1, cp2);
		status = SynchronizeStatus.Created;
	}

	@Override
	public Object Execute(PhonebookServer server) throws IOException {
		return server.synchronize(this);
	} 
}