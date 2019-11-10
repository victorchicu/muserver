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
	int			Total;
	int			Number;
} SDHP_ZENLOAD, *LPSDHP_ZENLOAD;
 */

@AutoValue
public abstract class SDHP_ZENLOAD extends AbstractPacket<SDHP_ZENLOAD> {
 public static Builder builder() {
  return new AutoValue_SDHP_ZENLOAD.Builder();
 }

 public static SDHP_ZENLOAD create(PBMSG_HEAD header, String accountId, Integer total, Integer number) {
  return builder()
      .header(header)
      .accountId(accountId)
      .total(total)
      .number(number)
      .build();
 }

 public static SDHP_ZENLOAD deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_ZENLOAD.create(
      header,
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      readIntegerBE(stream),
      readIntegerBE(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String accountId();

 public abstract Integer total();

 public abstract Integer number();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, accountId(), Globals.MAX_IDSTRING);
  writeIntegerLE(stream, total());
  writeIntegerLE(stream, number());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder accountId(String accountId);

  public abstract Builder total(Integer total);

  public abstract Builder number(Integer number);

  public abstract SDHP_ZENLOAD build();
 }
}
