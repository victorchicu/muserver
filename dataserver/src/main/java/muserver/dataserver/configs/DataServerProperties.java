package muserver.dataserver.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dataserver")
@EnableConfigurationProperties(DataServerProperties.class)
public class DataServerProperties {
 private String host;
 private Integer port;

 public String getHost() {
  return host;
 }

 public void setHost(String host) {
  this.host = host;
 }

 public Integer getPort() {
  return port;
 }

 public void setPort(Integer port) {
  this.port = port;
 }
}
