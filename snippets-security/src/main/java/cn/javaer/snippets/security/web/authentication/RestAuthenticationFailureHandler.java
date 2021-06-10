package cn.javaer.snippets.security.web.authentication;

import cn.javaer.snippets.jackson.Json;
import cn.javaer.snippets.spring.exception.DefinedErrorInfo;
import cn.javaer.snippets.spring.exception.ErrorMappings;
import cn.javaer.snippets.spring.exception.ErrorMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author cn-src
 */
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final AuthenticationException exception) throws IOException {
        this.logger.debug(exception.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        final DefinedErrorInfo errorInfo =
            ErrorMappings.getErrorInfo(exception.getClass().getName(),
                DefinedErrorInfo.of("LOGIN_ERROR", HttpServletResponse.SC_UNAUTHORIZED));

        final String json = Json.DEFAULT.write(ErrorMessageSource.replaceMessage(errorInfo));
        response.getWriter().write(json);
    }
}