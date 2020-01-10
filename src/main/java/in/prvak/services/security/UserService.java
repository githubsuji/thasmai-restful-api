package in.prvak.services.security;

import java.util.List;

import in.prvak.jpa.entities.security.User;

public interface UserService {
	public User getUserByName(String userName);
	public UserResponse addUsers(User users[]);
	public UserResponse updateUser(User user);
	public UserResponse deleteUsers();
	public UserResponse deleteUser(Long userId);
	List<User> getAllUsers(int offset, int limit,String field, String order);
	public UserResponse createUser(User user, String[] roles);
	public String createRole(String role);
	UserResponse updatePassword(User user, String[] roles);
	UserResponse updateUser(User user, String[] roles);

}


