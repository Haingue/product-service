package com.groupeun.product.infrastructure.configuration;

import com.groupeun.product.domain.exception.DomainException;
import com.groupeun.product.domain.exception.ProductNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestResponseStatusExceptionResolver extends AbstractHandlerExceptionResolver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof ProductNotFound) {
                return handleIllegalArgument((ProductNotFound) ex, response, HttpServletResponse.SC_NO_CONTENT);
            } else if (ex instanceof DomainException) {
                return handleIllegalArgument((DomainException) ex, response, HttpServletResponse.SC_BAD_REQUEST);
            }
            return handleIllegalArgument(new DomainException(ex.getMessage()), response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception handlerException) {
            logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
        }
        return null;
    }
    private ModelAndView handleIllegalArgument(DomainException ex, HttpServletResponse response, int httpCode) throws IOException {
        // response.setHeader("Error", ex.getMessage());
        logger.warn("Handling of [{}] resulted in Exception, details: {}", ex.getClass().getName(), ex.getMessage());
        response.sendError(httpCode, ex.getMessage());
        return new ModelAndView();
    }
}
