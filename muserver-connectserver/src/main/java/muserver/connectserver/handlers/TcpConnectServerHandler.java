package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.Globals;
import muserver.common.configs.ServerConfigs;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.messages.PBMSG_HEAD2;
import muserver.common.messages.PWMSG_HEAD2;
import muserver.common.types.ServerType;
import muserver.common.utils.HexUtils;
import muserver.connectserver.contexts.ConnectServerContext;
import muserver.connectserver.exceptions.TcpConnectServerHandlerException;
import muserver.connectserver.messages.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TcpConnectServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerHandler.class);

 private final ConnectServerContext connectServerContext;

 public TcpConnectServerHandler(ConnectServerContext connectServerContext) {
  this.connectServerContext = connectServerContext;
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Connection accepted: {}", ctx.channel().remoteAddress().toString());
  }

  PMSG_HANDSHAKE handshake = PMSG_HANDSHAKE.create(PBMSG_HEAD.create(Globals.PMHC_BYTE, (byte) PMSG_HANDSHAKE.sizeOf(), (byte) 0), (byte) 1);

  byte[] buffer = handshake.serialize(new ByteArrayOutputStream());

  ctx.writeAndFlush(Unpooled.wrappedBuffer(buffer));
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Connection interrupted: {}", ctx.channel().remoteAddress().toString());
  }
 }

 @Override
 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
  logger.error(cause.getMessage(), cause);
  ctx.close();
 }

 @Override
 public void channelReadComplete(ChannelHandlerContext ctx) {
  ctx.flush();
 }

 @Override
 protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
  handleProtocol(ctx, byteBuf.array(), byteBuf.getUnsignedByte(0), byteBuf.getUnsignedByte(1), byteBuf.getUnsignedByte(2), byteBuf.getUnsignedByte(3));
 }


 private void handleProtocol(ChannelHandlerContext ctx, byte[] byteBuf, short type, short size, short headCode, short subCode) throws Exception {
  if (type != Globals.PMHC_BYTE) {
   throw new TcpConnectServerHandlerException(String.format("Invalid protocol type: %d", type));
  }

  if (size != 4) {
   throw new TcpConnectServerHandlerException(String.format("Invalid buffer length: %d", size));
  }

//  0000   c1 04 f4 06
//  0000   c2 00 0b f4 06 00 01 00 00 00 cc                  ...........

//  0000   c1 06 f4 03 00 00                                 ......
//  0000   c1 16 f4 03 31 39 32 2e 31 36 38 2e 31 2e 31 33   ....192.168.1.13
//  0010   39 00 00 00 5d da                                 9...].


  switch (headCode) {
   case 0xF4: {
    switch (subCode) {
     case 3: {
      PMSG_REQ_SERVER_INFO requestServerInfo = PMSG_REQ_SERVER_INFO.deserialize(new ByteArrayInputStream(byteBuf));

      ServerConfigs serverConfigs = this.connectServerContext.serversConfigsMap().getOrDefault(requestServerInfo.serverCode().shortValue(), null);

      if (serverConfigs == null) {
       throw new TcpConnectServerHandlerException( String.format("Server id: %d mismatch configuration", requestServerInfo.serverCode()));
      }

      PMSG_RESP_SERVER_INFO responseServerInfo = PMSG_RESP_SERVER_INFO.create(
          PBMSG_HEAD2.create(Globals.PMHC_BYTE, (byte) PMSG_RESP_SERVER_INFO.sizeOf(), requestServerInfo.header().headCode(), requestServerInfo.header().subCode()),
          serverConfigs.ip(),
          serverConfigs.port().shortValue()
      );

      ctx.writeAndFlush(Unpooled.wrappedBuffer(responseServerInfo.serialize(new ByteArrayOutputStream())));
     }
     break;

     case 6: {
      PBMSG_HEAD2 header = PBMSG_HEAD2.deserialize(new ByteArrayInputStream(byteBuf));

      List<PMSG_SERVER> servers = new ArrayList<>();

      for (ServerConfigs serverConfigs : this.connectServerContext.serversConfigsMap().values()) {
       if (serverConfigs.type() == ServerType.VISIBLE) {
        //todo: Request players count from GS
        servers.add(PMSG_SERVER.create(serverConfigs.id(), (byte) 1, (byte) 0xCC));
       }
      }

      short sizeOf = (short) (PMSG_SERVERLIST.sizeOf() + (servers.size() * PMSG_SERVER.sizeOf()));

      PMSG_SERVERLIST serverList = PMSG_SERVERLIST.create(
          PWMSG_HEAD2.create(Globals.PMHC_WORD, sizeOf, header.headCode(), header.subCode()),
          (short) servers.size(),
          servers
      );

      ctx.writeAndFlush(Unpooled.wrappedBuffer(serverList.serialize(new ByteArrayOutputStream())));
     }
     break;

     default: {
      throw new TcpConnectServerHandlerException(String.format("Unsupported subcode: %d", subCode));
     }
    }
   }
   break;

   default: {
    throw new TcpConnectServerHandlerException(String.format("Unsupported headcode: %d", headCode));
   }
  }
 }
}