package org.lubumall.lubumallcommon.annotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.lubumall.lubumallcommon.exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
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
          response.setContentType("application/json");
          response.setCharacterEncoding("UTF-8");

          ExceptionResponse errorResponse =
              new ExceptionResponse(
                  HttpStatus.FORBIDDEN,
                  new Date(System.currentTimeMillis()),
                  "Không có quyền truy cập!",
                  "403",
                  "Không có quyền thực hiện hành động này",
                  request.getRequestURI());

          response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
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
