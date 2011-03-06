package bok.labexercise4.extended.commands;

import java.io.IOException;
import java.util.HashMap;

import bok.labexercise4.extended.AbstractServer;
import bok.labexercise4.extended.DataItemFactory;
import bok.labexercise4.extended.IItem;

public class UpdateItemCommand extends Command<UpdateItemCommand> {
	Object key;
	HashMap<String,Object> values;
	/**
	 * 
	 */
	public UpdateItemCommand(Object key, HashMap<String, Object> values) {
		this.key = key;
		this.values = values;
		
	}
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public Object Execute(AbstractServer server) throws IOException {
		Object result = null;
		if (this.getReceiver() != null) {
			// Add/remove/update
			try {
				IItem objOld = server.getData().Get(key);
				
				if (objOld != null ) {
					 IItem objNew =  DataItemFactory.build(objOld.getClass());
					 DataItemFactory.Create(objNew, values);
					 
					 server.getData().Update(objOld, objNew);
					 
				}
				
				result = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
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
