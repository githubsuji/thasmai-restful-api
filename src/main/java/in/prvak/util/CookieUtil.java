package in.prvak.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class CookieUtil {
	
	public static Integer cookieExpiry;
    public static void create(HttpServletResponse response,HttpServletRequest request, String name, String value, Boolean secure, Integer maxAge, String domain) {
    	Cookie cookie = getCookie(request,name);

    	if (cookie != null) {
    	    cookie.setValue(value);    	   
    	    cookie.setMaxAge(maxAge);
    	    System.err.println("updating the cookie");
    	    response.addCookie(cookie);
    	}else{
	        cookie = new Cookie(name, value);
	       // cookie.setSecure(secure);
	        cookie.setHttpOnly(true);
	        cookie.setMaxAge(maxAge);
	       // cookie.setDomain(domain.split(":")[0]);
	    //    cookie.setPath("/");
	        response.addCookie(cookie);
	        System.err.println("creating   the cookie");
    	}
    }

    public static void clear(HttpServletRequest request, HttpServletResponse httpServletResponse, String name) {
    	
    	  Cookie[] cookies = request.getCookies();
    	    if (cookies != null)
    	        for (Cookie cookie : cookies) {
    	            cookie.setValue(null);
    	           // cookie.setPath("/");
    	            cookie.setMaxAge(0);
    	            httpServletResponse.addCookie(cookie);
    	        }
//        Cookie cookie = new Cookie(name, null);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(0);
//        cookie.setDomain("localhost");
//        httpServletResponse.addCookie(cookie);
        System.err.println("cleared the cookie");
    }

    public static String getValue(HttpServletRequest httpServletRequest, String name) {
        Cookie cookie = WebUtils.getCookie(httpServletRequest, name);
        return cookie != null ? cookie.getValue() : null;
    }
    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }

        return null;
    }
    /*
        cookie.setSecure(secure): secure=true => work on HTTPS only.

		cookie.setHttpOnly(true): invisible to JavaScript.
		
		cookie.setMaxAge(maxAge): maxAge=0: expire cookie now, maxAge<0: expire cookie on browser exit.
		
		cookie.setDomain(domain): visible to domain only.
		
		cookie.setPath("/"): visible to all paths.
     */
}

