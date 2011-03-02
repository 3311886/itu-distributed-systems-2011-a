package bok.labexercise4.extended;

import java.io.IOException;

import bok.labexercise4.ServerResult;
import bok.labexercise4.extended.commands.ICommand;

public class BokServer extends AbstractServer {
	IDataCollection<?> data;
	
	public BokServer() throws IOException {
		super();
		data = new ServerData<IItem<?>>();	
	}


	
	@Override
	public void ExecuteAndSend(Object command) throws IOException {
		
		ExecuteAndSend((ICommand<?>) command);
		
	}
	
	void ExecuteAndSend(ServerResult result) {
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

	
	public <T> void ExecuteAndSend(ICommand<T> command)  {	
		try {
			command.Execute(this);
		} catch (IOException e) {
		}
		
		try {
			Send(command, command.getReturnTo());
			//Send(result, returnTo);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServerData<IItem<?>> getData() {
		return (ServerData<IItem<?>>) this.data;
	}

}
