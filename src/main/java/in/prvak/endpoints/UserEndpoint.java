package in.prvak.endpoints;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import in.prvak.common.annotations.RestApiController;
import in.prvak.jpa.entities.security.User;
import in.prvak.security.jwt.JwtTokenUtil;
import in.prvak.services.security.UserResponse;
import in.prvak.services.security.UserService;
import in.prvak.util.CookieUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestApiController
@Api("REST API for User Management!!!!")
@RequestMapping("/user/")
public class UserEndpoint {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	@Value("${cookie.expiry}")
	private Integer cookieExpiry;
	// https://github.com/swagger-api/swagger-core/wiki/annotations

	@PostConstruct
	public void initAfter() {
		CookieUtil.cookieExpiry = cookieExpiry;
	}

	@Autowired
	public PasswordEncoder passwordEncoder;
	@RequestMapping(path = "role", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@ApiOperation(httpMethod = "POST", value = "Resource to Create new Role", response = String.class, nickname = "createRole")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created | OK"),
			@ApiResponse(code = 200, message = "Success | OK"), @ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"), @ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<?> createRole(@ApiParam(name = "role", value = "Prefix with ROLE_ and use uppercase values", defaultValue = "", example="ROLE_ADMIN") @RequestParam String role) throws AuthenticationException {

		String status = userService.createRole(role);
		return ResponseEntity.ok(status);
	}
	@RequestMapping(path = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "Resource to Create new User", response = UserResponse.class, nickname = "createUser")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created | OK"),
			@ApiResponse(code = 200, message = "Success | OK"), @ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"), @ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<?> createUser(@RequestBody User user, HttpServletResponse response,
			UriComponentsBuilder ucBuilder, @RequestHeader(value = "roles", required = true) String[] roles)
			throws AuthenticationException {

		UserResponse ur = userService.createUser(user, roles);
		return ResponseEntity.ok(ur);
	}
	@RequestMapping(path = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "PUT", value = "Resource to Update User", response = UserResponse.class, nickname = "updateUser")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created | OK"),
			@ApiResponse(code = 200, message = "Success | OK"), @ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"), @ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<?> updateUser(@RequestBody User user, HttpServletResponse response,
			UriComponentsBuilder ucBuilder, @RequestHeader(value = "roles", required = true) String[] roles)
			throws AuthenticationException {
		UserResponse ur = null;
		User fetchedUser =   userService.getUserByName(user.getUsername());
		if(null != fetchedUser ) {
			 user.setId(fetchedUser.getId());
			 user.setPassword(fetchedUser.getPassword());
			 ur = userService.updateUser(user, roles);
		}else {
			ur = new UserResponse();
			ur.setStatus("Failed to Update. User not found");
			
		}
		
		return ResponseEntity.ok(ur);
	}
	
}

