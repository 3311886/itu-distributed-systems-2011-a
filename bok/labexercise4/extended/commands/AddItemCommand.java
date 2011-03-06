package bok.labexercise4.extended.commands;

import java.io.IOException;
import java.util.HashMap;

import bok.labexercise4.extended.AbstractServer;
import bok.labexercise4.extended.DataItemFactory;
import bok.labexercise4.extended.IItem;


public class AddItemCommand extends Command<AddItemCommand> {
	private Class<?> type;
	private HashMap<String, Object> values;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddItemCommand(Class<?> type, HashMap<String, Object>values) {

		this.type = type;
		this.values= values;
	}

	@SuppressWarnings("unchecked")
	public Object Execute(AbstractServer server) throws IOException {		
		Object result = null;

		if (this.getReceiver() != null) 
		{
			// Add/remove/update
			try {

				//System.out.println("Adding to server: " +   type.getClass().getCanonicalName());
				IItem item = (IItem) DataItemFactory.build(type);

				try {
					item  = (IItem<IItem<?>>) DataItemFactory.Create(item, values);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				result = server.getData().AddItem(item);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// adds contact to phonebook
			if (this.getReceiver().equals(server.getIP())) 
			{ 

				server.Send(result, this.getReturnTo());
				// Broadcast but clear returnto address
				this.setReturnTo(null);
				this.setSender(server.getIP());
				server.broadcast(this);
			} 
			this.setReturnTo(null);
		} else {
			this.setReceiver(server.getIP());
			// Calls the method again
			this.Execute(server);
		}
		return result;
	}	



}
