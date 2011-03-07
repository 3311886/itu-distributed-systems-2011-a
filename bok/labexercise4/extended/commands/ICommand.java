package bok.labexercise4.extended.commands;

import java.io.IOException;
import java.net.InetSocketAddress;

import bok.labexercise4.extended.AbstractServer;
import bok.labexercise4.extended.VectorClock;


public interface ICommand<T> {
	
	Object Execute(AbstractServer o) throws IOException;
	
	public InetSocketAddress getReceiver() ;

	public void setReceiver(InetSocketAddress receiver) ;

	public InetSocketAddress getReturnTo();

	public void setReturnTo(InetSocketAddress returnTo) ;

	public InetSocketAddress getSender() ;
	
	public VectorClock getVectorClock();
	
	public void setVectorClock(VectorClock vc) ;
	
	public void setSender(InetSocketAddress sender);



}
