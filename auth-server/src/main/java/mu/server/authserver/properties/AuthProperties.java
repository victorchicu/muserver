package mu.server.authserver.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "authserver")
public class AuthProperties {
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
