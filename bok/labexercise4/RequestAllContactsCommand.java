package bok.labexercise4;

import java.io.IOException;
import java.util.List;

public class RequestAllContactsCommand extends ServerCommand {
	List<Contact> contacts;
	public RequestAllContactsCommand(ConnectionPoint cp1, ConnectionPoint cp2) {
		super(cp1, cp2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object Execute(PhonebookServer server) throws IOException {
		//return server.ReqestAllContacts(this);
		return null;
	}
}
