package bok.labexercise4.extended.commands;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bok.labexercise4.extended.AbstractServer;

public class SynchronizeCommand extends ServerCommand<SynchronizeCommand> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Set<InetSocketAddress> cpoints;
	List<?> data;
	bok.labexercise4.extended.commands.SynchronizeStatus status;
	
	public Object Execute(AbstractServer o) throws IOException {
		
		Set<InetSocketAddress> cpointsToSend;
		SynchronizeStatus result = SynchronizeStatus.Default;
		switch (this.status) {
		case Created:
			// Target server creates a syncmessage and sends its connectionpoints and contacts to 
			// the joining server 
			// broadcasting of joinserver has already been done in addconnectionpoint
			cpointsToSend = new HashSet<InetSocketAddress>();
			cpointsToSend.addAll(o.getConnectionPoints());
			this.cpoints = cpointsToSend;

			// Add target server
			this.cpoints.add(this.getSender());
			this.data = o.getData().GetAll();
			this.status = SynchronizeStatus.SendFromTarget;
			result = this.status;
			// ExecuteAndSend(command);
			o.Send(this, this.getReceiver());
			break;
		case SendFromTarget:
			// Joining server receives sync message from server
			// Returns connectionpoints and contacts 
			Set<InetSocketAddress> cpointsReceived = this.cpoints;
			o.getData().Synchronize(this.data);
			cpointsToSend = new HashSet<InetSocketAddress>();
			cpointsToSend.addAll(o.getConnectionPoints());
			this.cpoints = cpointsToSend;

			o.getConnectionPoints().addAll(cpointsReceived);
			this.data= o.getData().GetAll();
			this.status= SynchronizeStatus.SendFromJoining;

			//ExecuteAndSend(command);
			InetSocketAddress tmp = this.getSender();
			this.setSender(this.getReceiver());
			this.setReceiver(tmp);
			result = this.status;
			o.Send(this, tmp);
			break;
		case SendFromJoining:
			// Server receives sync response from joining server
			o.getConnectionPoints().addAll(this.cpoints);
			o.getData().Synchronize(this.data);
			// Broadcast to servers
			this.status = SynchronizeStatus.BroadCast;
			o.broadcast(this);
			result =  SynchronizeStatus.Synchronized;
		case BroadCast:
			o.getConnectionPoints().addAll(this.cpoints);
			o.getData().Synchronize(this.data);
			result = SynchronizeStatus.Synchronized;
		default : 
		}
		return result;
	}

}
