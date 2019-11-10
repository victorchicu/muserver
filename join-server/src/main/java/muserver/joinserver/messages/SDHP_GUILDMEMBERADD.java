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
	char		MemberID[MAX_IDSTRING+1];
	BYTE		NumberH;
	BYTE		NumberL;
} SDHP_GUILDMEMBERADD, *LPSDHP_GUILDMEMBERADD;
 */

@AutoValue
public abstract class SDHP_GUILDMEMBERADD extends AbstractPacket<SDHP_GUILDMEMBERADD> {
 public static Builder builder() {
  return new AutoValue_SDHP_GUILDMEMBERADD.Builder();
 }

 public static SDHP_GUILDMEMBERADD create(PBMSG_HEAD header, String guildName, String memberId, Short number) {
  return builder()
      .header(header)
      .guildName(guildName)
      .memberId(memberId)
      .number(number)
      .build();
 }

 public static SDHP_GUILDMEMBERADD deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_GUILDMEMBERADD.create(
      header,
      new String(readBytes(stream, Globals.MAX_GUILDNAMESTRING + 1)),
      new String(readBytes(stream, Globals.MAX_IDSTRING + 1)),
      readShortBE(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String guildName();

 public abstract String memberId();

 public abstract Short number();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, guildName(), Globals.MAX_GUILDNAMESTRING + 1);
  writeString(stream, memberId(), Globals.MAX_IDSTRING + 1);
  writeShortLE(stream, number());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder guildName(String guildName);

  public abstract Builder memberId(String memberId);

  public abstract Builder number(Short number);

  public abstract SDHP_GUILDMEMBERADD build();
 }
}
