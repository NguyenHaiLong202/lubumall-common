package org.lubumall.lubumallcommon.elk.enities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.lubumall.lubumallcommon.elk.enities.base.LogData;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogExceptionData extends LogData {
  private Integer status;
  private Date timestamp;
  private String message;

  @JsonProperty("message_code")
  private String messageCode;

  private String description;
  private String path;
  private Object params;
  private Object body;
  private Object headers;
}
