package bok.labexercise4;
// Command wrapper class

import java.io.IOException;
import java.io.Serializable;

public class AddCommand extends Command implements Serializable {
	private Contact Contact;

	public AddCommand (Contact contact) {
		Contact = contact;
	}

	@Override
	public Object Execute (IPhonebook phonebook) throws IOException {
		return phonebook.AddContact(Contact);
	}

}
