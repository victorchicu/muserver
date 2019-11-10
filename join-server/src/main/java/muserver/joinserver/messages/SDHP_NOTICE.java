package muserver.joinserver.messages;

/*
typedef struct
{
	PBMSG_HEAD	h;
	char		Notice[61];
} SDHP_NOTICE, *LPSDHP_NOTICE;
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
public abstract class SDHP_NOTICE extends AbstractPacket<SDHP_NOTICE> {
 public static Builder builder() {
  return new AutoValue_SDHP_NOTICE.Builder();
 }

 public static SDHP_NOTICE create(PBMSG_HEAD header, String notice) {
  return builder()
      .header(header)
      .notice(notice)
      .build();
 }

 public static SDHP_NOTICE deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_NOTICE.create(
      header,
      new String(readBytes(stream, Globals.MAX_GUILDNOTICE + 1))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String notice();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, notice(), Globals.MAX_GUILDNOTICE + 1);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder notice(String notice);

  public abstract SDHP_NOTICE build();
 }
}
