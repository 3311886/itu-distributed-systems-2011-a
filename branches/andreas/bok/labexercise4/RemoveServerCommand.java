package bok.labexercise4;
// Command wrapper class 

public class RemoveServerCommand extends ServerCommand {

	public RemoveServerCommand(ConnectionPoint cp1, ConnectionPoint cp2) {
		super(cp1, cp2);
	}

	@Override
	public Object Execute(IPhonebookServer server) {
		// TODO Auto-generated method stub
		return server.removeConnectionPoints(this);
	}
}