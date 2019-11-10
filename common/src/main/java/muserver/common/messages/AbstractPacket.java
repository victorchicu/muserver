package muserver.common.messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

public abstract class AbstractPacket<T> implements Serializable {
 private final Date packetTime;

 public AbstractPacket() {
  packetTime = new Date();
 }

 public Date packetTime() {
  return packetTime;
 }

 public abstract byte[] serialize(ByteArrayOutputStream stream) throws IOException;
}