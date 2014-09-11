package ponkin.bencode.collection;

public class ByteDynamicArray extends DynamicArray {
	
	private byte[] data;
	
	public ByteDynamicArray(int initialCapacity, int limit){
		super(limit);
		if(initialCapacity > limit) 
			throw new IllegalArgumentException("Initial capacity can not be greater than limit");
		data = new byte[initialCapacity];
	}
	
	public void append(byte b){
		if( !isEnoughSpace(1) )
			enlarge();
		data[positionAndIncr()] = b;
	}
	
	public void append(byte[] bb){
		if( !isEnoughSpace(bb.length) )
			enlarge();
		System.arraycopy( bb, 0, data, position(), bb.length );
		position(position() + bb.length);
	}
	
	@Override
	protected final void enlarge(){
		int newLen = data.length * 3 / 2;
		if(newLen > limit()) newLen = limit();
		byte[] new_data = new byte[newLen];
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
		byte[] new_data = new byte[pos];
		System.arraycopy( data, 0, new_data, 0, pos );
		data = new_data;
	}
	
	public byte[] array(){
		return data;
	}
	
}