package bok.labexercise4;
// Abstract class for server commands 

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;

public abstract class ServerCommand implements ICommand, Serializable {
	private InetSocketAddress cp1;
	private InetSocketAddress cp2;
	
	public ServerCommand (InetSocketAddress cp1, InetSocketAddress cp2) {
		this.cp1 = cp1;
		this.cp2 = cp2;
	}
	
	public InetSocketAddress getJoiningServer() {
		return this.cp1;
	}
	
	public InetSocketAddress getTargetServer() {
		return this.cp2;
	}
	
	/***
	 * 
	 * @param server Untill 
	 * @return
	 * @throws IOException
	 */
	abstract public Object Execute(PhonebookServer server) throws IOException;
}
