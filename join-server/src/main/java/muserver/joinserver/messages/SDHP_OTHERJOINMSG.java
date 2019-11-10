package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

/*
typedef struct
{
	PBMSG_HEAD  h;
	char    AccountID[MAX_IDSTRING];
} SDHP_OTHERJOINMSG, *LPSDHP_OTHERJOINMSG;
 */

@AutoValue
public abstract class SDHP_OTHERJOINMSG extends AbstractPacket<SDHP_OTHERJOINMSG> {
 public static Builder builder() {
  return new AutoValue_SDHP_OTHERJOINMSG.Builder();
 }

 public static SDHP_OTHERJOINMSG create(PBMSG_HEAD header, String accountId) {
  return builder()
      .header(header)
      .accountId(accountId)
      .build();
 }

 public static SDHP_OTHERJOINMSG deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_OTHERJOINMSG.create(
      header,
      new String(readBytes(stream, Globals.MAX_IDSTRING))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String accountId();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, accountId(), Globals.MAX_IDSTRING);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder accountId(String accountId);

  public abstract SDHP_OTHERJOINMSG build();
 }
}
