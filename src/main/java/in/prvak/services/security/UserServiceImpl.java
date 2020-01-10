package in.prvak.services.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.prvak.jpa.entities.security.Authority;
import in.prvak.jpa.entities.security.User;
import in.prvak.repository.security.AuthorityRepository;
import in.prvak.repository.security.UserRepository;


@Service
public class UserServiceImpl implements UserService{
	  @Autowired
	  private UserRepository userRepository;
	  @Autowired
	  public PasswordEncoder passwordEncoder;
	  @Autowired
	  private AuthorityRepository authorityRepository;
	
	public List<User> getAllUsers(int offset,int limit,String field,String order) {

		PageRequest request = PageRequest.of(offset==0 ? offset+1 : offset, offset, 
				Sort.by(Direction.fromString(order), field));
				
		return userRepository.findAll(request).getContent();
	}
	
	public User getUserByName(String userName) {
		User user = userRepository.findByUsername(userName);
		
		return user;
	}

	public UserResponse addUsers(User users[]) {
		// TODO Auto-generated method stub
		String status=null;
		UserResponse response  = new UserResponse();
		for(User user : users){
			User d =userRepository.save(user);
		    status = "User added successfully";
		    status = d !=null ? status :"Not added successfully";
		    response.setStatus(status);
		}
		return response;
	}
	
	public UserResponse updateUser(User user) {
		String status = "Authority updated successfully";
		User d =userRepository.save(user);
		UserResponse response  = new UserResponse();
		status = d !=null ? status :"Not updated successfully";
		response.setUser(d);
		response.setStatus(status);
		return response;
	}
	
	public UserResponse deleteUsers() {
		String status = "User deleted successfully";
		userRepository.deleteAll();
		UserResponse response  = new UserResponse();
		response.setStatus(status);
		return response;
		
	}
	
	public UserResponse deleteUser(Long userId) {
		String status = "User deleted successfully";
		userRepository.deleteById(userId);
		UserResponse response  = new UserResponse();
		response.setStatus(status);
		return response;
		
	}

	@Override
	public UserResponse  createUser(User user,String[] roles) {
		UserResponse ur = new UserResponse("User creation failed.");
		System.out.println("Password "+user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		List<Authority> authorities =authorityRepository.findByRoleNameIn(Arrays.asList(roles));
		System.out.println(authorities); 	
		//user.set
		user.setAuthorities(authorities);
		User d =userRepository.save(user);
		d.setPassword("");
		ur.setUser(d);
		if(null != d ) {
			ur.setStatus("User successfully created");
		}
		System.err.println(d);
		return ur;
	}
	@Override
	public UserResponse updateUser(User user,String[] roles) {
		UserResponse ur = new UserResponse("User update failed.");
		System.out.println("Password "+user.getPassword());
		// user.setPassword(passwordEncoder.encode(user.getPassword()));
		List<Authority> authorities =authorityRepository.findByRoleNameIn(Arrays.asList(roles));
		System.out.println(authorities); 	
		//user.set
		user.setAuthorities(authorities);
		User d =userRepository.save(user);
		d.setPassword("");
		ur.setUser(d);
		
		if(null != d ) {
			ur.setStatus("User successfully updated");
		}
		System.err.println(d);
		return ur;
	}
	@Override
	public UserResponse  updatePassword(User user,String[] roles) {
		UserResponse ur = new UserResponse("Password update failed.");
		System.out.println("Password "+user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		List<Authority> authorities =authorityRepository.findByRoleNameIn(Arrays.asList(roles));
		System.out.println(authorities); 	
		//user.set
		user.setAuthorities(authorities);
		User d =userRepository.save(user);
		d.setPassword("");
		ur.setUser(d);
		if(null != d ) {
			ur.setStatus("Password successfully updated");
		}
		System.err.println(d);
		return ur;
	}
	@Override
	public String createRole(String role) {
		Authority authority = new Authority(role);
		Authority createdAutority = authorityRepository.save(authority);
		return null != createdAutority ?  "Role successfully created" : "Role creation failed.";
	}

		
}
