package bok.labexercise4;

import java.io.Serializable;

/**
 * Basic implementation of Vector Clocks.
 * 
 * @author Andres Rodriguez
 *
 */
public class VectorClock_mazewar implements Serializable {
	
	/**
	 * Default UID
	 */
	private static final long serialVersionUID = 1L;
	private static final int size = 10; 
	
	public int clocks[]= new int[size];
	public int localID = 0;
	
	/**
	 *  Constructor
	 */
	public VectorClock_mazewar(int clientID){
		localID = clientID;
	}
	
	public VectorClock_mazewar(VectorClock_mazewar vclock) {
		localID = vclock.localID;
		
		for (int i = 0;i<size; i++){
			clocks[i] = vclock.clocks[i];
		}
	}

	public void increment(){
		clocks[localID]++;
	}
	
	public void	update(VectorClock_mazewar c){
			
		for (int i = 0;i<size; i++){
			if ( i != localID)
				if (clocks[i] < c.clocks[i])
					clocks[i] = c.clocks[i];
		}
		
	}
	
	/**
	 * Compare two vector clocks
	 * @param c Vector clock to compare against
	 * @return this < c
	 */
	public boolean lessThan(VectorClock_mazewar c){
		if (c == null)
			return true;
		
		for (int i = 0;i<size; i++){
			if ( clocks[i] > c.clocks[i])
				return false;
		}
		return true;
	}
	
	/**
	 * Compare two vector clocks
	 * @param c Vector clock to compare against
	 * @return this < c
	 */
	public boolean equals(VectorClock_mazewar c){
		
		for (int i = 0;i<size; i++){
			if ( clocks[i] != c.clocks[i])
				return false;
		}
		
		return true;
		
	}
	
	public String toString(){
		String s = new String();
		
		for (int i = 0;i<size; i++){
			s += "["+ clocks[i] +"]" ;
		}
		return s;
	}
}