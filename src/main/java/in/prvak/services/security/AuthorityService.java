package in.prvak.services.security;

import java.util.List;

import in.prvak.jpa.entities.security.Authority;

public interface AuthorityService {
	public Authority getAuthority(String name);
	public AuthorityResponse addAuthorities(Authority authorities[]);
	public AuthorityResponse updateAuthority(Authority authority);
	public AuthorityResponse deleteAuthorities();
//	public AuthorityResponse deleteAuthority(Long authorityId);
	List<Authority> getAllAuthorities(int offset, int limit,String field, String order);

}