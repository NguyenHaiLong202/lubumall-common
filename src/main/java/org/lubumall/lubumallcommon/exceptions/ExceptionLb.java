package org.lubumall.lubumallcommon.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionLb extends RuntimeException {
  HttpStatus status;
  String messageCode;

  public ExceptionLb(HttpStatus status, BaseErrorMessage msg) {
    super(msg.val());
    this.status = status;
    this.messageCode = msg.toString();
  }

  public ExceptionLb(HttpStatus status, BaseErrorMessage msg, String data) {
    super(msg.val() + "(" + data + ")");
    this.status = status;
    this.messageCode = msg.toString();
  }

  public ExceptionLb(HttpStatus status, String msg) {
    super(msg);
    this.status = status;
  }
}
