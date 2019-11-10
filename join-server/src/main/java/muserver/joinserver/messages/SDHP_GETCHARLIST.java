package muserver.joinserver.messages;

/*
typedef struct
{
	PBMSG_HEAD	h;
	//int			UserNumber;	// АЇАъ Аэґл №шИЈ
	char		Id[MAX_IDSTRING];
	short		Number;	// °ФАУ ј­№ц АЇАъ№шИЈ
} SDHP_GETCHARLIST, *LPSDHP_GETCHARLIST;
 */

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

@AutoValue
public abstract class SDHP_GETCHARLIST extends AbstractPacket<SDHP_GETCHARLIST> {
 public static Builder builder() {
  return new AutoValue_SDHP_GETCHARLIST.Builder();
 }

 public static SDHP_GETCHARLIST create(PBMSG_HEAD header, String id, Short number) {
  return builder()
      .header(header)
      .id(id)
      .number(number)
      .build();
 }

 public static SDHP_GETCHARLIST deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_GETCHARLIST.create(
      header,
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      readShortBE(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String id();

 public abstract Short number();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, id(), Globals.MAX_IDSTRING);
  writeShortLE(stream, number());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder id(String id);

  public abstract Builder number(Short number);

  public abstract SDHP_GETCHARLIST build();
 }
}
