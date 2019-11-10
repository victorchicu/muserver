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
	char GuildName[MAX_GUILDNAMESTRING+1];
	char szGuildNotice[MAX_GUILDNOTICE];
} SDHP_GUILDNOTICE, *LPSDHP_GUILDNOTICE;
 */

@AutoValue
public abstract class SDHP_GUILDNOTICE extends AbstractPacket<SDHP_GUILDNOTICE> {
 public static Builder builder() {
  return new AutoValue_SDHP_GUILDNOTICE.Builder();
 }

 public static SDHP_GUILDNOTICE create(PBMSG_HEAD header, String guildName, String guildNotice) {
  return builder()
      .header(header)
      .guildName(guildName)
      .guildNotice(guildNotice)
      .build();
 }

 public static SDHP_GUILDNOTICE deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_GUILDNOTICE.create(
      header,
      new String(readBytes(stream, Globals.MAX_GUILDNAMESTRING + 1)),
      new String(readBytes(stream, Globals.MAX_GUILDNOTICE))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String guildName();

 public abstract String guildNotice();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, guildName(), Globals.MAX_GUILDNAMESTRING + 1);
  writeString(stream, guildNotice(), Globals.MAX_GUILDNOTICE);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder guildName(String guildName);

  public abstract Builder guildNotice(String guildNotice);

  public abstract SDHP_GUILDNOTICE build();
 }
}
