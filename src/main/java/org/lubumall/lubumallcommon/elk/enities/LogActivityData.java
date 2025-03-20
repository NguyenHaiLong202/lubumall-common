package org.lubumall.lubumallcommon.elk.enities;

import java.util.Map;
import lombok.Data;
import org.lubumall.lubumallcommon.elk.enities.base.LogData;
import org.lubumall.lubumallcommon.elk.enums.LogAction;

@Data
public class LogActivityData extends LogData {
  private Long userId;
  private Long mainId;
  private Boolean isSystem = false;
  private LogAction action;
  private Object relatedObject;
  //  private String ipAddress;
  //  private String device;
  //  private String location;
  private Object performedBy;
  private Map<String, Object> extraFields;
}
