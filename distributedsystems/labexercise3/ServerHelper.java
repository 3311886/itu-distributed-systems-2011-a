package bok.labexercise3;


/**
 * 
 * @author Andreas 
 *
 * Class to help encapsulate server methods  
 */
public class ServerHelper{

	/***
	 * 
	 * @param args arguments for the server method 
	 * @return returns result from invoked method. 
	 * TODO: Null?  
	 */
	public static Object UnmarshallRequest(Object[] args ) { 
		int methodId;
		Object arg;
		Object[] param;

		try {
			methodId = Integer.valueOf(args[0].toString());
			param = new Object[args.length-1];
			for (int i = 1;i< args.length; i++) {
				param[i-1] = args[i]; 
			}
			arg = args[1];
			switch (methodId) {
			case (0):
				return toLowerCase(param);
			case (1):   
				return toUpperCase(param);
			case (2):
				return getObject(Integer.valueOf(arg.toString()));
			case (3):
				return addObject(param);
			case (4) : 
				return contains(arg);
			default:
				return arg;		
			}	
		} catch (Exception e) {
			return null;
		}
	}

	/***
	 * 
	 * @param msg array of messages to be turned into upper case 
	 * @return
	 */
	private static String toUpperCase(Object[] msg) {
		StringBuilder sb = new StringBuilder();
		for (Object s : msg)
			sb.append(s.toString().toUpperCase() + "\n");
		return sb.toString().trim();
	}

	/***
	 * 
	 * @param msg array of messages to be turned into lower case 
	 * @return
	 */
	private static String toLowerCase(Object[] msg) {
		StringBuilder sb = new StringBuilder();
		for (Object s : msg)
			sb.append(s.toString().toLowerCase() + "\n");
		return sb.toString().trim();
	}

	/***
	 * 
	 * @param index at which the object will be removed
	 * @return
	 */
	private static Object getObject(int index) {
		// TODO Auto-generated method stub
		return Storage.getObject(index);
	}

	/***
	 * 
	 * @param o Objectcollection to add 
	 * @return
	 */
	private static Object addObject(Object[] o) {
		for(Object e : o) { 
			Storage.putObject(e);
		}
		return null;
	}

	/***
	 * 
	 * @param o Object to be inquire about 
	 * @return
	 */
	private static Object contains(Object o) {
		return Storage.getStorage().contains(o); 
	}
}
