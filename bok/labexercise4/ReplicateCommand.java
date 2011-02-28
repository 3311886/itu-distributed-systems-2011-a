package bok.labexercise4;
// Abstract superclass for add and update command
import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class ReplicateCommand extends Command {
	
	private InetSocketAddress Receiver;
	
	@Override
	public abstract Object Execute(IPhonebook phonebook) throws IOException ;
	
	public InetSocketAddress getReceiver() {
		return Receiver;
	}
	
	public void setReceiver(InetSocketAddress value) {
		Receiver = value;
	}
	
}
