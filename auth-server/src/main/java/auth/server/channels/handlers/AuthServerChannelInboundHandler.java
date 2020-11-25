package auth.server.channels.handlers;

import auth.server.channels.handlers.processors.AcceptClientPacketProcessor;
import auth.server.channels.handlers.processors.ServerConnectPacketProcessor;
import auth.server.channels.handlers.processors.base.BasePacketProcessor;
import auth.server.properties.AuthServerProperties;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import auth.server.channels.handlers.processors.ServerListPacketProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AuthServerChannelInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Integer UNSIGNED_BYTE_MAX_VALUE = Byte.MAX_VALUE * 2 + 1;
    private static final Integer UNSIGNED_SHORT_MAX_VALUE = Short.MAX_VALUE * 2 + 1;
    private static final Logger logger = LoggerFactory.getLogger(AuthServerChannelInboundHandler.class);
    private static final Map<Integer, BasePacketProcessor> packetProcessors = new HashMap<>();

    public AuthServerChannelInboundHandler(AuthServerProperties props) {
        packetProcessors.put(0xF403, new ServerConnectPacketProcessor(props));
        packetProcessors.put(0xF406, new ServerListPacketProcessor(props));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (ctx.channel().remoteAddress() != null) {
            logger.info("Accepted a client connection from remote address: {}", ctx.channel().remoteAddress().toString());
        }
        new AcceptClientPacketProcessor().execute(ctx, Unpooled.directBuffer(4));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (ctx.channel().remoteAddress() != null) {
            logger.info("Client from remote address: {} has interrupted connection", ctx.channel().remoteAddress().toString());
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
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        logger.info("RECEIVED PACKET\n{}", ByteBufUtil.prettyHexDump(byteBuf));

        short type = byteBuf.readUnsignedByte();

        switch (type) {
            case 0xC1:
            case 0xC3: {
                short size = byteBuf.readUnsignedByte();
                if (size <= 0 || size > UNSIGNED_BYTE_MAX_VALUE) {
                    ctx.close();
                    if (ctx.channel().remoteAddress() != null) {
                        logger.warn("Invalid protocol size: {} | from remote address: {}", size, ctx.channel().remoteAddress().toString());
                    }
                    return;
                }
            }
            break;
            case 0xC2:
            case 0xC4: {
                int size = byteBuf.readUnsignedShort();

                if (size <= 0 || size > UNSIGNED_SHORT_MAX_VALUE) {
                    ctx.close();
                    if (ctx.channel().remoteAddress() != null) {
                        logger.warn("Invalid protocol size: {} | from remote address: {}", size, ctx.channel().remoteAddress().toString());
                    }
                    return;
                }
            }
            break;
            default: {
                ctx.close();
                if (ctx.channel().remoteAddress() != null) {
                    logger.warn("Invalid protocol type: {} | from remote address: {}", type, ctx.channel().remoteAddress().toString());
                }
                return;
            }
        }

        int opCode = byteBuf.readUnsignedShort();

        BasePacketProcessor packetProcessor = packetProcessors.get(opCode);

        if (packetProcessor == null) {
            ctx.close();
            if (ctx.channel().remoteAddress() != null) {
                logger.warn("Invalid protocol number: {} | from remote address: {}", opCode, ctx.channel().remoteAddress().toString());
            }
            return;
        }

        packetProcessor.execute(ctx, byteBuf);
    }
}
