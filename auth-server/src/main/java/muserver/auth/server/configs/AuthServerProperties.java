package muserver.auth.server.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "authserver")
@EnableConfigurationProperties(AuthServerProperties.class)
public class AuthServerProperties {
 private Integer port;
 private Map<Integer, Server> servers;

 public Integer getPort() {
  return port;
 }

 public void setPort(Integer port) {
  this.port = port;
 }

 public Map<Integer, Server> getServers() {
  return servers;
 }

 public void setServers(Map<Integer, Server> servers) {
  this.servers = servers;
 }

 public static class Server {
  private String ip;
  private Integer port;

  public String getIp() {
   return ip;
  }

  public void setIp(String ip) {
   this.ip = ip;
  }

  public Integer getPort() {
   return port;
  }

  public void setPort(Integer port) {
   this.port = port;
  }
 }
}
