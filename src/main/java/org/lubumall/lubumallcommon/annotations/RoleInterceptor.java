package org.lubumall.lubumallcommon.annotations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(CheckRole.class)) {
                CheckRole checkRole = method.getAnnotation(CheckRole.class);
                List<String> requiredRoles = Arrays.asList(checkRole.value());

                List<String> userRoles = getUserRolesFromRequest(request);

                boolean hasPermission = userRoles.stream().anyMatch(requiredRoles::contains);
                if (!hasPermission) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Bạn không có quyền truy cập!");
                    return false;
                }
            }
        }
        return true;
    }

    private List<String> getUserRolesFromRequest(HttpServletRequest request) {
        String rolesHeader = request.getHeader("x-lbm-userrole");
        if (rolesHeader == null || rolesHeader.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(rolesHeader.split(","));
    }
}
