package muserver.joinserver.messages;

/*
typedef struct
{
	PBMSG_HEAD	h;
	BYTE		Event;
} SDHP_EVENTSTART, *LPSDHP_EVENTSTART;
 */

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

@AutoValue
public abstract class SDHP_EVENTSTART extends AbstractPacket<SDHP_EVENTSTART> {
 public static Builder builder() {
  return new AutoValue_SDHP_EVENTSTART.Builder();
 }

 public static SDHP_EVENTSTART create(PBMSG_HEAD header, Byte event) {
  return builder()
      .header(header)
      .event(event)
      .build();
 }

 public static SDHP_EVENTSTART deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_EVENTSTART.create(
      header,
      readByte(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Byte event();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeByte(stream, event());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder event(Byte event);

  public abstract SDHP_EVENTSTART build();
 }
}
