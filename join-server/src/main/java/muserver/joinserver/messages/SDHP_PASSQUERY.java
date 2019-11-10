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
	char	JoominN[MAX_JOOMINNUMBERSTR];
	char	Answer[MAX_ANSWERSTR];
	char    IpAddress[17];
} SDHP_PASSQUERY, *LPSDHP_PASSQUERY;
 */

@AutoValue
public abstract class SDHP_PASSQUERY extends AbstractPacket<SDHP_PASSQUERY> {
 public static Builder builder() {
  return new AutoValue_SDHP_PASSQUERY.Builder();
 }

 public static SDHP_PASSQUERY create(PBMSG_HEAD header, Short number, String id, String joomin, String answer, String ipAddress) {
  return builder()
      .header(header)
      .number(number)
      .id(id)
      .joomin(joomin)
      .answer(answer)
      .ipAddress(ipAddress)
      .build();
 }

 public static SDHP_PASSQUERY deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_PASSQUERY.create(
      header,
      readShortBE(stream),
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      new String(readBytes(stream, Globals.MAX_JOOMINNUMBERSTR)),
      new String(readBytes(stream, Globals.MAX_ANSWERSTR)),
      new String(readBytes(stream, Globals.MAX_IPADDRESS))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Short number();

 public abstract String id();

 public abstract String joomin();

 public abstract String answer();

 public abstract String ipAddress();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeShortLE(stream, number());
  writeString(stream, id(), Globals.MAX_IDSTRING);
  writeString(stream, joomin(), Globals.MAX_JOOMINNUMBERSTR);
  writeString(stream, answer(), Globals.MAX_ANSWERSTR);
  writeString(stream, ipAddress(), Globals.MAX_IPADDRESS);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder number(Short number);

  public abstract Builder id(String id);

  public abstract Builder joomin(String joomin);

  public abstract Builder answer(String answer);

  public abstract Builder ipAddress(String ipAddress);

  public abstract SDHP_PASSQUERY build();
 }
}
