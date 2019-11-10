package muserver.joinserver.messages;

/*
typedef struct
{
	PWMSG_HEAD	h;

	char		GuildName[MAX_GUILDNAMESTRING+1];
	BYTE		Count;
} SDHP_GUILDALL_COUNT, *LPSDHP_GUILDALL_COUNT;
 */

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PWMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

@AutoValue
public abstract class SDHP_GUILDALL_COUNT extends AbstractPacket<SDHP_GUILDALL_COUNT> {
    public static Builder builder() {
        return new AutoValue_SDHP_GUILDALL_COUNT.Builder();
    }

    public static SDHP_GUILDALL_COUNT create(PWMSG_HEAD header, String guildName, Byte count) {
        return builder()
                .header(header)
                .guildName(guildName)
                .count(count)
                .build();
    }

    public static SDHP_GUILDALL_COUNT deserialize(ByteArrayInputStream stream) throws IOException {
        PWMSG_HEAD header = PWMSG_HEAD.deserialize(stream);

        return SDHP_GUILDALL_COUNT.create(
                header,
                new String(readBytes(stream, Globals.MAX_GUILDNAMESTRING + 1)),
                readByte(stream)
        );
    }

    public abstract PWMSG_HEAD header();

    public abstract String guildName();

    public abstract Byte count();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        writeString(stream, guildName(), Globals.MAX_GUILDNAMESTRING + 1);
        writeByte(stream, count());
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PWMSG_HEAD header);

        public abstract Builder guildName(String guildName);

        public abstract Builder count(Byte count);

        public abstract SDHP_GUILDALL_COUNT build();
    }
}
