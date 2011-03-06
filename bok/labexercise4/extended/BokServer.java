package bok.labexercise4.extended;

import java.io.IOException;

import bok.labexercise4.ServerResult;
import bok.labexercise4.extended.commands.ICommand;

public class BokServer extends AbstractServer {
	ServerData<IItem<?>> data;

	public BokServer() throws IOException {
		super();
		data = new ServerData<IItem<?>>();	
	}

	@Override
	public void ExecuteAndSend(Object command) throws IOException {
		if (command instanceof ICommand<?>) {
		ExecuteAndSend((ICommand<?>) command);
		} else {
			Trace("ExecuteAndSend" + command.toString());
		}
	}

	public void ExecuteAndSend(ServerResult result) {
		try {
			Trace("Executing SERVERRESULT: " + result);
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (result) {
		case RemoveServerInitiatedFromServer : 
			getConnectionPoints().removeAll(getConnectionPoints());
		}
	}

	public <T> void ExecuteAndSend(ICommand<T> command) throws IOException  {
		this.setVectorClock(VectorClock.max(getVectorClock(), command.getVectorClock()));
		Trace(String.format("OnReceiveCommand: %s", command.getClass().getName()));
		String sender = command.getReturnTo() != null ? 
				command.getReturnTo().toString() : command.getSender().toString();

				try {
					command.Execute(this);
				} catch (IOException e) {
				}

				Trace( String.format("IP: %s ", getIP()));
				Trace(String.format("Receiving from: %s ", sender));
				Trace(String.format("Command: %s", command.getClass().getName()));
				Trace("VectorClock: "+ getVectorClock().toString()+ "\n");

				Send(command, command.getReturnTo());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServerData<IItem<?>> getData() {
		return (ServerData<IItem<?>>) this.data;
	}
}