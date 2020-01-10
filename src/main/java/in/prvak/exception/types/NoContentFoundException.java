package in.prvak.exception.types;

public class NoContentFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String NO_DATA_FOUND_MESSAGE = "No Data Found";
	
	public NoContentFoundException() {
		super(NO_DATA_FOUND_MESSAGE);
	}
	public NoContentFoundException(String message) {
		super(message);
	}

}
