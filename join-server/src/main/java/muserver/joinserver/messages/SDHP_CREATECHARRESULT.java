package muserver.joinserver.messages;

/*
typedef struct
{
	PBMSG_HEAD	h;
	BYTE		Result;
	short		Number;
	char		AccountId[MAX_IDSTRING];
	char        Name[MAX_IDSTRING];
	BYTE		Pos;
	BYTE		ClassSkin;
	BYTE		Equipment[24];	// ВшїлЗП°н АЦґВ Аеєс
} SDHP_CREATECHARRESULT, *LPSDHP_CREATECHARRESULT;
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
public abstract class SDHP_CREATECHARRESULT extends AbstractPacket<SDHP_CREATECHARRESULT> {
    public static Builder builder() {
        return new AutoValue_SDHP_CREATECHARRESULT.Builder();
    }

    public static SDHP_CREATECHARRESULT create(PBMSG_HEAD header, Byte result, Short number, String accountId, String name, Byte pos, Byte classSkin, byte[] equipment) {
        return builder()
                .header(header)
                .result(result)
                .number(number)
                .accountId(accountId)
                .name(name)
                .pos(pos)
                .classSkin(classSkin)
                .equipment(equipment)
                .build();
    }

    public static SDHP_CREATECHARRESULT deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

        return SDHP_CREATECHARRESULT.create(
                header,
                readByte(stream),
                readShortBE(stream),
                new String(readBytes(stream, Globals.MAX_IDSTRING)),
                new String(readBytes(stream, Globals.MAX_IDSTRING)),
                readByte(stream),
                readByte(stream),
                readBytes(stream, 24)
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract Byte result();

    public abstract Short number();

    public abstract String accountId();

    public abstract String name();

    public abstract Byte pos();

    public abstract Byte classSkin();

    public abstract byte[] equipment();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        writeByte(stream, result());
        writeShortLE(stream, number());
        writeString(stream, accountId(), Globals.MAX_IDSTRING);
        writeString(stream, name(), Globals.MAX_IDSTRING);
        writeByte(stream, pos());
        writeByte(stream, classSkin());
        writeBytes(stream, equipment());
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder result(Byte result);

        public abstract Builder number(Short number);

        public abstract Builder accountId(String accountId);

        public abstract Builder name(String name);

        public abstract Builder pos(Byte pos);

        public abstract Builder classSkin(Byte classSkin);

        public abstract Builder equipment(byte[] equipment);

        public abstract SDHP_CREATECHARRESULT build();
    }
}
