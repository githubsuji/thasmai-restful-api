package in.prvak.repository.security;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import in.prvak.jpa.entities.security.Authority;

@Repository
public interface AuthorityRepository extends PagingAndSortingRepository<Authority, Long> {


	public Authority findByRoleName(String name);
	
	List<Authority> findByRoleNameIn(List<String> roleNames);
	
	
}

