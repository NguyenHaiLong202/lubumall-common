package org.lubumall.lubumallcommon.elk.enities.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;
import org.lubumall.lubumallcommon.elk.enums.LogType;

@Data
public class LogData {
  protected LogType type;

  @JsonProperty("created_at")
  protected LocalDateTime createdAt;
}
