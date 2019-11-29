package muserver.data.server.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dataserver")
@EnableConfigurationProperties(DataServerProperties.class)
public class DataServerProperties {
 private Integer port;

 public void setPort(Integer port) {
  this.port = port;
 }

 public Integer getPort() {
  return port;
 }
}
