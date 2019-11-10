package muserver.connectserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD2;
import muserver.common.utils.EndianUtils;
import muserver.connectserver.messages.AutoValue_PMSG_REQ_SERVER_INFO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_REQ_SERVER_INFO extends AbstractPacket<PMSG_REQ_SERVER_INFO> {
 public static int sizeOf() {
  return PBMSG_HEAD2.sizeOf() + 1;
 }

 public static Builder builder() {
  return new AutoValue_PMSG_REQ_SERVER_INFO.Builder();
 }

 public static PMSG_REQ_SERVER_INFO create(PBMSG_HEAD2 header, Byte serverCode) {
  return builder()
      .header(header)
      .serverCode(serverCode)
      .build();
 }

 public static PMSG_REQ_SERVER_INFO deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD2 header = PBMSG_HEAD2.deserialize(stream);
  return PMSG_REQ_SERVER_INFO.create(header, EndianUtils.readByte(stream));
 }

 public abstract PBMSG_HEAD2 header();

 public abstract Byte serverCode();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeByte(stream, serverCode());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD2 header);

  public abstract Builder serverCode(Byte serverCode);

  public abstract PMSG_REQ_SERVER_INFO build();
 }
}
