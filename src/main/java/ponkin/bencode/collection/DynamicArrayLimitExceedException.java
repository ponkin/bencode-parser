package ponkin.bencode.collection;

public class DynamicArrayLimitExceedException extends RuntimeException {
	

    private static final long serialVersionUID = -5484897634319144536L;

    public DynamicArrayLimitExceedException(String message) {
		super(message);
	}	
	
}