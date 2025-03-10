package com.gptai.translation.platform.exceptions.handler;

import com.gptai.translation.common.enums.ExceptionLevel;
import com.gptai.translation.common.resp.JsonResp;
import com.gptai.translation.common.utils.ServletUtil;
import com.gptai.translation.platform.exceptions.resolver.AbstractExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

@Component
public class AsyncRequestTimeoutExceptionHandler extends AbstractExceptionHandler {

    public AsyncRequestTimeoutExceptionHandler() {
        super(HttpStatus.OK);
    }

    @Override
    public boolean support(Exception e) {
        return AsyncRequestTimeoutException.class.isAssignableFrom(e.getClass());
    }

    @Override
    public Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        var message = e.getMessage();
        logger.error(message, e);
        var httpStatus = HttpStatus.REQUEST_TIMEOUT;
        if (ServletUtil.isAjaxRequest(request)) {
            return JsonResp
                    .error(message)
                    .setCode(httpStatus.value())
                    .setExceptionTypeWithTraceId(ExceptionLevel.ERROR);
        } else {
            return errorView(message, httpStatus);
        }
    }

}
