package bok.labexercise3;


public class ServerHelper{

	public static Object UnmarshallRequest(Object[] args ) { 
		int methodId;
		Object arg;

		try {
			methodId = Integer.valueOf(args[0].toString());
			arg = args[1];
			switch (methodId) {
			case (0):
				return toLowerCase(arg.toString());
			case (1):   
				return toUpperCase(arg.toString());
			case (2):
				return getObject(Integer.valueOf(arg.toString()));
			case (3):
				System.out.println("Add object");
				return addObject(arg);
			default:
				return arg;		
			}	
		} catch (Exception e) {
			return null;
		}
	}

	private static String toUpperCase(String msg) {
		return msg.toUpperCase();
	}

	private static String toLowerCase(String msg) {
		return msg.toLowerCase();
	}


	private static Object getObject(int index) {
		// TODO Auto-generated method stub
		return Storage.getObject(index);
	}

	private static Object addObject(Object o) {
		Storage.putObject(o);
		return null;
	}

}
