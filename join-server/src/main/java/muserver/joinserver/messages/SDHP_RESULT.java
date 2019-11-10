package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class SDHP_RESULT extends AbstractPacket<SDHP_RESULT> {
 public static int sizeOf() {
  return PBMSG_HEAD.sizeOf() + 5;
 }

 public static Builder builder() {
  return new AutoValue_SDHP_RESULT.Builder();
 }

 public static SDHP_RESULT create(PBMSG_HEAD header, byte result, Integer itemCount) {
  return builder()
      .header(header)
      .result(result)
      .itemCount(itemCount)
      .build();
 }

 public static SDHP_RESULT deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);
  return SDHP_RESULT.create(
      header,
      EndianUtils.readByte(stream),
      EndianUtils.readIntegerLE(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract byte result();

 public abstract Integer itemCount();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeByte(stream, result());
  EndianUtils.writeIntegerLE(stream, itemCount());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder result(byte result);

  public abstract Builder itemCount(Integer itemCount);

  public abstract SDHP_RESULT build();
 }
}
