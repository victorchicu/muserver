package muserver.connectserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.utils.EndianUtils;
import muserver.connectserver.messages.AutoValue_PMSG_JOINSERVER_INFO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_JOINSERVER_INFO extends AbstractPacket<PMSG_JOINSERVER_INFO> {
 public int sizeOf() {
  return PBMSG_HEAD.sizeOf() + 4;
 }

 public static Builder builder() {
  return new AutoValue_PMSG_JOINSERVER_INFO.Builder();
 }

 public static PMSG_JOINSERVER_INFO create(PBMSG_HEAD header, Integer queueSize) {
  return builder()
      .header(header)
      .queueSize(queueSize)
      .build();
 }

 public static PMSG_JOINSERVER_INFO deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);
  return PMSG_JOINSERVER_INFO.create(header, EndianUtils.readIntegerBE(stream));
 }

 public abstract PBMSG_HEAD header();

 public abstract Integer queueSize();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeIntegerLE(stream, queueSize());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder queueSize(Integer queueSize);

  public abstract PMSG_JOINSERVER_INFO build();
 }
}