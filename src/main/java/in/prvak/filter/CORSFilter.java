package in.prvak.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("*************Entered into CorsFilter*************************");
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, HEAD");
    	httpResponse.setHeader("Access-Control-Allow-Headers", "X-Auth-Token, Content-Type, responseType, Origin, Accept, Authorization");
        httpResponse.setHeader("Access-Control-Expose-Headers", "test anthem, custom-header2");
    	httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
    	httpResponse.setHeader("Access-Control-Max-Age", "4800");
    	httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    	httpResponse.setHeader("Pragma", "no-cache");
    	httpResponse.setHeader("Expires", "0");
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
        	httpResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
        	chain.doFilter(request, response);
        }

	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
