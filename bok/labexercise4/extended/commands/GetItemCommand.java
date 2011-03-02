package bok.labexercise4.extended.commands;

import java.io.IOException;

import bok.labexercise4.extended.AbstractServer;

public class GetItemCommand extends ClientCommand<GetItemCommand> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Object key;
	
	public GetItemCommand(Object key) {
		this.key = key;
	}

	public Object Execute(AbstractServer server) {
		Object result = null;
		try {
			result = server.getData().Get(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
