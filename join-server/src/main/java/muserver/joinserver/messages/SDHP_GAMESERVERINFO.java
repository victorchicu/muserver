package muserver.joinserver.messages;

/*

typedef struct
{
	PBMSG_HEAD	h;
	DWORD		ItemCount;
} SDHP_GAMESERVERINFO, *LPSDHP_GAMESERVERINFO;
 */

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

@AutoValue
public abstract class SDHP_GAMESERVERINFO extends AbstractPacket<SDHP_GAMESERVERINFO> {
 public static Builder builder() {
  return new AutoValue_SDHP_GAMESERVERINFO.Builder();
 }

 public static SDHP_GAMESERVERINFO create(PBMSG_HEAD header, Integer itemCount) {
  return builder()
      .header(header)
      .itemCount(itemCount)
      .build();
 }

 public static SDHP_GAMESERVERINFO deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_GAMESERVERINFO.create(header, readIntegerBE(stream));
 }

 public abstract PBMSG_HEAD header();

 public abstract Integer itemCount();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeIntegerLE(stream, itemCount());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder itemCount(Integer itemCount);

  public abstract SDHP_GAMESERVERINFO build();
 }
}
