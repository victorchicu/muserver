package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

/*
typedef struct
{
	PBMSG_HEAD	h;
	int  UserNumber;
	int  DBNumber;
} SDHP_USERCLOSE, *LPSDHP_USERCLOSE;
 */

@AutoValue
public abstract class SDHP_USERCLOSE extends AbstractPacket<SDHP_USERCLOSE> {
 public static Builder builder() {
  return new AutoValue_SDHP_USERCLOSE.Builder();
 }

 public static SDHP_USERCLOSE create(PBMSG_HEAD header, Integer userNumber, Integer dbNumber) {
  return builder()
      .header(header)
      .userNumber(userNumber)
      .dbNumber(dbNumber)
      .build();
 }

 public static SDHP_USERCLOSE deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_USERCLOSE.create(
      header,
      readIntegerBE(stream),
      readIntegerBE(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Integer userNumber();

 public abstract Integer dbNumber();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeIntegerLE(stream, userNumber());
  writeIntegerLE(stream, dbNumber());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder userNumber(Integer userNumber);

  public abstract Builder dbNumber(Integer dbNumber);

  public abstract SDHP_USERCLOSE build();
 }
}
