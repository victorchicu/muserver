package muserver.common.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DataServerConfigs extends AbstractConfigs {
 @JsonProperty("port")
 public abstract Integer port();

 @JsonCreator
 public static DataServerConfigs create(@JsonProperty("port") Integer port) {
  return builder()
    .port(port)
    .build();
 }

 public static Builder builder() {
  return new AutoValue_DataServerConfigs.Builder();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder port(Integer port);

  public abstract DataServerConfigs build();
 }
}
