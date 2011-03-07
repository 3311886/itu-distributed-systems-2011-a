package bok.labexercise4.extended.commands;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bok.labexercise4.extended.AbstractServer;

public class SynchronizeCommand extends Command<SynchronizeCommand> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Set<InetSocketAddress> cpoints;
	List<?> data;
	bok.labexercise4.extended.commands.SynchronizeStatus status;

	public Object Execute(AbstractServer o) throws IOException {
		this.setReturnTo(null);
		Set<InetSocketAddress> cpointsToSend;
		SynchronizeStatus result = SynchronizeStatus.Default;
		o.Trace("Sync status:" + this.status);
		
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
			this.data = o.getData().GetAllTyped();
			this.status = SynchronizeStatus.SendFromTarget;
			result = this.status;
			// ExecuteAndSend(command);
			o.Send(this, this.getReceiver(), true);

			return result;
		case SendFromTarget:
			// Joining server receives sync message from server
			// Returns connectionpoints and contacts 
			Set<InetSocketAddress> cpointsReceived = this.cpoints;
			o.getData().Synchronize(this.data);
			cpointsToSend = new HashSet<InetSocketAddress>();
			cpointsToSend.addAll(o.getConnectionPoints());
			this.cpoints = cpointsToSend;

			o.getConnectionPoints().addAll(cpointsReceived);
			this.data= o.getData().GetAllTyped();
			this.status = SynchronizeStatus.SendFromJoining;
			result = this.status;
			// joining is receiver // 
			this.setReceiver(this.getSender());
			this.setSender(o.getIP());
			o.Send(this, this.getReceiver(), true);
			break;
		case SendFromJoining:
			// Sender is joining : This is receiver
			this.status = SynchronizeStatus.Broadcast;
			this.setReceiver(this.getSender());
			this.setSender(o.getIP());
			o.broadcast(this);
			// Server receives sync response from joining server
			o.getConnectionPoints().addAll(this.cpoints);
			o.getData().Synchronize(this.data);
			// Broadcast to servers
			result =  SynchronizeStatus.Synchronized;
			return result;
		case Broadcast:
			o.getConnectionPoints().addAll(this.cpoints);
			o.getData().Synchronize(this.data);
			result = SynchronizeStatus.Synchronized;
			break;
		default :
			break;
		}
		return result;
	}

} 
