package muserver.common.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import java.util.Map;

@AutoValue
public abstract class ConnectServerConfigs extends CommonConfigs {
 @JsonCreator
 public static ConnectServerConfigs create(
   @JsonProperty("port") Integer port,
   @JsonProperty("servers") Map<Integer, Server> servers
 ) {
  return builder()
    .port(port)
    .servers(servers)
    .build();
 }

 private static Builder builder() {
  return new AutoValue_ConnectServerConfigs.Builder();
 }

 @JsonProperty("port")
 public abstract Integer port();

 @JsonProperty("servers")
 public abstract Map<Integer, Server> servers();

 @AutoValue
 public static abstract class Server {
  static Builder builder() {
   return new AutoValue_ConnectServerConfigs_Server.Builder();
  }

  @JsonCreator
  public static Server create(
    @JsonProperty("ip") String ip,
    @JsonProperty("port") Integer port
  ) {
   return builder()
     .ip(ip)
     .port(port)
     .build();
  }

  @JsonProperty("ip")
  public abstract String ip();

  @JsonProperty("port")
  public abstract Integer port();

  @AutoValue.Builder
  public abstract static class Builder {
   public abstract Builder ip(String ip);

   public abstract Builder port(Integer port);

   public abstract Server build();
  }
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder port(Integer port);

  public abstract Builder servers(Map<Integer, Server> servers);

  public abstract ConnectServerConfigs build();
 }
}