package muserver.common.channels;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import muserver.common.objects.CommonConfigs;

public abstract class BaseChannelInitializer extends ChannelInitializer<SocketChannel> {
 private final CommonConfigs configs;

 public BaseChannelInitializer(CommonConfigs configs) {
  this.configs = configs;
 }

 public CommonConfigs configs() {
  return configs;
 }
}