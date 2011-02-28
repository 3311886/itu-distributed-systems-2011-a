package bok.labexercise4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Set;


public interface IPhonebookServer {

	public abstract ServerResult addConnectionPoint(ServerCommand command) throws IOException;
	public abstract Object removeConnectionPoint(ServerCommand command);
	public abstract Set<InetSocketAddress> getConnectionPoints(GetConnectionPointsCommand command) throws IOException;

}