package muserver.common.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import java.util.Map;

@AutoValue
public abstract class ConnectorServerConfigs extends AbstractConfigs {
 @JsonCreator
 public static ConnectorServerConfigs create(
  @JsonProperty("port") Integer port,
  @JsonProperty("servers") Map<Integer, Server> servers
 ) {
  return builder()
   .port(port)
   .servers(servers)
   .build();
 }

 private static Builder builder() {
  return new AutoValue_ConnectorServerConfigs.Builder();
 }

 @JsonProperty("port")
 public abstract Integer port();

 @JsonProperty("servers")
 public abstract Map<Integer, Server> servers();

 @AutoValue
 public static abstract class Server {
  @JsonCreator
  public static Server create(
   @JsonProperty("ip") String ip,
   @JsonProperty("port") Integer port,
   @JsonProperty("name") String name,
   @JsonProperty("visible") Boolean visible
  ) {
   return builder()
    .ip(ip)
    .port(port)
    .name(name)
    .visible(visible)
    .build();
  }

  static Builder builder() {
   return new AutoValue_ConnectorConfigs_Server.Builder();
  }

  @JsonProperty("ip")
  public abstract String ip();

  @JsonProperty("port")
  public abstract Integer port();

  @JsonProperty("name")
  public abstract String name();

  @JsonProperty("visible")
  public abstract Boolean visible();

  @AutoValue.Builder
  public abstract static class Builder {
   public abstract Builder ip(String ip);

   public abstract Builder port(Integer port);

   public abstract Builder name(String name);

   public abstract Builder visible(Boolean visible);

   public abstract Server build();
  }
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder port(Integer port);

  public abstract Builder servers(Map<Integer, Server> servers);

  public abstract ConnectorServerConfigs build();
 }
}