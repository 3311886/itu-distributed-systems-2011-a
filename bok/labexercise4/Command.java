package bok.labexercise4;
// Base-class for the wrapping of the IPhonebook methods
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;

public abstract class Command implements Serializable, ICommand  {
	private InetSocketAddress Sender;
	private InetSocketAddress ReturnTo;


	abstract public Object Execute(IPhonebook phonebook) throws IOException;

	public void setSender(InetSocketAddress value) {
		this.Sender = value;
	}

	public InetSocketAddress getSender() {
		return Sender;
	}

	public void setReturnTo(InetSocketAddress returnTo) {
		ReturnTo = returnTo;
	}

	public InetSocketAddress getReturnTo() {
		return ReturnTo;
	}

}
