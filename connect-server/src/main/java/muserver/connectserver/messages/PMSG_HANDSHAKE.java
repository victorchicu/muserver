package muserver.connectserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.utils.EndianUtils;
import muserver.connectserver.messages.AutoValue_PMSG_HANDSHAKE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_HANDSHAKE extends AbstractPacket<PMSG_HANDSHAKE> {
 public static int sizeOf() {
  return PBMSG_HEAD.sizeOf() + 1;
 }

 public static Builder builder() {
  return new AutoValue_PMSG_HANDSHAKE.Builder();
 }

 public static PMSG_HANDSHAKE create(PBMSG_HEAD header, byte result) {
  return builder()
      .header(header)
      .result(result)
      .build();
 }

 public static PMSG_HANDSHAKE deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);
  return PMSG_HANDSHAKE.create(header, EndianUtils.readByte(stream));
 }

 public abstract PBMSG_HEAD header();

 public abstract byte result();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeByte(stream, result());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder result(byte result);

  public abstract PMSG_HANDSHAKE build();
 }
}
