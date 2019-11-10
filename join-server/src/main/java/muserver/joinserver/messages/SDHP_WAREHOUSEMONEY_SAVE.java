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
	char		AccountID[MAX_IDSTRING];
	short		aIndex;
	int			Money;
} SDHP_WAREHOUSEMONEY_SAVE, *LPSDHP_WAREHOUSEMONEY_SAVE;
 */

@AutoValue
public abstract class SDHP_WAREHOUSEMONEY_SAVE extends AbstractPacket<SDHP_WAREHOUSEMONEY_SAVE> {
 public static Builder builder() {
  return new AutoValue_SDHP_WAREHOUSEMONEY_SAVE.Builder();
 }

 public static SDHP_WAREHOUSEMONEY_SAVE create(PBMSG_HEAD header, String accountId, Short index, Integer money) {
  return builder()
      .header(header)
      .accountId(accountId)
      .index(index)
      .money(money)
      .build();
 }

 public static SDHP_WAREHOUSEMONEY_SAVE deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_WAREHOUSEMONEY_SAVE.create(
      header,
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      readShortBE(stream),
      readIntegerBE(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String accountId();

 public abstract Short index();

 public abstract Integer money();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, accountId(), Globals.MAX_IDSTRING);
  writeShortLE(stream, index());
  writeIntegerLE(stream, money());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder accountId(String accountId);

  public abstract Builder index(Short index);

  public abstract Builder money(Integer money);

  public abstract SDHP_WAREHOUSEMONEY_SAVE build();
 }
}
