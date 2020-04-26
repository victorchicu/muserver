package mu.server.coreserver.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "core")
public class CoreServerProperties {
    private String host;
    private Integer port;
    private String version;
    private String cliSerial;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCliSerial() {
        return cliSerial;
    }

    public void setCliSerial(String cliSerial) {
        this.cliSerial = cliSerial;
    }
}
