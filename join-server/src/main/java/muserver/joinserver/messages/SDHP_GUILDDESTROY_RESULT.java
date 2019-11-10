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

	BYTE		NumberH;
	BYTE		NumberL;

	char		GuildName[MAX_GUILDNAMESTRING+1];
	char		Master[MAX_IDSTRING+1];
} SDHP_GUILDDESTROY_RESULT, *LPSDHP_GUILDDESTROY_RESULT;
 */

@AutoValue
public abstract class SDHP_GUILDDESTROY_RESULT extends AbstractPacket<SDHP_GUILDDESTROY_RESULT> {
    public static Builder builder() {
        return new AutoValue_SDHP_GUILDDESTROY_RESULT.Builder();
    }

    public static SDHP_GUILDDESTROY_RESULT create(PBMSG_HEAD header, Byte result, Byte flag, Short number, String guildName, String master) {
        return builder()
                .header(header)
                .result(result)
                .flag(flag)
                .number(number)
                .guildName(guildName)
                .master(master)
                .build();
    }

    public static SDHP_GUILDDESTROY_RESULT deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

        return SDHP_GUILDDESTROY_RESULT.create(
                header,
                readByte(stream),
                readByte(stream),
                readShortBE(stream),
                new String(readBytes(stream, Globals.MAX_GUILDNAMESTRING + 1)),
                new String(readBytes(stream, Globals.MAX_IDSTRING + 1))
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract Byte result();

    public abstract Byte flag();

    public abstract Short number();

    public abstract String guildName();

    public abstract String master();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        writeByte(stream, result());
        writeByte(stream, flag());
        writeShortLE(stream, number());
        writeString(stream, guildName(), Globals.MAX_GUILDNAMESTRING + 1);
        writeString(stream, master(), Globals.MAX_IDSTRING + 1);
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder result(Byte result);

        public abstract Builder flag(Byte flag);

        public abstract Builder number(Short number);

        public abstract Builder guildName(String guildName);

        public abstract Builder master(String master);

        public abstract SDHP_GUILDDESTROY_RESULT build();
    }
}
