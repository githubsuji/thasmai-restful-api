package in.prvak.security.jwt;

import java.io.Serializable;

/**
 * @author SUJIKUMAR
 */
public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;
    private String username;

    public JwtAuthenticationResponse(String token, String username) {
        this.token = token;
        this.username =  username;
    }

    public String getToken() {
        return this.token;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
}
