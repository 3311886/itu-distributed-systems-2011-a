package bok.labexercise4;
// Abstract class for server commands 

import java.io.IOException;
import java.io.Serializable;

public abstract class ServerCommand implements ICommand, Serializable {
	private ConnectionPoint cp1;
	private ConnectionPoint cp2;
	
	public ServerCommand (ConnectionPoint cp1, ConnectionPoint cp2) {
		this.cp1 = cp1;
		this.cp2 = cp2;
	}
	
	public ConnectionPoint getJoiningServer() {
		return this.cp1;
	}
	
	public ConnectionPoint getTargetServer() {
		return this.cp2;
	}
	
	abstract public Object Execute(PhonebookServer server) throws IOException;
}
