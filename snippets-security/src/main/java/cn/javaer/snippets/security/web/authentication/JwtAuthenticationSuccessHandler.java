package cn.javaer.snippets.security.web.authentication;

import cn.javaer.snippets.security.util.SecureUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author cn-src
 */
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final String secret;

    private final Duration expirationDuration;

    private final BiConsumer<UserDetails, JWTClaimsSet.Builder> handler;

    public JwtAuthenticationSuccessHandler(final String secret, final Duration expirationDuration,
                                           final BiConsumer<UserDetails, JWTClaimsSet.Builder> handler) {
        this.secret = Objects.requireNonNull(secret);
        this.expirationDuration = Objects.requireNonNull(expirationDuration);
        this.handler = handler;
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        final UsernamePasswordAuthenticationToken upToken =
            (UsernamePasswordAuthenticationToken) authentication;
        final UserDetails principal = (UserDetails) upToken.getPrincipal();
        final Date exp = Date.from(LocalDateTime.now().plus(this.expirationDuration)
            .atZone(ZoneId.systemDefault()).toInstant());
        final String jwtToken = SecureUtils.generateJwtToken(principal, exp, this.secret,
            this.handler);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"token\":\"" + jwtToken + "\"}");
        SecurityContextHolder.clearContext();
    }
}