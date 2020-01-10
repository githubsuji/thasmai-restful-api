package in.prvak.services.security;

import in.prvak.jpa.entities.security.User;

public class UserResponse {
	private String status;
	private User user;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public UserResponse(String status) {
		this.status = status;
	}
	public UserResponse() {
	}
	
	
	
}

