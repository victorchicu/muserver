package muserver.common.channels;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import muserver.common.objects.IConfigs;

import java.util.Map;

public abstract class AbstractChannelInitializer extends ChannelInitializer<SocketChannel> {
 private final IConfigs configs;

 public AbstractChannelInitializer(IConfigs configs) {
  this.configs = configs;
 }

 public IConfigs configs() {
  return configs;
 }
}
