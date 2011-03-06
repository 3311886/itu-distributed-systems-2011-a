package bok.labexercise4.extended.commands;

import java.io.IOException;

import bok.labexercise4.extended.AbstractServer;
import bok.labexercise4.extended.DataItemFactory;

public class GetClassesCommand extends Command<GetClassesCommand> {
		
	private static final long serialVersionUID = 1L;

	public Object Execute(AbstractServer o) throws IOException {
		
		o.Send(DataItemFactory.getClasses(), this.getReturnTo());
		this.setReturnTo(null);
		return null;
		
	}

}
