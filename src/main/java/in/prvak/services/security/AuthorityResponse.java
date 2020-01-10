package in.prvak.services.security;

import in.prvak.jpa.entities.security.Authority;

public class AuthorityResponse {
	private String status;
	private Authority authority;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Authority getAuthority() {
		return authority;
	}
	public void setAuthority(Authority authority) {
		this.authority = authority;
	}
	
}
