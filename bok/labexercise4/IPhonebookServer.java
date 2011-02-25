package bok.labexercise4;

import java.io.IOException;


public interface IPhonebookServer {

	public abstract ServerResult addConnectionPoint(ServerCommand command) throws IOException;

	public abstract Object removeConnectionPoints(ServerCommand command);

}