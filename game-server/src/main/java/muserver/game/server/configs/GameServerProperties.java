package muserver.game.server.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gameserver")
@EnableConfigurationProperties(GameServerProperties.class)
public class GameServerProperties {
 private String name;
 private Integer port;
 private String version, serial;

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public Integer getPort() {
  return port;
 }

 public void setPort(Integer port) {
  this.port = port;
 }

 public String getVersion() {
  return version;
 }

 public void setVersion(String version) {
  this.version = version;
 }

 public String getSerial() {
  return serial;
 }

 public void setSerial(String serial) {
  this.serial = serial;
 }
}
