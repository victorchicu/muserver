package mu.server.authenticator.channels.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import mu.server.authenticator.channels.handlers.processors.ServerListProcessor;
import mu.server.authenticator.channels.handlers.processors.base.BasePacketProcessor;
import mu.server.authenticator.channels.handlers.processors.AcceptClientProcessor;
import mu.server.authenticator.channels.handlers.processors.ServerConnectProcessor;
import mu.server.authenticator.properties.AuthProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuthServerChannelInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Integer UNSIGNED_BYTE_MAX_VALUE = Byte.MAX_VALUE * 2 + 1;
    private static final Integer UNSIGNED_SHORT_MAX_VALUE = Short.MAX_VALUE * 2 + 1;
    private static final Logger logger = LoggerFactory.getLogger(AuthServerChannelInboundHandler.class);
    private static final Map<Integer, BasePacketProcessor> processors = new HashMap<>();

    public AuthServerChannelInboundHandler(AuthProperties props) {
        processors.put(0xF403, new ServerConnectProcessor(props));
        processors.put(0xF406, new ServerListProcessor(props));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (ctx.channel().remoteAddress() != null) {
            logger.info("Accepted a client connection from remote address: {}", ctx.channel().remoteAddress().toString());
        }
        new AcceptClientProcessor().execute(ctx, Unpooled.directBuffer(4));
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

        BasePacketProcessor packetProcessor = processors.get(opCode);

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
