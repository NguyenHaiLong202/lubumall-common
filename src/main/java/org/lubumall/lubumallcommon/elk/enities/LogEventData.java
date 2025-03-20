package org.lubumall.lubumallcommon.elk.enities;

import lombok.Data;
import org.lubumall.lubumallcommon.elk.enities.base.LogData;
import org.lubumall.lubumallcommon.elk.enums.LogAction;

@Data
public class LogEventData extends LogData {
  private Long userId;
  private Long mainId;
  private Boolean isSystem = false;
  private LogAction action;
  private String objectName;
  private Object preValue;
  private Object value;
}
