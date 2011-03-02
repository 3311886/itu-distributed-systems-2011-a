package bok.labexercise4.extended.commands;

import java.io.IOException;

import bok.labexercise4.extended.AbstractServer;

public class GetConnectionPointsCommand extends ServerCommand<GetConnectionPointsCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object Execute(AbstractServer o) throws IOException {
		// TODO Auto-generated method stub
		return o.getConnectionPoints();
	}
}
