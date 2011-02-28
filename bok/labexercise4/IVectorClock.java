package bok.labexercise4;

public interface IVectorClock<K,V> extends Comparable<V>{
	/**
	 * Increases the component of pUnit by 1.
	 * 
	 * @param pUnit - The ID of the vector element being increased.
	 */
	public abstract void incrementClock(K pUnit);

	/**
	 * GUI operation, returns the IDs in some neat order.
	 * 
	 * @return The IDs of the elements in the Clock.
	 */
	public abstract K[] getOrderedIDs();

	/**
	 * GUI operation, returns the values in some neat order.
	 * 
	 * @return The Values of the elements in the Clock.
	 */
	public abstract V[] getOrderedValues();

	public abstract V get(K key);

	public abstract String toString();
}