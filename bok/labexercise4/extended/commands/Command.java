package bok.labexercise4.extended.commands;
// Base-class for the wrapping of the IPhonebook methods
import java.io.Serializable;
import java.net.InetSocketAddress;

import bok.labexercise4.extended.VectorClock;

public abstract class Command<T> implements Serializable, ICommand<T>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InetSocketAddress Sender;
	private InetSocketAddress Receiver; 
	private InetSocketAddress ReturnTo;
	private VectorClock vc;

	public void setSender(InetSocketAddress value) {
		this.Sender = value;
	}
	public Command() {
		vc = new VectorClock();
	}

	public InetSocketAddress getReceiver() {
		return Receiver;
	}

	public void setReceiver(InetSocketAddress receiver) {
		Receiver = receiver;
	}

	public InetSocketAddress getReturnTo() {
		return ReturnTo;
	}

	public void setReturnTo(InetSocketAddress returnTo) {
		ReturnTo = returnTo;
	}

	public InetSocketAddress getSender() {
		return Sender;
	}	
	
	public VectorClock getVectorClock() {
		return this.vc;
	}
	
	public void setVectorClock(VectorClock vc) {
		this.vc = vc;
	}
}
