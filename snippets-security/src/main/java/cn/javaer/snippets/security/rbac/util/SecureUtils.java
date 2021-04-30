package cn.javaer.snippets.security.rbac.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * @author cn-src
 */
public interface SecureUtils {

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

    /**
     * Jwt 生成.
     *
     * @param claims the claims
     * @param secret the secret
     *
     * @return Jwt
     */
    static String jwtGenerator(final JWTClaimsSet claims, final String secret) {
        final SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        try {
            signedJWT.sign(new MACSigner(secret));
            return signedJWT.serialize();
        }
        catch (final JOSEException e) {
            throw new IllegalArgumentException("Invalid secret", e);
        }
    }
}
