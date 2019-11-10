package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

/*
typedef struct
{
	PBMSG_HEAD	h;
	BYTE		x;
	BYTE		y;
	BYTE		MapNumber;
	DWORD		m_Number;
	BYTE		Type;
	BYTE		Level;
	BYTE		Dur;
	BYTE		Op1;
	BYTE		Op2;
	BYTE		Op3;
	int			aIndex;
} SDHP_ITEMCREATERECV, *LPSDHP_ITEMCREATERECV;
 */

@AutoValue
public abstract class SDHP_ITEMCREATERECV extends AbstractPacket<SDHP_ITEMCREATERECV> {
 public static Builder builder() {
  return new AutoValue_SDHP_ITEMCREATERECV.Builder();
 }

 public static SDHP_ITEMCREATERECV create(PBMSG_HEAD header, Byte x, Byte y, Byte mapNumber, Integer number, Byte type, Byte level, Byte dur, Byte op1, Byte op2, Byte op3, Integer index) {
  return builder()
      .header(header)
      .x(x)
      .y(y)
      .mapNumber(mapNumber)
      .number(number)
      .type(type)
      .level(level)
      .dur(dur)
      .op1(op1)
      .op2(op2)
      .op3(op3)
      .index(index)
      .build();
 }

 public static SDHP_ITEMCREATERECV deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_ITEMCREATERECV.create(
      header,
      readByte(stream),
      readByte(stream),
      readByte(stream),
      readIntegerBE(stream),
      readByte(stream),
      readByte(stream),
      readByte(stream),
      readByte(stream),
      readByte(stream),
      readByte(stream),
      readIntegerBE(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Byte x();

 public abstract Byte y();

 public abstract Byte mapNumber();

 public abstract Integer number();

 public abstract Byte type();

 public abstract Byte level();

 public abstract Byte dur();

 public abstract Byte op1();

 public abstract Byte op2();

 public abstract Byte op3();

 public abstract Integer index();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeByte(stream, x());
  writeByte(stream, y());
  writeByte(stream, mapNumber());
  writeIntegerLE(stream, number());
  writeByte(stream, type());
  writeByte(stream, level());
  writeByte(stream, dur());
  writeByte(stream, op1());
  writeByte(stream, op2());
  writeByte(stream, op3());
  writeIntegerLE(stream, index());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder x(Byte x);

  public abstract Builder y(Byte y);

  public abstract Builder mapNumber(Byte mapNumber);

  public abstract Builder number(Integer number);

  public abstract Builder type(Byte type);

  public abstract Builder level(Byte level);

  public abstract Builder dur(Byte dur);

  public abstract Builder op1(Byte op1);

  public abstract Builder op2(Byte op2);

  public abstract Builder op3(Byte op3);

  public abstract Builder index(Integer index);

  public abstract SDHP_ITEMCREATERECV build();
 }
}
