package muserver.joinserver.messages;

/*
typedef struct
{
	PBMSG_HEAD  h;
	BYTE ExitCode[3];
} SDHP_EXITMSG, *LPSDHP_EXITMSG;
 */

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

@AutoValue
public abstract class SDHP_EXITMSG extends AbstractPacket<SDHP_EXITMSG> {
 public static Builder builder() {
  return new AutoValue_SDHP_EXITMSG.Builder();
 }

 public static SDHP_EXITMSG create(PBMSG_HEAD header, byte[] exitCode) {
  return builder()
      .header(header)
      .exitCode(exitCode)
      .build();
 }

 public static SDHP_EXITMSG deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_EXITMSG.create(
      header,
      readBytes(stream, 3)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract byte[] exitCode();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeBytes(stream, exitCode());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder exitCode(byte[] exitCode);

  public abstract Builder header(PBMSG_HEAD header);

  public abstract SDHP_EXITMSG build();
 }
}
