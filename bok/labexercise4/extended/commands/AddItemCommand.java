package bok.labexercise4.extended.commands;

import java.io.IOException;

import bok.labexercise4.extended.AbstractServer;
import bok.labexercise4.extended.IItem;


public class AddItemCommand extends ClientCommand<AddItemCommand> {
	private IItem<?> item;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AddItemCommand(IItem<?> item) {
		this.item = item;
	}

	@SuppressWarnings("unchecked")
	public Object Execute(AbstractServer server) throws IOException {
		Object result = null;
		if (this.getReceiver() != null) {
			// adds contact to phonebook
			if (this.getReceiver().equals(server.getIP())) 
			{ 
				// Add/remove/update
				try {
					result = server.getData().AddItem((IItem<IItem<?>>) item);
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
