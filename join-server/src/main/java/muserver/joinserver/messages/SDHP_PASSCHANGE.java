package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

/*
typedef struct
{
	PBMSG_HEAD	h;
	short	Number;
	char	Id[MAX_IDSTRING];
	char	PassOld[MAX_IDSTRING];
	char	PassNew[MAX_IDSTRING];
	char	JoominN[MAX_JOOMINNUMBERSTR];
	char	IpAddress[17];
} SDHP_PASSCHANGE, *LPSDHP_PASSCHANGE;
 */

@AutoValue
public abstract class SDHP_PASSCHANGE extends AbstractPacket<SDHP_PASSCHANGE> {
 public static Builder builder() {
  return new AutoValue_SDHP_PASSCHANGE.Builder();
 }

 public static SDHP_PASSCHANGE create(PBMSG_HEAD header, Short number, String id, String passOld, String passNew, String joomin, String ipAddress) {
  return builder()
      .header(header)
      .number(number)
      .id(id)
      .passOld(passOld)
      .passNew(passNew)
      .joomin(joomin)
      .ipAddress(ipAddress)
      .build();
 }

 public static SDHP_PASSCHANGE deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_PASSCHANGE.create(
      header,
      readShortBE(stream),
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      new String(readBytes(stream, Globals.MAX_JOOMINNUMBERSTR)),
      new String(readBytes(stream, Globals.MAX_IPADDRESS))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Short number();

 public abstract String id();

 public abstract String passOld();

 public abstract String passNew();

 public abstract String joomin();

 public abstract String ipAddress();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeShortLE(stream, number());
  writeString(stream, id(), Globals.MAX_IDSTRING);
  writeString(stream, passOld(), Globals.MAX_IDSTRING);
  writeString(stream, passNew(), Globals.MAX_IDSTRING);
  writeString(stream, joomin(), Globals.MAX_JOOMINNUMBERSTR);
  writeString(stream, ipAddress(), Globals.MAX_IPADDRESS);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder number(Short number);

  public abstract Builder id(String id);

  public abstract Builder passOld(String passOld);

  public abstract Builder passNew(String passNew);

  public abstract Builder joomin(String joomin);

  public abstract Builder ipAddress(String ipAddress);

  public abstract SDHP_PASSCHANGE build();
 }
}
