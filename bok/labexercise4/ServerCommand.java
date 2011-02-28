package bok.labexercise4;
// Abstract class for server commands 

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;

public abstract class ServerCommand implements ICommand, Serializable {
	private InetSocketAddress Sender;
	private InetSocketAddress Receiver;
	private InetSocketAddress ReturnTo;
	private VectorClock vc;
	
	public ServerCommand (InetSocketAddress cp1, InetSocketAddress cp2) {
		this.Sender = cp1;
		this.Receiver = cp2;
	}
	
	public InetSocketAddress getSender() {
		return this.Sender;
	}
	
	public InetSocketAddress getReceiver() {
		return this.Receiver;
	}
	
	public void setSender(InetSocketAddress value) {
		this.Sender = value;
	}
	
	public void setReceiver(InetSocketAddress  value) {
		this.Receiver = value;
	}
	
	
	/***
	 * 
	 * @param server Untill 
	 * @return
	 * @throws IOException
	 */
	abstract public Object Execute(PhonebookServer server) throws IOException;

	public void setReturnTo(InetSocketAddress returnTo) {
		ReturnTo = returnTo;
	}

	public InetSocketAddress getReturnTo() {
		return ReturnTo;
	}
}
