package in.prvak.endpoints;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// import org.callibroz.api.redis.RedisUtil;
//import org.calibroz.api.security.jwt.JwtTokenUtil;
//import org.calibroz.api.security.jwt.JwtUser;
//import org.calibroz.api.security.jwt.cookie.CookieUtil;
//import org.callibroz.api.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import in.prvak.common.annotations.RestApiController;
import in.prvak.common.utils.PasswordClass;
import in.prvak.jpa.entities.security.Authority;
import in.prvak.jpa.entities.security.User;
import in.prvak.security.jwt.JwtAuthenticationRequest;
import in.prvak.security.jwt.JwtAuthenticationResponse;
import in.prvak.security.jwt.JwtTokenUtil;
import in.prvak.security.jwt.JwtUser;
import in.prvak.services.security.UserResponse;
import in.prvak.services.security.UserService;
import in.prvak.util.CookieUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestApiController
@Api("REST API for Authentication!!!!")
public class AuthRestController {
	// https://jira.spring.io/browse/SPR-16336
//	@Autowired
//	public RedisUtil redisUtil;

//	private static final String REDIS_SET_ACTIVE_SUBJECTS = "active-subjects";

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

	@RequestMapping(path = "${jwt.route.authentication.login}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "Resource to Create Authorized Token", response = JwtAuthenticationResponse.class, nickname = "createAuthenticationToken")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created | OK"),
			@ApiResponse(code = 200, message = "Success | OK"), @ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"), @ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<JwtAuthenticationResponse> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, Device device,
			@RequestHeader(value = "host", required = false) String host, HttpServletResponse response,
			HttpServletRequest request, UriComponentsBuilder ucBuilder) throws AuthenticationException {

		System.out.println(authenticationRequest);
		// Perform the security
		// passwordEncoder.encode(authenticationRequest.getPassword())
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		// Reload password post-security so we can generate token
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails, device);
		CookieUtil.create(response, request, JwtTokenUtil.JWT_TOKEN_COOKIENAME, token, false, cookieExpiry, host);
		// Return the token
//		redisUtil.addSubject(REDIS_SET_ACTIVE_SUBJECTS, authenticationRequest.getUsername());
		HttpHeaders headers = new HttpHeaders();

		headers.add(JwtTokenUtil.JWT_TOKEN_COOKIENAME, token);
		headers.setLocation(ucBuilder.path("${jwt.route.authentication.path}")
				.buildAndExpand(authenticationRequest.getUsername()).toUri());
		return new ResponseEntity<JwtAuthenticationResponse>(
				new JwtAuthenticationResponse(token, userDetails.getUsername()), headers, HttpStatus.CREATED);
	}

