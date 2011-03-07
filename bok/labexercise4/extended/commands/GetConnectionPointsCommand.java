package bok.labexercise4.extended.commands;

import java.io.IOException;

import bok.labexercise4.extended.AbstractServer;

public class GetConnectionPointsCommand extends Command<GetConnectionPointsCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object Execute(AbstractServer o) throws IOException {
		// TODO Auto-generated method stub
		Object result = o.getConnectionPoints();
		o.Send(result, this.getReturnTo(), false);
		this.setReturnTo(null);
		return result;
	}
}
