package muserver.joinserver.messages;

/*
typedef struct
{
	PBMSG_HEAD	h;
	char szId[MAX_IDSTRING];
} SDHP_USERCLOSE_ID, *LPSDHP_USERCLOSE_ID;
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
public abstract class SDHP_USERCLOSE_ID extends AbstractPacket<SDHP_USERCLOSE_ID> {
 public static Builder builder() {
  return new AutoValue_SDHP_USERCLOSE_ID.Builder();
 }

 public static SDHP_USERCLOSE_ID create(PBMSG_HEAD header, String id) {
  return builder()
      .header(header)
      .id(id)
      .build();
 }

 public static SDHP_USERCLOSE_ID deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_USERCLOSE_ID.create(
      header,
      new String(readBytes(stream, Globals.MAX_IDSTRING))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String id();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, id(), Globals.MAX_IDSTRING);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder id(String id);

  public abstract SDHP_USERCLOSE_ID build();
 }
}
