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
	BYTE		x;
	BYTE		y;
	BYTE		MapNumber;
} SDHP_LOVEHEARTCREATE, *LPSDHP_LOVEHEARTCREATE;
 */

@AutoValue
public abstract class SDHP_LOVEHEARTCREATE extends AbstractPacket<SDHP_LOVEHEARTCREATE> {
 public static Builder builder() {
  return new AutoValue_SDHP_LOVEHEARTCREATE.Builder();
 }

 public static SDHP_LOVEHEARTCREATE create(PBMSG_HEAD header, Byte x, Byte y, Byte mapNumber) {
  return builder()
      .header(header)
      .x(x)
      .y(y)
      .mapNumber(mapNumber)
      .build();
 }

 public static SDHP_LOVEHEARTCREATE deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_LOVEHEARTCREATE.create(
      header,
      readByte(stream),
      readByte(stream),
      readByte(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Byte x();

 public abstract Byte y();

 public abstract Byte mapNumber();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  writeByte(stream, x());
  writeByte(stream, y());
  writeByte(stream, mapNumber());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder x(Byte x);

  public abstract Builder y(Byte y);

  public abstract Builder mapNumber(Byte mapNumber);

  public abstract SDHP_LOVEHEARTCREATE build();
 }
}
