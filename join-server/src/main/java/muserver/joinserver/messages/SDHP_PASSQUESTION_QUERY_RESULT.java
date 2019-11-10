package muserver.joinserver.messages;

import static muserver.common.utils.EndianUtils.*;

/*
typedef struct
{
	PBMSG_HEAD	h;
	short	Number;
	BYTE	Result;
	char	Question[MAX_QUESTIONSTR];
} SDHP_PASSQUESTION_QUERY_RESULT, *LPSDHP_PASSQUESTION_QUERY_RESULT;
 */

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class SDHP_PASSQUESTION_QUERY_RESULT extends AbstractPacket<SDHP_PASSQUESTION_QUERY_RESULT> {
 public static Builder builder() {
  return new AutoValue_SDHP_PASSQUESTION_QUERY_RESULT.Builder();
 }

 public static SDHP_PASSQUESTION_QUERY_RESULT create(PBMSG_HEAD header, Short number, Byte result, String question) {
  return builder()
      .header(header)
      .number(number)
      .result(result)
      .question(question)
      .build();
 }

 public static SDHP_PASSQUESTION_QUERY_RESULT deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_PASSQUESTION_QUERY_RESULT.create(
      header,
      readShortBE(stream),
      readByte(stream),
      new String(readBytes(stream, Globals.MAX_QUESTIONSTR))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Short number();

 public abstract Byte result();

 public abstract String question();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeShortLE(stream, number());
  writeByte(stream, result());
  writeString(stream, question(), Globals.MAX_QUESTIONSTR);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder number(Short number);

  public abstract Builder result(Byte result);

  public abstract Builder question(String question);

  public abstract SDHP_PASSQUESTION_QUERY_RESULT build();
 }
}
