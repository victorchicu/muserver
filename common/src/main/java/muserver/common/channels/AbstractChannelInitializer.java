package muserver.common.channels;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

public abstract class AbstractChannelInitializer extends ChannelInitializer<SocketChannel> {
 private final Map<String, Object> props;

 public AbstractChannelInitializer(Map<String, Object> props) {
  this.props = props;
 }

 public Map<String, Object> props() {
  return props;
 }
}
