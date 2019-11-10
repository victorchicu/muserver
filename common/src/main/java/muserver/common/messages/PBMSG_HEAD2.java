package muserver.common.messages;

import com.google.auto.value.AutoValue;
import muserver.common.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PBMSG_HEAD2 extends AbstractPacket<PBMSG_HEAD2> {
 public static int sizeOf() {
  return 4;
 }

 public static Builder builder() {
  return new AutoValue_PBMSG_HEAD2.Builder();
 }

 public static PBMSG_HEAD2 create(Byte type, Byte size, Byte headCode, Byte subCode) {
  return builder()
      .type(type)
      .size(size)
      .headCode(headCode)
      .subCode(subCode)
      .build();
 }

 public static PBMSG_HEAD2 deserialize(ByteArrayInputStream stream) throws IOException {
  return PBMSG_HEAD2.create(
      EndianUtils.readByte(stream),
      EndianUtils.readByte(stream),
      EndianUtils.readByte(stream),
      EndianUtils.readByte(stream)
  );
 }

 public abstract Byte type();

 public abstract Byte size();

 public abstract Byte headCode();

 public abstract Byte subCode();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  EndianUtils.writeByte(stream, type());
  EndianUtils.writeByte(stream, size());
  EndianUtils.writeByte(stream, headCode());
  EndianUtils.writeByte(stream, subCode());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder type(Byte type);

  public abstract Builder size(Byte size);

  public abstract Builder headCode(Byte headCode);

  public abstract Builder subCode(Byte subCode);

  public abstract PBMSG_HEAD2 build();
 }
}
