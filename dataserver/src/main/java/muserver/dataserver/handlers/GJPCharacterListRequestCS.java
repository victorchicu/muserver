package muserver.dataserver.handlers;

import base.BasePacketHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.dataserver.configs.DataServerProperties;

public class GJPCharacterListRequestCS extends BasePacketHandler {
 private final DataServerProperties props;

 public GJPCharacterListRequestCS(DataServerProperties props) {
  this.props = props;
 }

 @Override
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {


  super.send(ctx, byteBuf);
 }
}
