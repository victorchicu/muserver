package muserver.common.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GameServerConfigs extends AbstractConfigs {
 public static Builder builder() {
  return new AutoValue_GameServerConfigs.Builder();
 }

 @JsonCreator
 public static GameServerConfigs create(@JsonProperty("port") Integer port, @JsonProperty("version") String version) {
  return builder()
    .port(port)
    .version(version)
    .build();
 }

 @JsonProperty("port")
 public abstract Integer port();

 @JsonProperty("version")
 public abstract String version();

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder port(Integer port);

  public abstract Builder version(String version);

  public abstract GameServerConfigs build();
 }
}