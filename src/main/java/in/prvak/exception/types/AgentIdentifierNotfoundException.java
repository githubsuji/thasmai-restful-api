package in.prvak.exception.types;

public class AgentIdentifierNotfoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public AgentIdentifierNotfoundException() {
	}

	public AgentIdentifierNotfoundException(String message) {
		super(message);
	}

	public AgentIdentifierNotfoundException(Throwable cause) {
		super(cause);
	}

	public AgentIdentifierNotfoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public AgentIdentifierNotfoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