/*	@RequestMapping(path = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "Resource to Create new User", response = String.class, nickname = "createUser")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created | OK"),
			@ApiResponse(code = 200, message = "Success | OK"), @ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"), @ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<?> createUser(@RequestBody User user, HttpServletResponse response,
			UriComponentsBuilder ucBuilder, @RequestHeader(value = "roles", required = true) String[] roles)
			throws AuthenticationException {

		UserResponse ur = userService.createUser(user, roles);
		return ResponseEntity.ok(ur);
	}

	@RequestMapping(path = "createRole", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@ApiOperation(httpMethod = "POST", value = "Resource to Create new Role", response = String.class, nickname = "createRole")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created | OK"),
			@ApiResponse(code = 200, message = "Success | OK"), @ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"), @ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<?> createRole(@ApiParam(name = "role", value = "Prefix with ROLE_ and use uppercase values", defaultValue = "", example="ROLE_ADMIN") @RequestParam String role) throws AuthenticationException {

		String status = userService.createRole(role);
		return ResponseEntity.ok(status);
	}*/

	@RequestMapping(path = "${jwt.route.authentication.refresh}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "Refresh Authentication Token", response = JwtAuthenticationResponse.class, nickname = "refreshAndGetAuthenticationToken")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success | OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request, HttpServletResponse response,
			@RequestHeader(required = false) String host) {
		String token = jwtTokenUtil.getToken(request);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
		System.out.println("host==" + host);

		if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			CookieUtil.create(response, request, JwtTokenUtil.JWT_TOKEN_COOKIENAME, refreshedToken, false, cookieExpiry,
					host);
			return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken, username));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@GetMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "Log out", response = String.class, nickname = "logout")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success | OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

		String token = jwtTokenUtil.getToken(request);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
		String message = "Invalid Token! Logout Failed..";
		if (jwtTokenUtil.validateToken(token, userDetails)) {
//			jwtTokenUtil.invalidateRelatedTokens(userDetails.getUsername());
			CookieUtil.clear(request, response, JwtTokenUtil.JWT_TOKEN_COOKIENAME);

			message = "Successful Logout";
		}
		class Status {
			public String status;

			public Status(String status) {
				this.status = status;
			}
		}

		Status stat = new Status(message);
		System.out.println("message====" + message);
		HttpHeaders headers = new HttpHeaders();
		// headers.add("Set-Cookie",JwtTokenUtil.JWT_TOKEN_COOKIENAME+"=; Max-Age=0;");
		return new ResponseEntity<>(stat, headers, HttpStatus.OK);

	}

	@RequestMapping(path = "${jwt.route.authentication.user}", method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "Get User Details", response = JwtAuthenticationResponse.class, nickname = "getAuthenticatedUser")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success | OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public JwtUser getAuthenticatedUser(HttpServletRequest request) {
		String token = request.getHeader(tokenHeader);
		if (null == token) {
			Cookie cookie = CookieUtil.getCookie(request, JwtTokenUtil.JWT_TOKEN_COOKIENAME);
			token = cookie.getValue();
		} else {
			token = token.substring(7);
		}
		System.err.println(token);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		System.err.println(username);
		JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
		Iterator<GrantedAuthority> itr = (Iterator<GrantedAuthority>) user.getAuthorities().iterator();
		while (itr.hasNext()) {
			GrantedAuthority s = itr.next();
			String role = s.getAuthority();
			System.out.println("Roleeeeeeee= " + role);
		}
		System.err.println(user);
		return user;
	}
	@RequestMapping(path = "password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "PUT", value = "Resource to Update password", response = String.class, nickname = "updatePassword")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created | OK"),
			@ApiResponse(code = 200, message = "Success | OK"), @ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"), @ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<?> updatePassword(@RequestBody PasswordClass password, HttpServletRequest request)
			throws AuthenticationException {

		UserResponse ur = null;
		String token = request.getHeader(tokenHeader);
		if (null == token) {
			Cookie cookie = CookieUtil.getCookie(request, JwtTokenUtil.JWT_TOKEN_COOKIENAME);
			token = cookie.getValue();
		} else {
			token = token.substring(7);
		}
		System.err.println(token);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		User fetchedUser =   userService.getUserByName(username);
		List<Authority> authorities =  fetchedUser.getAuthorities();
		Iterator<Authority> itr = authorities.iterator();
		List<String> list = new ArrayList<String>();
		while (itr.hasNext()) {
			Authority s = itr.next();
			String role = s.getRoleName();
			System.out.println("Roleeeeeeee= " + role);
			list.add(role);
		}
		String[] roles =  new String[list.size()];
		list.toArray(roles);
		if(null != fetchedUser ) {
			fetchedUser.setPassword(password.password);
			 ur = userService.updatePassword(fetchedUser, roles);
		}else {
			ur = new UserResponse();
			ur.setStatus("Failed to Update. User not found");
			
		}
		
		return ResponseEntity.ok(ur);
	}
	

	/**
	 * This is an example of some different kinds of granular restriction for
	 * endpoints. You can use the built-in SPEL expressions in @PreAuthorize such as
	 * 'hasRole()' to determine if a user has access. Remember that the hasRole
	 * expression assumes a 'ROLE_' prefix on all role names. So 'ADMIN' here is
	 * actually stored as 'ROLE_ADMIN' in database!
	 **/
	@RequestMapping(path = "/protected", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getProtectedGreeting() {

		return ResponseEntity.ok("Greetings from admin protected method!");
	}

}
