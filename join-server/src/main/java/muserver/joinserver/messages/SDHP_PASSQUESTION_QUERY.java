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
} SDHP_PASSQUESTION_QUERY, *LPSDHP_PASSQUESTION_QUERY;
 */

@AutoValue
public abstract class SDHP_PASSQUESTION_QUERY extends AbstractPacket<SDHP_PASSQUESTION_QUERY> {
 public static Builder builder() {
  return new AutoValue_SDHP_PASSQUESTION_QUERY.Builder();
 }

 public static SDHP_PASSQUESTION_QUERY create(PBMSG_HEAD header, Short number, String id) {
  return builder()
      .header(header)
      .number(number)
      .id(id)
      .build();
 }

 public static SDHP_PASSQUESTION_QUERY deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_PASSQUESTION_QUERY.create(
      header,
      readShortBE(stream),
      new String(readBytes(stream, Globals.MAX_IDSTRING))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Short number();

 public abstract String id();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeShortLE(stream, number());
  writeString(stream, id(), Globals.MAX_IDSTRING);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder number(Short number);

  public abstract Builder id(String id);

  public abstract SDHP_PASSQUESTION_QUERY build();
 }
}
