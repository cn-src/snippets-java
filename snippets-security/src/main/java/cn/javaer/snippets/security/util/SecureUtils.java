package cn.javaer.snippets.security.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author cn-src
 */
public interface SecureUtils {
    Converter<Map<String, Object>, Map<String, Object>> claimSetConverter =
        MappedJwtClaimSetConverter
            .withDefaults(Collections.emptyMap());

    /**
     * UserDetails 转换 成 Jwt.
     *
     * @param userDetails userDetails
     * @param exp 到期时间
     * @param secret 密钥
     * @param handler handler
     *
     * @return Jwt
     */
    static String generateJwtToken(final UserDetails userDetails, final Date exp,
                                   final String secret,
                                   final BiConsumer<UserDetails, JWTClaimsSet.Builder> handler) {
        Collection<String> scp = Collections.emptyList();
        if (userDetails.getAuthorities() != null && !userDetails.getAuthorities().isEmpty()) {
            scp = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        }
        final JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
            .claim("scp", scp)
            .subject(userDetails.getUsername())
            .issuer("local")
            .expirationTime(exp);
        if (null != handler) {
            handler.accept(userDetails, builder);
        }
        final SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256),
            builder.build());
        try {
            signedJWT.sign(new MACSigner(secret));
            return signedJWT.serialize();
        }
        catch (final JOSEException e) {
            throw new IllegalArgumentException("Invalid secret", e);
        }
    }

    /**
     * 创建 Jwt 解码器.
     *
     * @param secret the secret
     *
     * @return Jwt 解码器
     */
    static JwtDecoder jwtDecoder(final String secret) {
        try {
            return NimbusJwtDecoder.withSecretKey(new MACVerifier(secret)
                .getSecretKey()).build();
        }
        catch (final JOSEException e) {
            throw new IllegalArgumentException("Invalid secret", e);
        }
    }
}