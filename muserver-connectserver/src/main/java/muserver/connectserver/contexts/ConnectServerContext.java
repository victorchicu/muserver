package muserver.connectserver.contexts;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import muserver.common.configs.ServerConfigs;
import muserver.common.messages.AbstractPacket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectServerContext {
 private final Map<Short, ServerConfigs> serversConfigsMap;
 private final ConcurrentHashMap<Short, AbstractPacket> packets = new ConcurrentHashMap<>();
 private final ConcurrentHashMap<ChannelId, ChannelHandlerContext> clients;

 public ConnectServerContext(Map<Short, ServerConfigs> serversConfigsMap) {
  this.clients = new ConcurrentHashMap<>();
  this.serversConfigsMap = serversConfigsMap;
 }

 public Map<Short, ServerConfigs> serversConfigsMap() {
  return serversConfigsMap;
 }

 public ConcurrentHashMap<Short, AbstractPacket> packets() {
  return packets;
 }

 public ConcurrentHashMap<ChannelId, ChannelHandlerContext> clients() {
  return clients;
 }
}