package org.lubumall.lubumallcommon.elk.enities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.lubumall.lubumallcommon.elk.enities.base.LogData;
import org.lubumall.lubumallcommon.elk.enums.LogAction;
import org.lubumall.lubumallcommon.elk.enums.LogStatus;

@Data
public class LogObjectData extends LogData {
  @JsonProperty("user_id")
  private Long userId;

  private Integer version;

  @JsonProperty("object_name")
  private String objectName;

  @JsonProperty("object_id")
  private Long objectId;

  private LogAction action;
  private Object data;
  private LogStatus status;
}
