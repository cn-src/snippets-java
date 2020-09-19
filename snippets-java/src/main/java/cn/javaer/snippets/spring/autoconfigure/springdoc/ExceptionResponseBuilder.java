package cn.javaer.snippets.spring.autoconfigure.springdoc;

import cn.javaer.snippets.spring.web.exception.DefinedErrorInfo;
import cn.javaer.snippets.spring.web.exception.ErrorInfoExtractor;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.GenericResponseBuilder;
import org.springdoc.core.MethodAttributes;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.PropertyResolverUtils;
import org.springdoc.core.ReturnTypeParser;
import org.springdoc.core.SpringDocConfigProperties;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

/**
 * @author cn-src
 */
class ExceptionResponseBuilder extends GenericResponseBuilder {

    private final ErrorInfoExtractor errorInfoExtractor;

    /**
     * Instantiates a new Generic response builder.
     *
     * @param operationBuilder the operation builder
     * @param returnTypeParsers the return type parsers
     * @param springDocConfigProperties the spring doc config properties
     * @param propertyResolverUtils the property resolver utils
     * @param errorInfoExtractor errorInfoExtractor
     */
    public ExceptionResponseBuilder(final OperationBuilder operationBuilder,
                                    final List<ReturnTypeParser> returnTypeParsers,
                                    final SpringDocConfigProperties springDocConfigProperties,
                                    final PropertyResolverUtils propertyResolverUtils,
                                    final ErrorInfoExtractor errorInfoExtractor) {
        super(operationBuilder, returnTypeParsers, springDocConfigProperties,
            propertyResolverUtils);
        this.errorInfoExtractor = errorInfoExtractor;
    }

    @Override
    public ApiResponses build(final Components components, final HandlerMethod handlerMethod,
                              final Operation operation, final MethodAttributes methodAttributes) {
        final ApiResponses apiResponses = super.build(components, handlerMethod, operation,
            methodAttributes);
        final Class<?>[] exceptionTypes = handlerMethod.getMethod().getExceptionTypes();
        for (final Class<?> exceptionType : exceptionTypes) {
            @SuppressWarnings("unchecked")
            final DefinedErrorInfo errorInfo = this.errorInfoExtractor.extract((Class<?
                extends Throwable>) exceptionType, true);
            if (errorInfo != null) {
                final ApiResponse response = new ApiResponse();
                response.setDescription(String.format("status:%s, %s", errorInfo.getStatus(),
                    errorInfo.getMessage()));
                apiResponses.addApiResponse(errorInfo.getError(), response);
            }
        }
        return apiResponses;
    }
}
