package muserver.startup;

import muserver.common.IServer;
import muserver.common.exceptions.ServerException;
import muserver.connectserver.ConnectServer;
import muserver.dataserver.DataServer;
import muserver.gameserver.GameServer;
import muserver.joinserver.JoinServer;
import muserver.startup.exceptions.StartupException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class Startup {
 private final static Logger logger = LogManager.getLogger(Startup.class);

 public static void main(String[] args) throws Exception {
  CommandLineParser parser = new DefaultParser();

  Options cliOptions = new Options();

  cliOptions.addOption("p", "path", true, "path to configuration file");

  CommandLine cl = parser.parse(cliOptions, args);

  if (!cl.hasOption("p")) {
   throw new StartupException("Please, set --path for configuration file");
  }

  String path = cl.getOptionValue("p");

  File configurationFile = new File(path);

  IServer connectServer = new ConnectServer(), joinServer = new JoinServer(), dataServer = new DataServer(), gameServer = new GameServer();

  Runtime.getRuntime().addShutdownHook(new Thread(() -> {
   try {
    if (connectServer != null) {
     connectServer.shutdown();
    }

    if (joinServer != null) {
     joinServer.shutdown();
    }

    if (dataServer != null) {
     dataServer.shutdown();
    }

    if (gameServer != null) {
     gameServer.shutdown();
    }
   } catch (Exception e) {
    logger.error(e.getMessage(), e);
   }
  }));

  CompletableFuture.runAsync(() -> {
   try {
    connectServer.startup(configurationFile);
   } catch (ServerException e) {
    logger.fatal(e.getMessage(), e);
   }
  }).thenRun(() -> {
   try {
    joinServer.startup(configurationFile);
   } catch (ServerException e) {
    logger.fatal(e.getMessage(), e);
   }
  }).thenRun(() -> {
   try {
    dataServer.startup(configurationFile);
   } catch (ServerException e) {
    logger.fatal(e.getMessage(), e);
   }
  }).thenRun(() -> {
   try {
    gameServer.startup(configurationFile);
   } catch (ServerException e) {
    logger.fatal(e.getMessage(), e);
   }
  }).exceptionally(throwable -> {
   logger.error(throwable.getMessage(), throwable);
   return null;
  }).get();
 }
}
