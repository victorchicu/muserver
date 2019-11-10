package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

/*
typedef struct
{
	PBMSG_HEAD	h;
	char		GuildName[MAX_GUILDNAMESTRING+1];
	int			Score;
} SDHP_GUILDSCOREUPDATE, *LPSDHP_GUILDSCOREUPDATE;
 */

@AutoValue
public abstract class SDHP_GUILDSCOREUPDATE extends AbstractPacket<SDHP_GUILDSCOREUPDATE> {
 public static Builder builder() {
  return new AutoValue_SDHP_GUILDSCOREUPDATE.Builder();
 }

 public static SDHP_GUILDSCOREUPDATE create(String guildName, Integer score) {
  return builder()
      .guildName(guildName)
      .score(score)
      .build();
 }

 public static SDHP_GUILDSCOREUPDATE deserialize(ByteArrayInputStream stream) throws IOException {
  return SDHP_GUILDSCOREUPDATE.create(
      new String(readBytes(stream, Globals.MAX_GUILDNAMESTRING + 1)),
      readIntegerBE(stream)
  );
 }

 public abstract String guildName();

 public abstract Integer score();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  writeString(stream, guildName(), Globals.MAX_GUILDNAMESTRING + 1);
  writeIntegerLE(stream, score());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder guildName(String guildName);

  public abstract Builder score(Integer score);

  public abstract SDHP_GUILDSCOREUPDATE build();
 }
}
