package muserver.dataserver;

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
import muserver.dataserver.contexts.DataServerContext;
import muserver.dataserver.exceptions.DataServerException;
import muserver.dataserver.initializers.TcpDataServerInitializer;
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

public class DataServer  implements IServer {
 private final static Logger logger = LogManager.getLogger(DataServer.class);

 private final ObjectMapper mapper = new ObjectMapper();
 private final EventLoopGroup tcpParentLoopGroup, tcpChildLoopGroup;

 public DataServer() {
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
   throw new DataServerException("Please, set --path for configuration file");
  }

  String path = cl.getOptionValue("p");

  DataServer dataServer = new DataServer();

  dataServer.startup(new File(path));

  Runtime.getRuntime().addShutdownHook(new Thread(() -> {
   try {
    dataServer.shutdown();
   } catch (Exception e) {
    logger.error(e.getMessage(), e);
   }
  }));
 }

 public void startup(File file) throws DataServerException {
  try {
   LoggerUtils.updateLoggerConfiguration(DataServer.class.getCanonicalName(), AppenderType.CONSOLE, "%d{DEFAULT} [%t] %-5level %logger{36} - %msg%n", LoggingLevel.INFO);

   CommonConfigs commonConfigs = mapper.readValue(IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8), CommonConfigs.class);

   TcpDataServerInitializer tcpDataServerInitializer = new TcpDataServerInitializer(new DataServerContext());

   logger.info(String.format("Start data server tcp channel on port: %d", commonConfigs.dataServer().tcpPort()));

   new ServerBootstrap()
           .group(tcpParentLoopGroup, tcpChildLoopGroup)
           .channel(NioServerSocketChannel.class)
           .childHandler(tcpDataServerInitializer)
           .bind(commonConfigs.dataServer().tcpPort());

   logger.info(String.format("Data server is running and listening for connections"));
  } catch (Exception e) {
   throw new DataServerException(e.getMessage(), e);
  }
 }

 public void shutdown() throws DataServerException {
  try {
   logger.info("Shutdown data server gracefully");

   tcpChildLoopGroup.shutdownGracefully();

   tcpParentLoopGroup.shutdownGracefully();
  } catch (Exception e) {
   logger.error(e.getMessage(), e);
   throw new DataServerException(e.getMessage(), e);
  }
 }
}

