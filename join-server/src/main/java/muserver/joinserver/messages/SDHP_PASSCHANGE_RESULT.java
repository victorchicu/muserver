package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
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
	short	Number;
	BYTE	Result;
} SDHP_PASSCHANGE_RESULT, *LPSDHP_PASSCHANGE_RESULT;
 */

@AutoValue
public abstract class SDHP_PASSCHANGE_RESULT extends AbstractPacket<SDHP_PASSCHANGE_RESULT> {
 public static Builder builder() {
  return new AutoValue_SDHP_PASSCHANGE_RESULT.Builder();
 }

 public static SDHP_PASSCHANGE_RESULT create(PBMSG_HEAD header, Short number, Byte result) {
  return builder()
      .header(header)
      .number(number)
      .result(result)
      .build();
 }

 public static SDHP_PASSCHANGE_RESULT deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_PASSCHANGE_RESULT.create(
      header,
      readShortBE(stream),
      readByte(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Short number();

 public abstract Byte result();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeShortLE(stream, number());
  writeByte(stream, result());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder number(Short number);

  public abstract Builder result(Byte result);

  public abstract SDHP_PASSCHANGE_RESULT build();
 }
}
