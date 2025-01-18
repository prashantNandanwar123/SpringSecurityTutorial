package com.security.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.payload.Response;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	        throws ServletException, IOException {
	    logger.info("Request come from : {}", request.getRequestURI());

	    try {
	        String token = getTokenFromRequest(request);
	        logger.info("Token from request: {}", token);

	        if (token != null && tokenProvider.validateToken(token)) {
	            String username = tokenProvider.getUsernameFromJWT(token);
	            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

	            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
	                    userDetails, null, userDetails.getAuthorities());
	            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

	            logger.info("Authentication successful for user: {}", username);
	        } else {
	            logger.warn("Invalid or missing token for request: {}", request.getRequestURI());

	            // Create a custom response
	            Response customResponse = new Response();
	            customResponse.setRespCode(1);
	            customResponse.setRespMsg("Unauthorized: Missing or invalid token");

	            // Set response status and content type
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.setContentType("application/json");

	            // Write the custom response as JSON
	            ObjectMapper mapper = new ObjectMapper();
	            response.getWriter().write(mapper.writeValueAsString(customResponse));
	            response.getWriter().flush();
	            return; // Stop further processing
	        }
	    } catch (Exception ex) {
	        logger.error("Error during authentication: {}", ex.getMessage());

	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json");

	        // Create a custom error response
	        Response errorResponse = new Response();
	        errorResponse.setRespCode(1);
	        errorResponse.setRespMsg("Unauthorized: " + ex.getMessage());

	        // Write the custom response as JSON
	        ObjectMapper mapper = new ObjectMapper();
	        response.getWriter().write(mapper.writeValueAsString(errorResponse));
	        response.getWriter().flush();
	        return; // Stop further processing
	    }

	    chain.doFilter(request, response);
	}


	private String getTokenFromRequest(HttpServletRequest request) {
	    String bearerToken = request.getHeader("Authorization");
	    logger.info("Authorization header: {}", bearerToken);
	    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
	        return bearerToken.substring(7);
	    }
	    return null;
	}

}
