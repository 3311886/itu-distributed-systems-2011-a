package bok.labexercise4.extended.commands;

import java.io.IOException;
import java.net.InetSocketAddress;

import bok.labexercise4.ServerResult;
import bok.labexercise4.extended.AbstractServer;

public class JoinServerCommand extends ServerCommand<JoinServerCommand> {
	
	public JoinServerCommand(InetSocketAddress sender, InetSocketAddress receiver) {
		super.setReceiver(receiver);
		super.setSender(sender);
	}
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;


	public Object Execute(AbstractServer o) {
		Object result = null;
		if (o.getIP().equals(this.getSender())) {
			// Receives from client a command to join another server
			this.setReturnTo(null);
			try {
				o.Send(this, this.getReceiver());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Return to client 
			result =  ServerResult.JoiningServer;
		} else 	
			if (o.getIP().equals(this.getReceiver())) {
				try {
					
					// Broadcast join command
					result = o.broadcast(this);
					//Synchronize from target server to joining server
					SynchronizeCommand syncCommand =  
						new SynchronizeCommand();
					syncCommand.setSender(o.getIP());
					syncCommand.setReceiver(this.getSender());
								
					syncCommand.status = SynchronizeStatus.Created;
					syncCommand.Execute(o);
					// Add the joining server to target server
					o.getConnectionPoints().add(this.getSender());
					return result;
				} catch (IOException e) {
					e.printStackTrace();
					return ServerResult.UnknownError;
				}		
			} else {
				// A broadcast server has received a joincommand 
				o.getConnectionPoints().add(this.getSender());
				result =  ServerResult.Added;
			}
		return result;
	}
}
