package bok.labexercise4;

import java.net.InetSocketAddress;



public interface ICommand {
	public InetSocketAddress getSender();
	public void setSender(InetSocketAddress value);
	public void setReturnTo(InetSocketAddress value);
}
