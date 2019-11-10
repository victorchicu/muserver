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
	DWORD		Serial;
	char		ServerName[20];
	char		Account[MAX_IDSTRING];
	char		Name[MAX_IDSTRING];
	char		ItemName[21];
	BYTE		X;
	BYTE		Y;
	BYTE		ItemLevel;
	BYTE		ItemOp1;
	BYTE		ItemOp2;
	BYTE		ItemOp3;

} SDHP_ITEMMOVESAVE, *LPSDHP_ITEMMOVESAVE;
 */

@AutoValue
public abstract class SDHP_ITEMMOVESAVE extends AbstractPacket<SDHP_ITEMMOVESAVE> {
 public static Builder builder() {
  return new AutoValue_SDHP_ITEMMOVESAVE.Builder();
 }

 public static SDHP_ITEMMOVESAVE create(PBMSG_HEAD header, Integer serial, String serverName, String account, String name, String itemName, Byte x, Byte y, Byte itemLevel, Byte itemOp1, Byte itemOp2, Byte itemOp3) {
  return builder()
      .header(header)
      .serial(serial)
      .serverName(serverName)
      .account(account)
      .name(name)
      .itemName(itemName)
      .x(x)
      .y(y)
      .itemLevel(itemLevel)
      .itemOp1(itemOp1)
      .itemOp2(itemOp2)
      .itemOp3(itemOp3)
      .build();
 }

 public static SDHP_ITEMMOVESAVE deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_ITEMMOVESAVE.create(
      header,
      readIntegerBE(stream),
      new String(readBytes(stream, Globals.MAX_SERVERNAME)),
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      new String(readBytes(stream, Globals.MAX_ITEMNAME)),
      readByte(stream),
      readByte(stream),
      readByte(stream),
      readByte(stream),
      readByte(stream),
      readByte(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Integer serial();

 public abstract String serverName();

 public abstract String account();

 public abstract String name();

 public abstract String itemName();

 public abstract Byte x();

 public abstract Byte y();

 public abstract Byte itemLevel();

 public abstract Byte itemOp1();

 public abstract Byte itemOp2();

 public abstract Byte itemOp3();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeIntegerLE(stream, serial());
  writeString(stream, serverName(), Globals.MAX_SERVERNAME);
  writeString(stream, account(), Globals.MAX_IDSTRING);
  writeString(stream, name(), Globals.MAX_IDSTRING);
  writeString(stream, itemName(), Globals.MAX_ITEMNAME);
  writeByte(stream, x());
  writeByte(stream, y());
  writeByte(stream, itemLevel());
  writeByte(stream, itemOp1());
  writeByte(stream, itemOp2());
  writeByte(stream, itemOp3());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder serial(Integer serial);

  public abstract Builder serverName(String serverName);

  public abstract Builder account(String account);

  public abstract Builder name(String name);

  public abstract Builder itemName(String itemName);

  public abstract Builder x(Byte x);

  public abstract Builder y(Byte y);

  public abstract Builder itemLevel(Byte itemLevel);

  public abstract Builder itemOp1(Byte itemOp1);

  public abstract Builder itemOp2(Byte itemOp2);

  public abstract Builder itemOp3(Byte itemOp3);

  public abstract SDHP_ITEMMOVESAVE build();
 }
}
