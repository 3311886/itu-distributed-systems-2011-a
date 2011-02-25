package bok.labexercise4;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;

public class SynchronizeCommand extends ServerCommand implements Serializable {
	Set<InetSocketAddress> cpoints;
	List<Contact> contacts;
	SynchronizeStatus status;  
	
	public SynchronizeCommand(InetSocketAddress cp1, InetSocketAddress cp2) {
		
		super(cp1, cp2);
		status = SynchronizeStatus.Created;
	}

	@Override
	public Object Execute(PhonebookServer server) throws IOException {
		return server.synchronize(this);
	} 
}