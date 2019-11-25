package muserver.common.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GameServerConfigs extends CommonConfigs {
 public static Builder builder() {
  return new AutoValue_GameServerConfigs.Builder();
 }

 @JsonProperty("name")
 public abstract String name();

 @JsonProperty("port")
 public abstract Integer port();

 @JsonProperty("version")
 public abstract String version();

 @JsonProperty("serial")
 public abstract String serial();

 @JsonCreator
 public static GameServerConfigs create(
   @JsonProperty("name") String name,
   @JsonProperty("port") Integer port,
   @JsonProperty("version") String version,
   @JsonProperty("serial") String serial
 ) {
  return builder()
    .name(name)
    .port(port)
    .version(version)
    .serial(serial)
    .build();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder port(Integer port);

  public abstract Builder version(String version);

  public abstract Builder name(String name);

  public abstract Builder serial(String serial);

  public abstract GameServerConfigs build();
 }
}