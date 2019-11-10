package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.Globals;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

/*
typedef struct
{
	PBMSG_HEAD	h;

	BYTE			Type;		// 0:Join Server, 1:MapServer 2: ManagerServer
	unsigned short	Port;		// Server Port Number
	char			ServerName[50];
} SDHP_SERVERINFO, * LPSDHP_SERVERINFO;
 */

@AutoValue
public abstract class SDHP_SERVERINFO extends AbstractPacket<SDHP_SERVERINFO> {
 public static Builder builder() {
  return new AutoValue_SDHP_SERVERINFO.Builder();
 }

 public static SDHP_SERVERINFO create(PBMSG_HEAD header, Byte type, Short port, String serverName) {
  return builder()
      .header(header)
      .type(type)
      .port(port)
      .serverName(serverName)
      .build();
 }

 public static SDHP_SERVERINFO deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_SERVERINFO.create(
      header,
      readByte(stream),
      readShortLE(stream),
      new String(readBytes(stream, Globals.MAX_SERVERNAME + 30))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Byte type(); //0 join server, 1 map server, 2 manager server

 public abstract Short port();

 public abstract String serverName();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeByte(stream, type());
  writeShortLE(stream, port());
  writeString(stream, serverName(), Globals.MAX_SERVERNAME + 30);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder type(Byte type);

  public abstract Builder port(Short port);

  public abstract Builder serverName(String serverName);

  public abstract SDHP_SERVERINFO build();
 }
}
