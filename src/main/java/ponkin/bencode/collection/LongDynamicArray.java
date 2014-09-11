package ponkin.bencode.collection;

public class LongDynamicArray extends DynamicArray { 
	
	private long[] data;
	
	public LongDynamicArray(int initialCapacity, int limit){
		super(limit);
		if(initialCapacity > limit) 
			throw new IllegalArgumentException("Initial capacity can not be greater than limit");
		data = new long[initialCapacity];
	}
	
	public void append(long b){
		if( !isEnoughSpace(1) )
			enlarge();
		data[positionAndIncr()] = b;
	}
	
	public void put(int idx, long b){
		data[idx] = b;
	}
	
	public long get(int idx){
		return data[idx];
	}
	
	@Override
	protected final void enlarge(){
		if(data.length == limit()) throw new DynamicArrayLimitExceedException("Buffer limit exceed");
		int newLen = data.length * 3 / 2;
		if(newLen > limit()) newLen = limit();
		long[] new_data = new long[newLen];
		System.arraycopy( data, 0, new_data, 0, length() );
		data = new_data;
	}
	
	@Override
	protected final int length(){
		return data.length;
	}
	
	@Override
	public void compact(){
		int pos = position();
		long[] new_data = new long[pos];
		System.arraycopy( data, 0, new_data, 0, pos );
		data = new_data;
	}	
	
	public long[] array(){
		return data;
	}	
	
}