package org.lubumall.lubumallcommon.exceptions;

import static org.springframework.http.HttpStatus.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.lubumall.lubumallcommon.utils.SendMessageTelegram;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalHandleException extends ResponseEntityExceptionHandler {

  private final SendMessageTelegram sendMessageTelegram;

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> globalExceptionHandler(
      Exception ex, HttpServletRequest request) {
    sendNotification(ex, request);
    if (ex instanceof ExceptionLb exceptionLb) {
      ExceptionResponse exceptionResponse =
          new ExceptionResponse(
              exceptionLb.status,
              new Date(),
              exceptionLb.getMessage(),
              exceptionLb.messageCode,
              exceptionLb.getMessage(),
              request.getServletPath());
      return new ResponseEntity<>(exceptionResponse, exceptionLb.status);
    }
    ExceptionResponse exceptionResponse = new ExceptionResponse();
    if (ex.getClass().getName().contains("InvalidDataAccessApiUsageException")) {
      exceptionResponse.setStatus(BAD_REQUEST.value());
      exceptionResponse.setMessage("Invalid argument");
      exceptionResponse.setMessageCode(BAD_REQUEST.toString());

      exceptionResponse.setPath(request.getServletPath());
    } else {
      exceptionResponse =
          new ExceptionResponse(
              INTERNAL_SERVER_ERROR,
              new Date(),
              "Đã có lỗi xảy ra.",
              "INTERNAL_SERVER_ERROR",
              "Đã có lỗi xảy ra.",
              request.getServletPath());
    }

    return new ResponseEntity<>(
        exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
  }


  private void sendNotification(Exception ex, HttpServletRequest request) {
    try {
      String logLevel = "ERROR";
      StringBuilder msg = new StringBuilder();
      if (ex instanceof ExceptionLb exceptionLb) {
        if (exceptionLb.status.value() < 500) {
          return;
        } else {
          msg.append("<b>ERROR</b> : ")
              .append(ex.getMessage())
              .append(" (")
              .append(exceptionLb.status.value())
              .append(") \n");
        }
      } else {
        msg.append("<b>ERROR</b> : ").append(ex.getMessage()).append(" \n");
      }
      try {
        msg.append("<b>METHOD</b> : ").append(request.getMethod()).append(" \n");
      } catch (Exception ignored) {
      }
      try {
        msg.append("<b>URI</b> : ").append(request.getRequestURL().toString()).append(" \n");
      } catch (Exception ignored) {
      }
      try {
        msg.append("<b>BODY</b> : ").append(getBody(request));
      } catch (Exception ignored) {
      }
      sendMessageTelegram.send(msg.toString());
    } catch (Exception ignore) {
    }
  }

  public static String getBody(HttpServletRequest request) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    try (InputStream inputStream = request.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
      char[] charBuffer = new char[128];
      int bytesRead;
      while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
        stringBuilder.append(charBuffer, 0, bytesRead);
      }
    }
    return stringBuilder.toString();
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      org.springframework.http.HttpStatusCode status,
      WebRequest request) {
    HttpServletRequest httpRequest =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    sendNotification(ex, httpRequest);
    Error validDetails = new Error();
    Map<String, String> message = new HashMap<>();

    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    for (FieldError fieldError : fieldErrors) {
      message.put(fieldError.getField(), fieldError.getDefaultMessage());
    }

    validDetails.setMessage(message);
    validDetails.setStatus(status.value());
    validDetails.setTimestamp(new Date());
    validDetails.setError("Not valid exception");
    validDetails.setPath(httpRequest.getServletPath());

    return new ResponseEntity<>(validDetails, headers, status);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      org.springframework.http.HttpStatusCode status,
      WebRequest request) {
    HttpServletRequest httpRequest =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    sendNotification(ex, httpRequest);

    ExceptionResponse exceptionResponse =
        new ExceptionResponse(
            (HttpStatus) status,
            new Date(),
            "Malformed JSON request",
            "MALFORMED_JSON_REQUEST",
            ex.getLocalizedMessage(),
            httpRequest.getServletPath());

    return new ResponseEntity<>(exceptionResponse, headers, status);
  }
}
