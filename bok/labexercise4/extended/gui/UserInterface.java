package bok.labexercise4.extended.gui;
// Simple-minded console-based user interface
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import bok.labexercise4.extended.IItem;

public class UserInterface {
	private ClientInterface ui;
	private RemoteServerUI remoteServer;
	private List<Class<?>> classes;

	public UserInterface (ClientInterface ui, RemoteServerUI server) throws IOException {
		this.ui = ui;
		remoteServer = server;
		classes = remoteServer.getClasses();
	}

	public void Start () throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, NoSuchFieldException {
		BufferedReader bisr = new BufferedReader (new InputStreamReader (System.in));
		while (true) {
			PrintOptions ();
			try {
				String command = bisr.readLine ().toUpperCase () + "Q";
				System.out.println ();
				switch (command.charAt (0)) {
				case 'A':
					AddCommand (bisr);
					break;
				case 'C':
					remoteServer.ConnectToServer(bisr);
					break;
				case 'D':
					DeleteCommand (bisr);
					break;
				case 'F':
					FindCommand (bisr);
					break;
				case 'G':
					remoteServer.GetConnectionPointsCommand();
					break;
				case 'J':
					remoteServer.JoinCommand(bisr, true);
					break;
				case 'K':
					remoteServer.JoinCommand(bisr, false);
					break;
				case 'L':
					ListCommand ();
					break;
				case 'R':
					remoteServer.RemoveCommand(bisr);
					break;
				case 'U':
					UpdateCommand (bisr);
					break;
				case 'Q':
					System.exit (-1);
					return;
				default:
					System.out.println ("Unknown command: " + command.charAt (0)
							+ ", try again");
					break;
				}
			} catch (IOException e) {
				System.err.println (e.getLocalizedMessage ());
				System.exit (-1);
			}
		}
	}

	static void PrintOptions () {
		System.out.println ();
		System.out.println ("(A) Add new contact");
		System.out.println ("(C) Connect to new server");
		System.out.println ("(D) Delete contact");
		System.out.println ("(F) Find contact");
		System.out.println ("(G) Get connected servers");
		System.out.println ("(J) Join server as joiner");
		System.out.println ("(K) Join server as joinee");
		System.out.println ("(L) List all contacts");
		System.out.println ("(R) Remove server");
		System.out.println ("(U) Update contact");
		System.out.println ("(Q) Quit");
	}

	void AddCommand (BufferedReader bisr) throws IOException 
	{
		BufferedReader bisr2 = new BufferedReader (new InputStreamReader (System.in));
		System.out.println("");
		PrintAddOptions();
		int command = Integer.valueOf(bisr.readLine());
		HashMap<String, Object> values =getInput(bisr2,(Class<?>) classes.get(command-1));

		ui.AddItem(classes.get(command-1), values);

	} 

	void PrintAddOptions() {
		int i = 1;
		for (Class<?> c : classes) {
			System.out.printf ("(%s) Add new %s\n", i,c.getSimpleName());
			i++;
		}
	}	 

	void DeleteCommand (BufferedReader bisr) throws IOException {
		String key = GetName (bisr);
		if (ui.Remove (key)) {
			System.out.println ("DataItem deleted");
		} else {
			System.out.println ("DataItem not found");
		}
	}

	void FindCommand (BufferedReader bisr) throws IOException {
		String key = GetName (bisr);
		Object result = ui.Get(key);
		if (result != null) {
			System.out.println (result.toString());
		} else {
			System.out.println ("DataItem not found");
		}
	}

	void ListCommand () throws IOException {

		Object[] items= ui.GetAll();
		System.out.printf("Items on the server: %s,\n", items.length);
		for (Object c: items) {
			System.out.println (c);
		}
	}

	void UpdateCommand (BufferedReader bisr) throws IOException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, NoSuchFieldException {
		Object key = GetName(bisr);

		IItem<?> c = null;
		c = ui.Get(key);
		if (c!=null) {
		HashMap<String, Object> values = getUpdateInput(bisr, c.getClass());
		System.out.println(ui.Update(key, values));
		} else {
			System.out.println("Dataitem not found");
		}
	}

	static HashMap<String, Object> getUpdateInput(BufferedReader bisr, Class<?> c) throws IOException {
		HashMap<String, Object> values = new HashMap<String, Object>();
		System.out.println("");
		System.out.println("");
		for (Field f : c.getFields()) {	
			System.out.printf("%s:", f.getName());
			String input = bisr.readLine (); 
			while(input.length() ==0) {
				input = bisr.readLine();
			}
			
			values.put(f.getName(), input);
		}
		return values;
	}

	static HashMap<String, Object> getInput(BufferedReader bisr, Class<?> c) throws IOException {
		HashMap<String, Object> values = new HashMap<String, Object>();
		for (Field f : c.getFields()) {
			if (f.getAnnotations().length>0) {
				System.out.printf("%s:", f.getName());
				String input = bisr.readLine (); 
				while(input.length() ==0) {
					input = bisr.readLine();
				}
				values.put(f.getName(), input);			
			}
		}
		return values;
	}

	static String GetName (BufferedReader bisr) throws IOException {
		System.out.print ("Search for: ");
		return bisr.readLine ();
	}

	static String GetPhoneNo (BufferedReader bisr) throws IOException {
		System.out.print ("Phone no: ");
		return bisr.readLine ();
	}
}