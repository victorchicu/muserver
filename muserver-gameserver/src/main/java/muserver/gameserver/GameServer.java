package muserver.gameserver;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import muserver.common.IServer;
import muserver.common.configs.CommonConfigs;
import muserver.common.logging.LoggingLevel;
import muserver.common.types.AppenderType;
import muserver.common.utils.LoggerUtils;
import muserver.gameserver.contexts.GameServerContext;
import muserver.gameserver.exceptions.GameServerException;
import muserver.gameserver.initializers.TcpGameServerInitializer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public class GameServer implements IServer {
 private final static Logger logger = LogManager.getLogger(GameServer.class);

 private final ObjectMapper mapper = new ObjectMapper();
 private final EventLoopGroup tcpParentLoopGroup, tcpChildLoopGroup;

 public GameServer() {
  tcpChildLoopGroup = new NioEventLoopGroup(1);
  tcpParentLoopGroup = new NioEventLoopGroup(1);
  mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
 }

 public static void main(String[] args) throws Exception {
  CommandLineParser parser = new DefaultParser();

  Options cliOptions = new Options();

  cliOptions.addOption("p", "path", true, "path to configuration file");

  CommandLine cl = parser.parse(cliOptions, args);

  if (!cl.hasOption("p")) {
   throw new GameServerException("Please, set --path for configuration file");
  }

  String path = cl.getOptionValue("p");

  GameServer gameServer = new GameServer();

  gameServer.startup(new File(path));

  Runtime.getRuntime().addShutdownHook(new Thread(() -> {
   try {
    gameServer.shutdown();
   } catch (Exception e) {
    logger.error(e.getMessage(), e);
   }
  }));
 }

 public void startup(File file) throws GameServerException {
  try {
   LoggerUtils.updateLoggerConfiguration(GameServer.class.getCanonicalName(), AppenderType.CONSOLE, "%d{DEFAULT} [%t] %-5level %logger{36} - %msg%n", LoggingLevel.INFO);

   CommonConfigs commonConfigs = mapper.readValue(IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8), CommonConfigs.class);

   TcpGameServerInitializer tcpGameServerInitializer = new TcpGameServerInitializer(new GameServerContext());

   logger.info(String.format("Start game server tcp channel on port: %d", commonConfigs.gameServer().tcpPort()));

   new ServerBootstrap()
       .group(tcpParentLoopGroup, tcpChildLoopGroup)
       .channel(NioServerSocketChannel.class)
       .childHandler(tcpGameServerInitializer)
       .bind(commonConfigs.gameServer().tcpPort());

   logger.info(String.format("Game server is running and listening for connections"));
  } catch (Exception e) {
   throw new GameServerException(e.getMessage(), e);
  }
 }

 public void shutdown() throws GameServerException {
  try {
   logger.info("Shutdown game server gracefully");

   tcpChildLoopGroup.shutdownGracefully();

   tcpParentLoopGroup.shutdownGracefully();
  } catch (Exception e) {
   logger.error(e.getMessage(), e);
   throw new GameServerException(e.getMessage(), e);
  }
 }
}
