package org.lubumall.lubumallcommon.exceptions;

import java.util.Date;
import java.util.Map;
import lombok.Data;

@Data
public class Error {
  private Integer status;
  private Map<String, String> message;
  private String error;
  private String path;
  private Date timestamp;
}