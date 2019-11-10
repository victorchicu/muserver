package muserver.joinserver.messages;

/*
typedef struct
{
	PBMSG_HEAD  h;
	char    AccountID[MAX_IDSTRING+1];
	char	Name[MAX_IDSTRING+1];
	short	Number;
} SDHP_DBCHARINFOREQUEST, *LPSDHP_DBCHARINFOREQUEST;
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
public abstract class SDHP_DBCHARINFOREQUEST extends AbstractPacket<SDHP_DBCHARINFOREQUEST> {
 public static Builder builder() {
  return new AutoValue_SDHP_DBCHARINFOREQUEST.Builder();
 }

 public static SDHP_DBCHARINFOREQUEST create(PBMSG_HEAD header, String accountId, String name, Short number) {
  return builder()
      .header(header)
      .accountId(accountId)
      .name(name)
      .number(number)
      .build();
 }

 public static SDHP_DBCHARINFOREQUEST deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_DBCHARINFOREQUEST.create(
      header,
      new String(readBytes(stream, Globals.MAX_IDSTRING + 1)),
      new String(readBytes(stream, Globals.MAX_IDSTRING + 1)),
      readShortBE(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String accountId();

 public abstract String name();

 public abstract Short number();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, accountId(), Globals.MAX_IDSTRING + 1);
  writeShortLE(stream, number());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder accountId(String accountId);

  public abstract Builder name(String name);

  public abstract Builder number(Short number);

  public abstract SDHP_DBCHARINFOREQUEST build();
 }
}
