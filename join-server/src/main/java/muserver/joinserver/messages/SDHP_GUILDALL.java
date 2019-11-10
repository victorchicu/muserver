package muserver.joinserver.messages;

/*
typedef struct
{
	char		MemberID[MAX_IDSTRING+1];
} SDHP_GUILDALL, *LPSDHP_GUILDALL;
 */

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

@AutoValue
public abstract class SDHP_GUILDALL extends AbstractPacket<SDHP_GUILDALL> {
    public static Builder builder() {
        return new AutoValue_SDHP_GUILDALL.Builder();
    }

    public static SDHP_GUILDALL create(String memberId) {
        return builder()
                .memberId(memberId)
                .build();
    }

    public static SDHP_GUILDALL deserialize(ByteArrayInputStream stream) throws IOException {
        return SDHP_GUILDALL.create(new String(readBytes(stream, Globals.MAX_IDSTRING + 1)));
    }

    public abstract String memberId();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        writeString(stream, memberId(), Globals.MAX_IDSTRING + 1);
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder memberId(String memberId);

        public abstract SDHP_GUILDALL build();
    }
}
