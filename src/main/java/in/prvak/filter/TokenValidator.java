package in.prvak.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import in.prvak.security.jwt.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class TokenValidator {
	private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;
    
	public void validateRequestAndToken(HttpServletRequest request, HttpServletResponse response) {
    	String username = null;
    	String authToken = null;
		boolean isExpectedURI = this.validateURI(request);
		if(isExpectedURI)  authToken = jwtTokenUtil.getToken(request);
		username =  this.validateAndFindUsername(authToken, isExpectedURI);
		this.doValidUserAuthenticationAndUpdateSecurityContext(authToken, isExpectedURI, username, request);
		this.updateTokenForValidUserSession(request, response, isExpectedURI, username);
	}
	private boolean validateURI(HttpServletRequest request) {
		boolean isExpectedURI = true;
    	String requestedURI = request.getRequestURI();
    	logger.info("Expected uri"+requestedURI);
    	if(requestedURI.contains("/thasmai/api/login") || requestedURI.contains("/thasmai/api/create")) {
    		isExpectedURI = false;
    		logger.info("Expected uri");
    	}
    	return isExpectedURI;

	}
	private String validateAndFindUsername(String authToken,boolean isExpectedURI) {
		String username = null;
		   if (authToken != null && isExpectedURI) {
	            try {
	                username = jwtTokenUtil.getUsernameFromToken(authToken);
	            } catch (IllegalArgumentException e) {
	                logger.error("an error occured during getting username from token", e);
	            } catch (ExpiredJwtException e) {
	                logger.warn("the token is expired and not valid anymore", e);
	            }
	        } else {
	            logger.warn("couldn't find bearer string, will ignore the header");
	        }
		 return username;

	}
	
	private void doValidUserAuthenticationAndUpdateSecurityContext(String authToken,boolean isExpectedURI, String username, HttpServletRequest request) {
        logger.info("checking authentication for user " + username);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && isExpectedURI) {

            // It is not compelling necessary to load the use details from the database. We could also store the information
            // in the token and read it from it. It's up to us ;)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // For simple validation it is completely sufficient to just check the token integrity. We don't have to call
            // the database compellingly. Again it's up to us ;)
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //chain.doFilter(request, response);
            }
        }

	}
	private void updateTokenForValidUserSession(HttpServletRequest request, HttpServletResponse response,boolean isExpectedURI, String username) {
        if (username != null && SecurityContextHolder.getContext().getAuthentication() != null && isExpectedURI) {
            jwtTokenUtil.updateToken(request, response);
       }
	}

}
