package bok.labexercise4.extended.commands;

import java.io.IOException;

import bok.labexercise4.extended.AbstractServer;
import bok.labexercise4.extended.IItem;

public class UpdateItemCommand extends ClientCommand<UpdateItemCommand> {
	Object key;
	IItem<?> item;
	/**
	 * 
	 */
	public UpdateItemCommand(Object key, IItem<?> item) {
		this.key = key;
		this.item = item;
		
	}
	private static final long serialVersionUID = 1L;

	
	@SuppressWarnings("unchecked")
	public Object Execute(AbstractServer server) throws IOException {
		Object result = null;
		if (this.getReceiver() != null) {
			// adds contact to phonebook
			if (this.getReceiver().equals(server.getIP())) 
			{ 
				// Add/remove/update
				try {
					result = server.getData().Update(key, (IItem<IItem<?>>) item);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				server.Send(result, this.getReturnTo());
				// Broadcast but clear returnto address
				this.setReturnTo(null);
				this.setSender(server.getIP());
				server.broadcast(this);
			} else {
				this.Execute(server);
			}
		} else {
			this.setReceiver(server.getIP());
			// Calls the method again
			this.Execute(server);
		}
		return result;

	} 
		
}
