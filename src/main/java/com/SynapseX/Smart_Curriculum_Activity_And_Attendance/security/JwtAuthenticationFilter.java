package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.security;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter
 * - reads Bearer token from Authorization header
 * - validates token using JwtUtil
 * - loads UserDetails via CustomUserDetailsService and sets Authentication in SecurityContext
 *
 * Note: keep this class minimal and side-effect free. Logging and error handling are left simple
 * so you can extend to record audit or metrics as needed.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            final String header = request.getHeader(HEADER);
            String token = null;
            String username = null;

            if (header != null && header.startsWith(PREFIX)) {
                token = header.substring(PREFIX.length()).trim();
                if (!token.isEmpty()) {
                    // getUsername will throw if token is invalid; catch below handles it
                    username = jwtUtil.getUsername(token);
                }
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validate(token)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception ex) {
            // don't throw here — just ensure SecurityContext remains empty and request continues (or set response if you prefer)
            // you can log the exception for debugging:
            logger.debug("JWT processing failed: " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Optionally override shouldNotFilter to skip filter for public endpoints (if you prefer).
     * Example: skip for /api/auth/** if you allow unauthenticated access there.
     *
     * Uncomment and adapt if you'd rather short-circuit here instead of configuring in SecurityConfig.
     */
    // @Override
    // protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    //     String path = request.getRequestURI();
    //     return path.startsWith("/api/auth") || path.startsWith("/actuator");
    // }
}