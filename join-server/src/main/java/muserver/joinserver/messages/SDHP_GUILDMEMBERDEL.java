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

	BYTE		NumberH;
	BYTE		NumberL;

	char		GuildName[MAX_GUILDNAMESTRING];
	char		MemberID[MAX_IDSTRING];
} SDHP_GUILDMEMBERDEL, *LPSDHP_GUILDMEMBERDEL;
 */

@AutoValue
public abstract class SDHP_GUILDMEMBERDEL extends AbstractPacket<SDHP_GUILDMEMBERDEL> {
 public static Builder builder() {
  return new AutoValue_SDHP_GUILDMEMBERDEL.Builder();
 }

 public static SDHP_GUILDMEMBERDEL create(PBMSG_HEAD header, Short number, String guildName, String memberId) {
  return builder()
      .header(header)
      .number(number)
      .guildName(guildName)
      .memberId(memberId)
      .build();
 }

 public static SDHP_GUILDMEMBERDEL deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_GUILDMEMBERDEL.create(
      header,
      readShortBE(stream),
      new String(readBytes(stream, Globals.MAX_GUILDNAMESTRING)),
      new String(readBytes(stream, Globals.MAX_IDSTRING))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Short number();

 public abstract String guildName();

 public abstract String memberId();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeShortLE(stream, number());
  writeString(stream, guildName(), Globals.MAX_GUILDNAMESTRING);
  writeString(stream, memberId(), Globals.MAX_IDSTRING);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder number(Short number);

  public abstract Builder guildName(String guildName);

  public abstract Builder memberId(String memberId);

  public abstract SDHP_GUILDMEMBERDEL build();
 }
}
