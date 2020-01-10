package in.prvak.security.jwt;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author SUJIKUMAR
 */
public class  JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    @ApiModelProperty(notes = "Your username",name="username",required=true,value="Sample value",example="admin")
   private String username;
    @ApiModelProperty(notes = "Your password",name="password",required=true,value="Sample value",example="admin")
    private String password;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	@Override
	public String toString() {
		return "JwtAuthenticationRequest [username=" + username + ", password=" + password + "]";
	}
    
}
