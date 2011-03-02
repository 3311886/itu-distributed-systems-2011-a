package bok.labexercise4.extended.commands;

import java.io.IOException;
import java.net.InetSocketAddress;

import bok.labexercise4.extended.AbstractServer;
import bok.labexercise4.extended.VectorClock;


public interface ICommand<T> {
	VectorClock getVectorClock();
	void setVectorClock(VectorClock vc);
	Object Execute(AbstractServer o) throws IOException;
	InetSocketAddress getSender();
	InetSocketAddress getReturnTo();
	void setReturnTo(InetSocketAddress value);
		


}
