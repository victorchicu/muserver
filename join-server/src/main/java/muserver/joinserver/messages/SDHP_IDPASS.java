package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.utils.BuxConvert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
typedef struct
{
	PBMSG_HEAD	h;

	char		Id[MAX_IDSTRING];
	char		Pass[MAX_IDSTRING];
	short		Number;
	char		IpAddress[17];
} SDHP_IDPASS, *LPSDHP_IDPASS;
 */

import static muserver.common.utils.EndianUtils.*;

@AutoValue
public abstract class SDHP_IDPASS extends AbstractPacket<SDHP_IDPASS> {
 public static Builder builder() {
  return new AutoValue_SDHP_IDPASS.Builder();
 }

 public static SDHP_IDPASS create(PBMSG_HEAD header, Byte type, Short number, String id, String pass, String ipAddress) {
  return builder()
      .header(header)
      .type(type)
      .number(number)
      .id(id)
      .pass(pass)
      .ipAddress(ipAddress)
      .build();
 }

 public static SDHP_IDPASS deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_IDPASS.create(
      header,
      readByte(stream),
      readShortLE(stream),
      new String(readBytes(stream, Globals.MAX_IDSTRING + 1)).trim(),
      new String(readBytes(stream, Globals.MAX_IDSTRING + 1)).trim(),
      new String(readBytes(stream, Globals.MAX_IPADDRESS)).trim()
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Byte type();

 public abstract Short number();

 public abstract String id();

 public abstract String pass();

 public abstract String ipAddress();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeByte(stream, type());
  writeShortLE(stream, number());
  writeString(stream, id(), Globals.MAX_IDSTRING);
  writeString(stream, pass(), Globals.MAX_IDSTRING);
  writeString(stream, ipAddress(), Globals.MAX_IPADDRESS);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder id(String id);

  public abstract Builder pass(String pass);

  public abstract Builder number(Short number);

  public abstract Builder ipAddress(String ipAddress);

  public abstract Builder type(Byte type);

  public abstract SDHP_IDPASS build();
 }
}
