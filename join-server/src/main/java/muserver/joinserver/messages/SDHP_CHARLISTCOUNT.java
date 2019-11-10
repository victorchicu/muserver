package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PWMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.common.utils.EndianUtils.*;

/*
typedef struct
{
	PWMSG_HEAD	h;
	short		Number;
	BYTE		Count;
	int			DbNumber;
	BYTE		Magumsa;		// ё¶°Л»зё¦ ёёµйјц АЦґВ °иБ¤АОБц..
	char		AccountId[MAX_IDSTRING+1];
} SDHP_CHARLISTCOUNT, *LPSDHP_CHARLISTCOUNT;
 */
@AutoValue
public abstract class SDHP_CHARLISTCOUNT extends AbstractPacket<SDHP_CHARLISTCOUNT> {
    public static Builder builder() {
        return new AutoValue_SDHP_CHARLISTCOUNT.Builder();
    }

    public static SDHP_CHARLISTCOUNT create(PWMSG_HEAD header, Short number, Byte count, Integer dbNumber, byte magumsa, String accountId) {
        return builder()
                .header(header)
                .number(number)
                .count(count)
                .dbNumber(dbNumber)
                .magumsa(magumsa)
                .accountId(accountId)
                .build();
    }

    public static SDHP_CHARLISTCOUNT deserialize(ByteArrayInputStream stream) throws IOException {
        PWMSG_HEAD header = PWMSG_HEAD.deserialize(stream);

        return SDHP_CHARLISTCOUNT.create(
                header,
                readShortBE(stream),
                readByte(stream),
                readIntegerBE(stream),
                readByte(stream),
                new String(readBytes(stream, Globals.MAX_IDSTRING + 1))
        );
    }

    public abstract PWMSG_HEAD header();

    public abstract Short number();

    public abstract Byte count();

    public abstract Integer dbNumber();

    public abstract byte magumsa();

    public abstract String accountId();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        writeShortLE(stream, number());
        writeByte(stream, count());
        writeIntegerLE(stream, dbNumber());
        writeByte(stream, magumsa());
        writeString(stream, accountId(), Globals.MAX_IDSTRING + 1);
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PWMSG_HEAD header);

        public abstract Builder number(Short number);

        public abstract Builder count(Byte count);

        public abstract Builder dbNumber(Integer dbNumber);

        public abstract Builder magumsa(byte magumsa);

        public abstract Builder accountId(String accountId);

        public abstract SDHP_CHARLISTCOUNT build();
    }
}
