package cn.javaer.snippets.security.web.authentication;

import cn.javaer.snippets.security.util.SecureUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Duration;
import java.util.function.BiConsumer;

/**
 * @author cn-src
 */
public class SecurityConfigurator {
    private final HttpSecurity http;

    private SecurityConfigurator(final HttpSecurity http) {
        this.http = http;
    }

    public SecurityConfigurator staticResourceAntdVPro() throws Exception {
        this.http.authorizeRequests().antMatchers(
            "/public/**", "/webjars/**", "/favicon.*", "/*/icon-*",
            "/*.html", "/*.jpg", "/*.png", "/assets/**", "/js/**", "/css/**")
            .permitAll();
        return this;
    }

    public SecurityConfigurator jwtLogin(final String secret, final Duration expirationDuration,
                                         final BiConsumer<UserDetails, JWTClaimsSet.Builder> handler) throws Exception {
        final JwtDecoder jwtDecoder = SecureUtils.jwtDecoder(secret);
        this.http.oauth2ResourceServer().jwt().decoder(jwtDecoder);
        this.http.formLogin().successHandler(
            new JwtAuthenticationSuccessHandler(secret, expirationDuration, handler));
        this.http.formLogin().failureHandler(new RestAuthenticationFailureHandler());
        this.http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return this;
    }

    public SecurityConfigurator userDetailsService(final UserDetailsService userDetailsService) throws Exception {
        this.http.userDetailsService(userDetailsService);
        return this;
    }

    public SecurityConfigurator anyRequestAuthenticated() throws Exception {
        this.http.authorizeRequests().anyRequest().authenticated();
        return this;
    }

    public SecurityConfigurator httpBasic() throws Exception {
        this.http.httpBasic();
        return this;
    }

    public SecurityConfigurator csrfDisable() throws Exception {
        this.http.csrf().disable();
        return this;
    }

    public static SecurityConfigurator on(final HttpSecurity http) {
        return new SecurityConfigurator(http);
    }
}