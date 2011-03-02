package bok.labexercise4.extended.commands;

import java.io.IOException;

import bok.labexercise4.extended.AbstractServer;

public class GetAllCommand extends ClientCommand<GetAllCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object Execute(AbstractServer o) {
		Object result = null; 
		try {
			result = o.getData().GetAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
