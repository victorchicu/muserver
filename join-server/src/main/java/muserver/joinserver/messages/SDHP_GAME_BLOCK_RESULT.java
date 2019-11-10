package muserver.joinserver.messages;

/*
typedef struct
{
	PBMSG_HEAD	h;
	BYTE		Result;
} SDHP_GAME_BLOCK_RESULT, *LPSDHP_GAME_BLOCK_RESULT;
 */

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

@AutoValue
public abstract class SDHP_GAME_BLOCK_RESULT extends AbstractPacket<SDHP_GAME_BLOCK_RESULT> {
 public static Builder builder() {
  return new AutoValue_SDHP_GAME_BLOCK_RESULT.Builder();
 }

 public static SDHP_GAME_BLOCK_RESULT create(PBMSG_HEAD header, Byte result) {
  return builder()
      .header(header)
      .result(result)
      .build();
 }

 public static SDHP_GAME_BLOCK_RESULT deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_GAME_BLOCK_RESULT.create(header, readByte(stream));
 }

 public abstract PBMSG_HEAD header();

 public abstract Byte result();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeByte(stream, result());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder result(Byte result);

  public abstract SDHP_GAME_BLOCK_RESULT build();
 }
}
