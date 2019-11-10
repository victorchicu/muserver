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
	BYTE	Result;
	char	Password[MAX_IDSTRING];
} SDHP_PASSQUERY_RESULT, *LPSDHP_PASSQUERY_RESULT;
 */

@AutoValue
public abstract class SDHP_PASSQUERY_RESULT extends AbstractPacket<SDHP_PASSQUERY_RESULT> {
 public static Builder builder() {
  return new AutoValue_SDHP_PASSQUERY_RESULT.Builder();
 }

 public static SDHP_PASSQUERY_RESULT create(PBMSG_HEAD header, Short number, Byte result, String password) {
  return builder()
      .header(header)
      .number(number)
      .result(result)
      .password(password)
      .build();
 }

 public static SDHP_PASSQUERY_RESULT deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_PASSQUERY_RESULT.create(
      header,
      readShortBE(stream),
      readByte(stream),
      new String(readBytes(stream, Globals.MAX_IDSTRING))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Short number();

 public abstract Byte result();

 public abstract String password();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeShortLE(stream, number());
  writeByte(stream, result());
  writeString(stream, password(), Globals.MAX_IDSTRING);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder number(Short number);

  public abstract Builder result(Byte result);

  public abstract Builder password(String password);

  public abstract SDHP_PASSQUERY_RESULT build();
 }
}
