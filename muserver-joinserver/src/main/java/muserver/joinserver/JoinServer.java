package muserver.joinserver;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.dialect.DatabaseDialect;
import database.dialect.SqlServerDatabaseDialect;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import muserver.common.IServer;
import muserver.common.configs.CommonConfigs;
import muserver.common.logging.LoggingLevel;
import muserver.common.types.AppenderType;
import muserver.common.utils.LoggerUtils;
import muserver.joinserver.contexts.JoinServerContext;
import muserver.joinserver.exceptions.JoinServerException;
import muserver.joinserver.initializers.TcpJoinServerInitializer;
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

public class JoinServer implements IServer {
 private final static Logger logger = LogManager.getLogger(JoinServer.class);

 private final ObjectMapper mapper = new ObjectMapper();
 private final EventLoopGroup tcpParentLoopGroup, tcpChildLoopGroup;

 public JoinServer() {
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
   throw new JoinServerException("Please, set --path for configuration file");
  }

  String path = cl.getOptionValue("p");

  JoinServer joinServer = new JoinServer();

  joinServer.startup(new File(path));

  Runtime.getRuntime().addShutdownHook(new Thread(() -> {
   try {
    joinServer.shutdown();
   } catch (Exception e) {
    logger.error(e.getMessage(), e);
   }
  }));
 }

 public void startup(File file) throws JoinServerException {
  try {
   LoggerUtils.updateLoggerConfiguration(JoinServer.class.getCanonicalName(), AppenderType.CONSOLE, "%d{DEFAULT} [%t] %-5level %logger{36} - %msg%n", LoggingLevel.INFO);

   CommonConfigs commonConfigs = mapper.readValue(IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8), CommonConfigs.class);

   DatabaseDialect dialect = new SqlServerDatabaseDialect(commonConfigs.joinServer().connectionString());

   TcpJoinServerInitializer tcpJoinServerInitializer = new TcpJoinServerInitializer(new JoinServerContext(dialect));

   logger.info(String.format("Start join server tcp channel on port: %d", commonConfigs.joinServer().tcpPort()));

   new ServerBootstrap()
       .group(tcpParentLoopGroup, tcpChildLoopGroup)
       .channel(NioServerSocketChannel.class)
       .childHandler(tcpJoinServerInitializer)
       .bind(commonConfigs.joinServer().hostname(), commonConfigs.joinServer().tcpPort());

   logger.info(String.format("Join server is running and listening for connections"));
  } catch (Exception e) {
   throw new JoinServerException(e.getMessage(), e);
  }
 }

 public void shutdown() throws JoinServerException {
  try {
   logger.info("Shutdown join server gracefully");

   tcpChildLoopGroup.shutdownGracefully();

   tcpParentLoopGroup.shutdownGracefully();
  } catch (Exception e) {
   logger.error(e.getMessage(), e);
   throw new JoinServerException(e.getMessage(), e);
  }
 }
}
