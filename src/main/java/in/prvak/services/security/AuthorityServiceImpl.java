package in.prvak.services.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import in.prvak.jpa.entities.security.Authority;
import in.prvak.repository.security.AuthorityRepository;

@Service
public class AuthorityServiceImpl implements AuthorityService{
	@Autowired
	private AuthorityRepository authorityRepository;
	
	public List<Authority> getAllAuthorities(int offset,int limit,String field,String order) {
		PageRequest request = PageRequest.of(offset==0 ? offset+1 : offset, offset, 
				Sort.by(Direction.fromString(order), field));
		return authorityRepository.findAll(request).getContent();
	}
	
	public Authority getAuthority(String name) {
		Authority authority = authorityRepository.findByRoleName(name);
		return authority;
	}

	public AuthorityResponse addAuthorities(Authority authorities[]) {
		// TODO Auto-generated method stub
		String status=null;
		AuthorityResponse response  = new AuthorityResponse();
		for(Authority authority : authorities){
			Authority d =authorityRepository.save(authority);
		    status = "Authority added successfully";
		    status = d !=null ? status :"Not added successfully";
		    response.setStatus(status);
		}
		return response;
	}
	
	public AuthorityResponse updateAuthority(Authority authority) {
		String status = "Authority updated successfully";
		Authority d =authorityRepository.save(authority);
		AuthorityResponse response  = new AuthorityResponse();
		status = d !=null ? status :"Not updated successfully";
		response.setAuthority(d);
		response.setStatus(status);
		return response;
	}
	
	public AuthorityResponse deleteAuthorities() {
		String status = "Authority deleted successfully";
		authorityRepository.deleteAll();
		AuthorityResponse response  = new AuthorityResponse();
		response.setStatus(status);
		return response;
		
	}
	
//	public AuthorityResponse deleteAuthority(Long authorityId) {
//	String status = "Authority deleted successfully";
//	authorityRepository.delete(authorityId);
//	AuthorityResponse response  = new AuthorityResponse();
//	response.setStatus(status);
//	return response;
//		
//	}

		
}
