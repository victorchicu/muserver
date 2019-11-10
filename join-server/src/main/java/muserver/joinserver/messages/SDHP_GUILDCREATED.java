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
	WORD		Number;
	char		GuildName[MAX_GUILDNAMESTRING+1];
	char		Master[MAX_IDSTRING+1];
	BYTE		Mark[32];
	int			score;
} SDHP_GUILDCREATED, *LPSDHP_GUILDCREATED;
 */

@AutoValue
public abstract class SDHP_GUILDCREATED extends AbstractPacket<SDHP_GUILDCREATED> {
    public static Builder builder() {
        return new AutoValue_SDHP_GUILDCREATED.Builder();
    }

    public static SDHP_GUILDCREATED create(PBMSG_HEAD header, Short number, String guildName, String master, byte[] mark, Integer score) {
        return builder()
                .header(header)
                .number(number)
                .guildName(guildName)
                .master(master)
                .mark(mark)
                .score(score)
                .build();
    }

    public static SDHP_GUILDCREATED deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

        return SDHP_GUILDCREATED.create(
                header,
                readShortBE(stream),
                new String(readBytes(stream, Globals.MAX_GUILDNAMESTRING + 1)),
                new String(readBytes(stream, Globals.MAX_IDSTRING + 1)),
                readBytes(stream, 32),
                readIntegerBE(stream)
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract Short number();

    public abstract String guildName();

    public abstract String master();

    public abstract byte[] mark();

    public abstract Integer score();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        writeShortLE(stream, number());
        writeString(stream, guildName(), Globals.MAX_GUILDNAMESTRING + 1);
        writeString(stream, master(), Globals.MAX_IDSTRING + 1);
        writeBytes(stream, mark());
        writeIntegerLE(stream, score());
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder number(Short number);

        public abstract Builder guildName(String guildName);

        public abstract Builder master(String master);

        public abstract Builder mark(byte[] mark);

        public abstract Builder score(Integer score);

        public abstract SDHP_GUILDCREATED build();
    }
}
