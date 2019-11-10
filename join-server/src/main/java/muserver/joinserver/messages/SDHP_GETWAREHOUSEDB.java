package muserver.joinserver.messages;

/*
typedef struct
{
	PBMSG_HEAD	h;
	char		AccountID[MAX_IDSTRING];
	short		aIndex;
} SDHP_GETWAREHOUSEDB, *LPSDHP_GETWAREHOUSEDB;
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
public abstract class SDHP_GETWAREHOUSEDB extends AbstractPacket<SDHP_GETWAREHOUSEDB> {
 public static Builder builder() {
  return new AutoValue_SDHP_GETWAREHOUSEDB.Builder();
 }

 public static SDHP_GETWAREHOUSEDB create(PBMSG_HEAD header, String accountId, Short index) {
  return builder()
      .header(header)
      .accountId(accountId)
      .index(index)
      .build();
 }

 public static SDHP_GETWAREHOUSEDB deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_GETWAREHOUSEDB.create(
      header,
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      readShortBE(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String accountId();

 public abstract Short index();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, accountId(), Globals.MAX_IDSTRING);
  writeShortLE(stream, index());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder accountId(String accountId);

  public abstract Builder index(Short index);

  public abstract SDHP_GETWAREHOUSEDB build();
 }
}
