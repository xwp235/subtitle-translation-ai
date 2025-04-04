package com.gptai.translation.platform.exceptions.handler;

import com.gptai.translation.common.resp.JsonResp;
import com.gptai.translation.common.utils.ServletUtil;
import com.gptai.translation.common.utils.SpringUtil;
import com.gptai.translation.platform.exceptions.resolver.AbstractExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Component
public class NoResourceFoundExceptionHandler extends AbstractExceptionHandler {

    @Override
    public boolean support(Exception e) {
        return NoResourceFoundException.class.isAssignableFrom(e.getClass());
    }

    public NoResourceFoundExceptionHandler() {
        super(HttpStatus.OK);
    }

    @Override
    public Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        var message = SpringUtil.getMessage("error_resource_not_found");
        var httpStatus = HttpStatus.NOT_FOUND;
        if (ServletUtil.isAjaxRequest(request)) {
            return JsonResp
                    .error(message)
                    .setCode(httpStatus.value());
        } else {
            return redirect404View();
        }
    }

}
