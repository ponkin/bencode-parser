package ponkin.bencode.collection;

/**
 * Base class for dynamic resizable array of primitives
 */
public abstract class DynamicArray {
	
	private int position = 0;
	private final int limit;
	
	public DynamicArray(int limit){
		this.limit = limit;
	}
	
	public int position(){
		return position;
	}
	
	public void position(int position){
		this.position = position;
	}
	
	protected int positionAndIncr(){
		return position++;
	}
	
	public int limit(){
		return limit;
	}
	
	protected boolean isEnoughSpace(int size){
		return position + size < length();
	}
	
	protected abstract int length();
	
	protected abstract void enlarge();
	
	public abstract void compact();
	
	
}