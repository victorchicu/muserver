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
	char		szId[10];
	char		Notice[61];
} SDHP_USER_NOTICE, *LPSDHP_USER_NOTICE;
 */

@AutoValue
public abstract class SDHP_USER_NOTICE extends AbstractPacket<SDHP_USER_NOTICE> {
 public static Builder builder() {
  return new AutoValue_SDHP_USER_NOTICE.Builder();
 }

 public static SDHP_USER_NOTICE create(PBMSG_HEAD header, String id, String notice) {
  return builder()
      .header(header)
      .id(id)
      .notice(notice)
      .build();
 }

 public static SDHP_USER_NOTICE deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_USER_NOTICE.create(
      header,
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      new String(readBytes(stream, Globals.MAX_GUILDNOTICE + 1))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String id();

 public abstract String notice();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, id(), Globals.MAX_IDSTRING);
  writeString(stream, notice(), Globals.MAX_GUILDNOTICE + 1);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder id(String id);

  public abstract Builder notice(String notice);

  public abstract SDHP_USER_NOTICE build();
 }
}
