package muserver.joinserver.messages;

/*
typedef struct
{
	PBMSG_HEAD	h;

	BYTE		Result;

	char		Name[MAX_IDSTRING];
	int			Number;

} SDHP_LOVEHEARTEVENT_RESULT, * LPSDHP_LOVEHEARTEVENT_RESULT;
 */

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

@AutoValue
public abstract class SDHP_LOVEHEARTEVENT_RESULT extends AbstractPacket<SDHP_LOVEHEARTEVENT_RESULT> {
 public static Builder builder() {
  return new AutoValue_SDHP_LOVEHEARTEVENT_RESULT.Builder();
 }

 public static SDHP_LOVEHEARTEVENT_RESULT create(PBMSG_HEAD header, Byte result, String name, Integer number) {
  return builder()
      .header(header)
      .result(result)
      .name(name)
      .number(number)
      .build();
 }

 public static SDHP_LOVEHEARTEVENT_RESULT deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_LOVEHEARTEVENT_RESULT.create(
      header,
      readByte(stream),
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      readIntegerBE(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Byte result();

 public abstract String name();

 public abstract Integer number();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeByte(stream, result());
  writeString(stream, name(), Globals.MAX_IDSTRING);
  writeIntegerLE(stream, number());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder result(Byte result);

  public abstract Builder name(String name);

  public abstract Builder number(Integer number);

  public abstract SDHP_LOVEHEARTEVENT_RESULT build();
 }
}
