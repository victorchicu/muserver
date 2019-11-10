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
	BYTE		result;

	short		Number;
	char		Id[MAX_IDSTRING];
	int			UserNumber;
	int			DBNumber;
	char		JoominNumber[13];	// БЦ№О№шИЈ µЮАЪё®
} SDHP_IDPASSRESULT, *LPSDHP_IDPASSRESULT;
 */

@AutoValue
public abstract class SDHP_IDPASSRESULT extends AbstractPacket<SDHP_IDPASSRESULT> {
 public static int sizeOf() {
  return PBMSG_HEAD.sizeOf() + 39;
 }

 public static Builder builder() {
  return new AutoValue_SDHP_IDPASSRESULT.Builder();
 }

 public static SDHP_IDPASSRESULT create(PBMSG_HEAD header, Byte result, Short number, String id, Integer userNumber, Integer dbNumber, String joominNumber) {
  return builder()
      .header(header)
      .result(result)
      .number(number)
      .id(id)
      .userNumber(userNumber)
      .dbNumber(dbNumber)
      .joominNumber(joominNumber)
      .build();
 }

 public static SDHP_IDPASSRESULT deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_IDPASSRESULT.create(
      header,
      readByte(stream),
      readShortLE(stream),
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      readIntegerBE(stream),
      readIntegerBE(stream),
      new String(readBytes(stream, Globals.MAX_JOOMINNUMBERSTR))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Byte result();

 public abstract Short number();

 public abstract String id();

 public abstract Integer userNumber();

 public abstract Integer dbNumber();

 public abstract String joominNumber();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeByte(stream, result());
  writeShortBE(stream, number());
  writeString(stream, id(), Globals.MAX_IDSTRING);
  writeIntegerBE(stream, userNumber());
  writeIntegerBE(stream, dbNumber());
  writeString(stream, joominNumber(), Globals.MAX_JOOMINNUMBERSTR);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder result(Byte result);

  public abstract Builder number(Short number);

  public abstract Builder id(String id);

  public abstract Builder userNumber(Integer userNumber);

  public abstract Builder dbNumber(Integer dbNumber);

  public abstract Builder joominNumber(String joominNumber);

  public abstract SDHP_IDPASSRESULT build();
 }
}
