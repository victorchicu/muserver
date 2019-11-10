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

	BYTE		Result;
	BYTE		Flag;

	DWORD		GuildNumber;

	BYTE		NumberH;
	BYTE		NumberL;
	char		Master[MAX_IDSTRING+1];
	char		GuildName[MAX_GUILDNAMESTRING+1];
	BYTE		Mark[32];

} SDHP_GUILDCREATE_RESULT, *LPSDHP_GUILDCREATE_RESULT;
 */

@AutoValue
public abstract class SDHP_GUILDCREATE_RESULT extends AbstractPacket<SDHP_GUILDCREATE_RESULT> {
    public static Builder builder() {
        return new AutoValue_SDHP_GUILDCREATE_RESULT.Builder();
    }

    public static SDHP_GUILDCREATE_RESULT create(PBMSG_HEAD header, Byte result, Byte flag, Integer guildNumber, Short number, String master, String guildName, byte[] mark) {
        return builder()
                .header(header)
                .result(result)
                .flag(flag)
                .guildNumber(guildNumber)
                .number(number)
                .master(master)
                .guildName(guildName)
                .mark(mark)
                .build();
    }

    public static SDHP_GUILDCREATE_RESULT deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

        return SDHP_GUILDCREATE_RESULT.create(
                header,
                readByte(stream),
                readByte(stream),
                readIntegerBE(stream),
                readShortBE(stream),
                new String(readBytes(stream, Globals.MAX_IDSTRING + 1)),
                new String(readBytes(stream, Globals.MAX_GUILDNAMESTRING + 1)),
                readBytes(stream, 32)
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract Byte result();

    public abstract Byte flag();

    public abstract Integer guildNumber();

    public abstract Short number();

    public abstract String master();

    public abstract String guildName();

    public abstract byte[] mark();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        writeByte(stream, result());
        writeByte(stream, flag());
        writeIntegerLE(stream, guildNumber());
        writeShortLE(stream, number());
        writeString(stream, master(), Globals.MAX_IDSTRING + 1);
        writeString(stream, guildName(), Globals.MAX_GUILDNAMESTRING + 1);
        writeBytes(stream, mark());
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder result(Byte result);

        public abstract Builder flag(Byte flag);

        public abstract Builder guildNumber(Integer guildNumber);

        public abstract Builder number(Short number);

        public abstract Builder master(String master);

        public abstract Builder guildName(String guildName);

        public abstract Builder mark(byte[] mark);

        public abstract SDHP_GUILDCREATE_RESULT build();
    }
}
