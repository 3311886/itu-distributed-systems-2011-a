package bok.labexercise4.extended.commands;

import java.io.IOException;

import bok.labexercise4.extended.AbstractServer;

public class RemoveItemCommand extends Command<RemoveItemCommand> {
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
			// Add/remove/update
			try {
				result = o.getData().Remove(key);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (this.getReceiver().equals(o.getIP())) 
			{ 
				
				o.Send(result, this.getReturnTo());
				// Broadcast but clear returnto address
				this.setReturnTo(null);
				this.setSender(o.getIP());
				o.broadcast(this);
			} 
			this.setReturnTo(null);
		} else {
			this.setReceiver(o.getIP());
			// Calls the method again
			this.Execute(o);
		}
		return result;
	}	
}
