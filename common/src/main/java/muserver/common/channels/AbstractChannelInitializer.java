package muserver.common.channels;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import muserver.common.objects.AbstractConfigs;

public abstract class AbstractChannelInitializer extends ChannelInitializer<SocketChannel> {
 private final AbstractConfigs configs;

 public AbstractChannelInitializer(AbstractConfigs configs) {
  this.configs = configs;
 }

 public AbstractConfigs configs() {
  return configs;
 }
}
