package muserver.joinserver.messages;

/*
typedef struct
{
	PWMSG_HEAD	h;
	char		AccountID[MAX_IDSTRING];
	short		aIndex;
	int			Money;
	BYTE		dbItems[MAX_WAREHOUSEDBSIZE];
} SDHP_GETWAREHOUSEDB_SAVE, *LPSDHP_GETWAREHOUSEDB_SAVE;
 */

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PWMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

@AutoValue
public abstract class SDHP_GETWAREHOUSEDB_RESULT extends AbstractPacket<SDHP_GETWAREHOUSEDB_RESULT> {
 public static Builder builder() {
  return new AutoValue_SDHP_GETWAREHOUSEDB_RESULT.Builder();
 }

 public static SDHP_GETWAREHOUSEDB_RESULT create(PWMSG_HEAD header, String accountId, Short index, Integer money, byte[] dbItems) {
  return builder()
      .header(header)
      .accountId(accountId)
      .index(index)
      .money(money)
      .dbItems(dbItems)
      .build();
 }

 public static SDHP_GETWAREHOUSEDB_RESULT deserialize(ByteArrayInputStream stream) throws IOException {
  PWMSG_HEAD header = PWMSG_HEAD.deserialize(stream);

  return SDHP_GETWAREHOUSEDB_RESULT.create(
      header,
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      readShortBE(stream),
      readIntegerBE(stream),
      readBytes(stream, Globals.MAX_WAREHOUSEDBSIZE)
  );
 }

 public abstract PWMSG_HEAD header();

 public abstract String accountId();

 public abstract Short index();

 public abstract Integer money();

 public abstract byte[] dbItems();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, accountId(), Globals.MAX_IDSTRING);
  writeShortLE(stream, index());
  writeIntegerLE(stream, money());
  writeBytes(stream, dbItems());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PWMSG_HEAD header);

  public abstract Builder accountId(String accountId);

  public abstract Builder index(Short index);

  public abstract Builder money(Integer money);

  public abstract Builder dbItems(byte[] dbItems);

  public abstract SDHP_GETWAREHOUSEDB_RESULT build();
 }
}
