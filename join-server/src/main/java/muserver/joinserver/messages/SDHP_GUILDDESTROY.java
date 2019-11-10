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

	BYTE		NumberH;
	BYTE		NumberL;

	char		GuildName[MAX_GUILDNAMESTRING];
	char		Master[MAX_IDSTRING];
} SDHP_GUILDDESTROY, *LPSDHP_GUILDDESTROY;
 */

@AutoValue
public abstract class SDHP_GUILDDESTROY extends AbstractPacket<SDHP_GUILDDESTROY> {
    public static Builder builder() {
        return new AutoValue_SDHP_GUILDDESTROY.Builder();
    }

    public static SDHP_GUILDDESTROY create(PBMSG_HEAD header, Short number, String guildName, String master) {
        return builder()
                .header(header)
                .number(number)
                .guildName(guildName)
                .master(master)
                .build();
    }

    public static SDHP_GUILDDESTROY deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

        return SDHP_GUILDDESTROY.create(
                header,
                readShortBE(stream),
                new String(readBytes(stream, Globals.MAX_GUILDNAMESTRING)),
                new String(readBytes(stream, Globals.MAX_IDSTRING))
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract Short number();

    public abstract String guildName();

    public abstract String master();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        writeShortLE(stream, number());
        writeString(stream, guildName(), Globals.MAX_GUILDNAMESTRING);
        writeString(stream, master(), Globals.MAX_IDSTRING);
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder number(Short number);

        public abstract Builder guildName(String guildName);

        public abstract Builder master(String master);

        public abstract SDHP_GUILDDESTROY build();
    }
}
