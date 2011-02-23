package bok.labexercise4;


public interface IPhonebookServer {

	public abstract ServerResult addConnectionPoint(ServerCommand command);

	public abstract ServerResult removeConnectionPoints(ServerCommand command);

}