package cn.javaer.snippets.security.rbac.util;

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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
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
     *
     * @return Jwt
     */
    static Jwt toJwt(final UserDetails userDetails, final Date exp, final String secret) {
        Collection<String> scp = Collections.emptyList();
        if (userDetails.getAuthorities() != null && !userDetails.getAuthorities().isEmpty()) {
            scp = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        }
        final JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .claim("scp", scp)
            .subject(userDetails.getUsername())
            .issuer("local")
            .expirationTime(exp)
            .build();
        final SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        final String token;
        try {
            signedJWT.sign(new MACSigner(secret));
            token = signedJWT.serialize();
        }
        catch (final JOSEException e) {
            throw new IllegalArgumentException("Invalid secret", e);
        }
        final Map<String, Object> headers =
            new LinkedHashMap<>(signedJWT.getHeader().toJSONObject());
        final Map<String, Object> claims = claimSetConverter.convert(claimsSet.getClaims());

        return Jwt.withTokenValue(token)
            .headers((h) -> h.putAll(headers))
            .claims((c) -> c.putAll(Objects.requireNonNull(claims)))
            .build();
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