package org.lubumall.lubumallcommon.elk.enities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.lubumall.lubumallcommon.elk.enities.base.LogData;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogRequestData extends LogData {
  private String path;
  private String method;
  private Object params;
  private Object header;
  private Object body;
  private Object response;
  private HttpStatus status;

  @JsonProperty("int_status")
  private Integer intStatus;

  @JsonProperty("time_taken")
  private Long timeTaken;

  @JsonProperty("user_id")
  private Long userId;
}
