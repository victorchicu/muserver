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
	PBMSG_HEAD	h;
	char		Account[MAX_IDSTRING];
	char		Name[MAX_IDSTRING];
} SDHP_LOVEHEARTEVENT, * LPSDHP_LOVEHEARTEVENT;
 */

@AutoValue
public abstract class SDHP_LOVEHEARTEVENT extends AbstractPacket<SDHP_LOVEHEARTEVENT> {
 public static Builder builder() {
  return new AutoValue_SDHP_LOVEHEARTEVENT.Builder();
 }

 public static SDHP_LOVEHEARTEVENT create(PBMSG_HEAD header, String account, String name) {
  return builder()
      .header(header)
      .account(account)
      .name(name)
      .build();
 }

 public static SDHP_LOVEHEARTEVENT deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);
  return SDHP_LOVEHEARTEVENT.create(
      header,
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      new String(readBytes(stream, Globals.MAX_IDSTRING))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String account();

 public abstract String name();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, account(), Globals.MAX_IDSTRING);
  writeString(stream, name(), Globals.MAX_IDSTRING);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder account(String account);

  public abstract Builder name(String name);

  public abstract SDHP_LOVEHEARTEVENT build();
 }
}
