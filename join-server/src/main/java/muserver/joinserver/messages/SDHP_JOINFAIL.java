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

	short		Number;
	char		Id[MAX_IDSTRING+1];
	int			UserNumber;
	int			DBNumber;
} SDHP_JOINFAIL, * LPSDHP_JOINFAIL;
 */

@AutoValue
public abstract class SDHP_JOINFAIL extends AbstractPacket<SDHP_JOINFAIL> {
 public static Builder builder() {
  return new AutoValue_SDHP_JOINFAIL.Builder();
 }

 public static SDHP_JOINFAIL create(PBMSG_HEAD header, Short number, String id, Integer userNumber, Integer dbNumber) {
  return builder()
      .header(header)
      .number(number)
      .id(id)
      .userNumber(userNumber)
      .dbNumber(dbNumber)
      .build();
 }

 public static SDHP_JOINFAIL deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_JOINFAIL.create(
      header,
      readShortBE(stream),
      new String(readBytes(stream, Globals.MAX_IDSTRING + 1)),
      readIntegerBE(stream),
      readIntegerBE(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Short number();

 public abstract String id();

 public abstract Integer userNumber();

 public abstract Integer dbNumber();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeShortLE(stream, number());
  writeString(stream, id(), Globals.MAX_IDSTRING + 1);
  writeIntegerLE(stream, userNumber());
  writeIntegerLE(stream, dbNumber());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder number(Short number);

  public abstract Builder id(String id);

  public abstract Builder userNumber(Integer userNumber);

  public abstract Builder dbNumber(Integer dbNumber);

  public abstract SDHP_JOINFAIL build();
 }
}
