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
	char		GuildName[MAX_GUILDNAMESTRING+1];
	char		Master[MAX_IDSTRING+1];
	BYTE		Mark[32];
	int			Score;
	BYTE		Count;
} SDHP_GUILDUPDATE, *LPSDHP_GUILDUPDATE;
 */

@AutoValue
public abstract class SDHP_GUILDUPDATE extends AbstractPacket<SDHP_GUILDUPDATE> {
 public static Builder builder() {
  return new AutoValue_SDHP_GUILDUPDATE.Builder();
 }

 public static SDHP_GUILDUPDATE create(PBMSG_HEAD header, String guildName, String master, byte[] mark, Integer score, Byte count) {
  return builder()
      .header(header)
      .guildName(guildName)
      .master(master)
      .mark(mark)
      .score(score)
      .count(count)
      .build();
 }

 public static SDHP_GUILDUPDATE deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_GUILDUPDATE.create(
      header,
      new String(readBytes(stream, Globals.MAX_GUILDNAMESTRING + 1)),
      new String(readBytes(stream, Globals.MAX_IDSTRING + 1)),
      readBytes(stream, 32),
      readIntegerBE(stream),
      readByte(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String guildName();

 public abstract String master();

 public abstract byte[] mark();

 public abstract Integer score();

 public abstract Byte count();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, guildName(), Globals.MAX_GUILDNAMESTRING + 1);
  writeString(stream, master(), Globals.MAX_IDSTRING + 1);
  writeBytes(stream, mark());
  writeIntegerLE(stream, score());
  writeByte(stream, count());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder guildName(String guildName);

  public abstract Builder master(String master);

  public abstract Builder mark(byte[] mark);

  public abstract Builder score(Integer score);

  public abstract Builder count(Byte count);

  public abstract SDHP_GUILDUPDATE build();
 }
}
