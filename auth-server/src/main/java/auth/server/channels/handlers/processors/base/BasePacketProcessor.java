package auth.server.channels.handlers.processors.base;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class BasePacketProcessor {
 public abstract void execute(ChannelHandlerContext ctx, ByteBuf byteBuf);
}