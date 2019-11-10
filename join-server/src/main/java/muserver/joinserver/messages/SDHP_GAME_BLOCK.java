package muserver.joinserver.messages;

/*
typedef struct
{
	PBMSG_HEAD	h;
	int		ClientIndex;
	char	AccountID[MAX_IDSTRING];
	BYTE	ServerNum;
	char	CharName[MAX_IDSTRING];
	BYTE	Type;
} SDHP_GAME_BLOCK, *LPSDHP_GAME_BLOCK;
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
public abstract class SDHP_GAME_BLOCK extends AbstractPacket<SDHP_GAME_BLOCK> {
 public static Builder builder() {
  return new AutoValue_SDHP_GAME_BLOCK.Builder();
 }

 public static SDHP_GAME_BLOCK create(PBMSG_HEAD header, Integer clientIndex, String accountId, Byte serverNum, String charName, Byte type) {
  return builder()
      .header(header)
      .clientIndex(clientIndex)
      .accountId(accountId)
      .serverNum(serverNum)
      .charName(charName)
      .type(type)
      .build();
 }

 public static SDHP_GAME_BLOCK deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_GAME_BLOCK.create(
      header,
      readIntegerBE(stream),
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      readByte(stream),
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      readByte(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Integer clientIndex();

 public abstract String accountId();

 public abstract Byte serverNum();

 public abstract String charName();

 public abstract Byte type();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeIntegerLE(stream, clientIndex());
  writeString(stream, accountId(), Globals.MAX_IDSTRING);
  writeByte(stream, serverNum());
  writeString(stream, charName(), Globals.MAX_IDSTRING);
  writeByte(stream, type());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder clientIndex(Integer clientIndex);

  public abstract Builder accountId(String accountId);

  public abstract Builder serverNum(Byte serverNum);

  public abstract Builder charName(String charName);

  public abstract Builder type(Byte type);

  public abstract SDHP_GAME_BLOCK build();
 }
}
