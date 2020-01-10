package in.prvak.endpoints;
//package org.calibroz.api.hrms.resources.endpoints.security;
//
//import java.util.List;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.DefaultValue;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.MediaType;
//
//import org.calibroz.api.application.bootstrap.BaseController;
//import org.calibroz.api.hrms.resources.services.security.AuthorityResponse;
//import org.calibroz.api.hrms.resources.services.security.AuthorityService;
//import org.calibroz.api.jpa.entities.security.Authority;
//import org.calibroz.api.jpa.entities.security.types.AuthorityName;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//@Path("/authority")
//
//public class AuthorityEndpoint {
//	@Autowired
//	private AuthorityService authorityService;
//	@GET
//    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_OCTET_STREAM })
//	public List<Authority> getAllAuthorities(@DefaultValue("1")@QueryParam("Offset") int offset,
//			@DefaultValue("2") @QueryParam("limit") int limit, @DefaultValue("id")@QueryParam("field") String field,@DefaultValue("DESC")@QueryParam("order") String order){
//		System.out.println("offset: "+offset);
//		System.out.println("limit: "+limit);
//
//		List<Authority> authorities = authorityService.getAllAuthorities(offset,limit,field,order);
//		return authorities;
//	}
//	
//	@GET
//	@Consumes({MediaType.APPLICATION_JSON})
//    @Produces("application/json")
//	@Path("/{authorityName}")
//	public Authority getAuthority(@PathParam("userName") String authorityName){
//		Authority authority = authorityService.getAuthority(authorityName);
//		return authority;
//	}
//	
//	@POST
//	@Consumes({MediaType.APPLICATION_JSON})
//    @Produces("application/json")	
//	public AuthorityResponse addAuthorities(Authority authorities[]){
//		AuthorityResponse status = authorityService.addAuthorities(authorities);
//	        return status;
//	}
//	
//	@PUT
//	@Consumes({MediaType.APPLICATION_JSON})
//	@Produces("application/json")
//	public AuthorityResponse updateAuthority(Authority authority){
//		AuthorityResponse status=authorityService.updateAuthority(authority);
//		return status;
//	}
//
//	@DELETE
//	@Consumes({MediaType.APPLICATION_JSON})
//	@Produces("application/json")
//	public AuthorityResponse deleteAuthorities(Authority authority){
//		AuthorityResponse status=authorityService.deleteAuthorities();
//		return status;
//	}
//	
////	@DELETE
////	@Consumes({MediaType.APPLICATION_JSON})
////	@Produces("application/json")
////	@Path("/{authorityId}")
////	public AuthorityResponse deleteAuthority(@PathParam("authorityId") Long authorityId){
////		AuthorityResponse status=authorityService.deleteAuthority(authorityId);
////	return status;
////	}
//
//}
//
