package muserver.dataserver.components;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dataserver")
@EnableConfigurationProperties(DataserverProperties.class)
public class DataserverProperties {
 private Integer port;

 public void setPort(Integer port) {
  this.port = port;
 }

 public Integer getPort() {
  return port;
 }
}
