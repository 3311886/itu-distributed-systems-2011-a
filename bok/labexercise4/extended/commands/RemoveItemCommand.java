package bok.labexercise4.extended.commands;

import java.io.IOException;

import bok.labexercise4.extended.AbstractServer;

public class RemoveItemCommand extends ClientCommand<RemoveItemCommand> {
	Object key;
	/**
	 * 
	 */
	public RemoveItemCommand(Object key) {
		this.key = key;
	}
	private static final long serialVersionUID = 1L;

	public Object Execute(AbstractServer o) throws IOException {
		Object result = null;
		if (this.getReceiver() != null) {
			// adds contact to phonebook
			if (this.getReceiver().equals(o.getIP())) 
			{ 
				// Add/remove/update
				try {
					result = o.getData().Remove(key);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				o.Send(result, this.getReturnTo());
				// Broadcast but clear returnto address
				this.setReturnTo(null);
				this.setSender(o.getIP());
				o.broadcast(this);
			} else {
				this.Execute(o);
			}
		} else {
			this.setReceiver(o.getIP());
			// Calls the method again
			this.Execute(o);
		}
		return result;
	}	
}
