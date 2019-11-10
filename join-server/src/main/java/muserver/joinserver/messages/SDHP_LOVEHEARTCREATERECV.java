package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
typedef struct
{
	PBMSG_HEAD	h;
} SDHP_LOVEHEARTCREATERECV, *LPSDHP_LOVEHEARTCREATERECV;
 */

@AutoValue
public abstract class SDHP_LOVEHEARTCREATERECV extends AbstractPacket<SDHP_LOVEHEARTCREATERECV> {
 public static Builder builder() {
  return new AutoValue_SDHP_LOVEHEARTCREATERECV.Builder();
 }

 public static SDHP_LOVEHEARTCREATERECV create(PBMSG_HEAD header) {
  return builder()
      .header(header)
      .build();
 }

 public static SDHP_LOVEHEARTCREATERECV deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);
  return SDHP_LOVEHEARTCREATERECV.create(header);
 }

 public abstract PBMSG_HEAD header();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract SDHP_LOVEHEARTCREATERECV build();
 }
}
