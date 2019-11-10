package muserver.common.messages;

import com.google.auto.value.AutoValue;
import muserver.common.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PWMSG_HEAD extends AbstractPacket<PWMSG_HEAD> {
 public int sizeOf() {
  return 4;
 }

 public static Builder builder() {
  return new AutoValue_PWMSG_HEAD.Builder();
 }

 public static PWMSG_HEAD create(Byte type, Short size, Byte headCode) {
  return builder()
      .type(type)
      .size(size)
      .headCode(headCode)
      .build();
 }

 public static PWMSG_HEAD deserialize(ByteArrayInputStream stream) throws IOException {
  return PWMSG_HEAD.create(
      EndianUtils.readByte(stream),
      EndianUtils.readShortBE(stream),
      EndianUtils.readByte(stream)
  );
 }

 public abstract Byte type();

 public abstract Short size();

 public abstract Byte headCode();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  EndianUtils.writeByte(stream, type());
  EndianUtils.writeShortLE(stream, size());
  EndianUtils.writeByte(stream, headCode());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder type(Byte type);

  public abstract Builder size(Short size);

  public abstract Builder headCode(Byte headCode);

  public abstract PWMSG_HEAD build();
 }
}
