package in.prvak.security.jwt;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import in.prvak.common.utils.TimeProvider;
import in.prvak.util.CookieUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * @author SUJIKUMAR
 *
 */
@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -3301605591108950415L;
	private final Log logger = LogFactory.getLog(this.getClass());

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_AUDIENCE = "aud";
	static final String CLAIM_KEY_CREATED = "iat";

	static final String AUDIENCE_UNKNOWN = "unknown";
	static final String AUDIENCE_WEB = "web";
	static final String AUDIENCE_MOBILE = "mobile";
	static final String AUDIENCE_TABLET = "tablet";
	public static final String JWT_TOKEN_COOKIENAME = "JWT-TOKEN";
	//private static final String REDIS_SET_ACTIVE_SUBJECTS = "active-subjects";

	@Autowired
	private TimeProvider timeProvider;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	@Value("${cookie.expiry}")
	private Integer cookieExpiry;

	@Value("${jwt.header}")
	private String tokenHeader;
//	@Autowired
//	private RedisUtil redisUtil;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public String getAudienceFromToken(String token) {
		return getClaimFromToken(token, Claims::getAudience);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expirationDate = getExpirationDateFromToken(token);
		return expirationDate.before(timeProvider.now());
	}

	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	private String generateAudience(Device device) {
		String audience = AUDIENCE_UNKNOWN;
		if (device.isNormal()) {
			audience = AUDIENCE_WEB;
		} else if (device.isTablet()) {
			audience = AUDIENCE_TABLET;
		} else if (device.isMobile()) {
			audience = AUDIENCE_MOBILE;
		}
		return audience;
	}

	private Boolean ignoreTokenExpiration(String token) {
		String audience = getAudienceFromToken(token);
		return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
	}

	public String generateToken(UserDetails userDetails, Device device) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername(), generateAudience(device));
	}

	private String doGenerateToken(Map<String, Object> claims, String subject, String audience) {
		final Date createdDate = timeProvider.now();
		final Date expirationDate = calculateExpirationDate(createdDate);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy hh:mm:ss");
		String createdDateStr = sdf.format(createdDate);
		String expirationDateStr = sdf.format(expirationDate);

		System.out.println("token Created Date Time " + createdDateStr);
		System.out.println("token Expiry Date Time " + expirationDateStr);

		return Jwts.builder().setClaims(claims).setSubject(subject).setAudience(audience).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getIssuedAtDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public String refreshToken(String token) {
		final Date createdDate = timeProvider.now();
		final Date expirationDate = calculateExpirationDate(createdDate);

		final Claims claims = getAllClaimsFromToken(token);
		claims.setIssuedAt(createdDate);
		claims.setExpiration(expirationDate);

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		JwtUser user = (JwtUser) userDetails;
		final String username = getUsernameFromToken(token);
		final Date created = getIssuedAtDateFromToken(token);
		logger.info("Token Created....date===" + created);
		logger.info("Last password reset date....===" + user.getLastPasswordResetDate());
		final Date expiration = getExpirationDateFromToken(token);
		logger.info("expiration===" + expiration);
		logger.info("TimeProvider" + timeProvider.now());
		return (username.equals(user.getUsername()) && !isTokenExpired(token));
		// && !isCreatedBeforeLastPasswordReset(created,
		// user.getLastPasswordResetDate())
//				&& redisUtil.isMember(REDIS_SET_ACTIVE_SUBJECTS, username));
	}

	private Date calculateExpirationDate(Date createdDate) {
		return new Date(createdDate.getTime() + (expiration * 1000));
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private String getJwtCookieToken(HttpServletRequest request) {
		final String token = CookieUtil.getValue(request, JWT_TOKEN_COOKIENAME);
		if (token == null) {
			logger.info("token from cookie=" + token);
			return null;
		}
		logger.info("token from cookie=" + token);
		return token;
	}

	private String getJwtHeaderToken(HttpServletRequest request) {
		final String requestHeader = request.getHeader(this.tokenHeader);
		String token = null;

		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			try {
				token = requestHeader.substring(7);
			} catch (Exception e) {
				logger.error("an error occured during getting username from token", e);
			}
		}

		return token;
	}

	public String getToken(HttpServletRequest request) {
		String token = this.getJwtCookieToken(request);
		return null != token ? token : this.getJwtHeaderToken(request);
	}

//	public void invalidateRelatedTokens(String userName) {
//		// TODO Auto-generated method stub
//		redisUtil.removeSubject(REDIS_SET_ACTIVE_SUBJECTS, userName);
//
//	}

	public void updateToken(HttpServletRequest request, HttpServletResponse response) {
		String token = getToken(request);
		String host = request.getHeader("host");
		System.out.println("host==" + host);

		String refreshedToken = refreshToken(token);
		CookieUtil.create(response, request, JwtTokenUtil.JWT_TOKEN_COOKIENAME, refreshedToken, false, cookieExpiry,
				host);

	}
}
