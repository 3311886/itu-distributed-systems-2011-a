package bok.labexercise4;
// Abstract superclass for add and update command
import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class ReplicateCommand extends Command {
	public InetSocketAddress ForwardTo;
	
	@Override
	public abstract Object Execute(IPhonebook phonebook) throws IOException ;
}
