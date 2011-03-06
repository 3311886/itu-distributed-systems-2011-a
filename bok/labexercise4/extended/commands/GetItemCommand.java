package bok.labexercise4.extended.commands;

import java.io.IOException;

import bok.labexercise4.extended.AbstractServer;

public class GetItemCommand extends Command<GetItemCommand> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Object key;
	
	public GetItemCommand(Object key) {
		this.key = key;
	}

	public Object Execute(AbstractServer server) throws IOException {
		Object result = null;
		 
		try {
			result = server.getData().Get(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		server.Send(result, this.getReturnTo());
		this.setReturnTo(null);
		return result;
	}
}
