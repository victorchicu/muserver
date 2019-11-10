package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
typedef struct
{
	PBMSG_HEAD	h;

	char		Id[MAX_IDSTRING];
	short		Number;
} SDHP_BILLSEARCH, * LPSDHP_SDHP_BILLSEARCH;
 */
@AutoValue
public abstract class SDHP_BILLSEARCH extends AbstractPacket<SDHP_BILLSEARCH> {
    public static SDHP_BILLSEARCH.Builder builder() {
        return new AutoValue_SDHP_BILLSEARCH.Builder();
    }

    public static SDHP_BILLSEARCH create(PBMSG_HEAD header, String id, Short number) {
        return builder()
                .header(header)
                .id(id)
                .number(number)
                .build();
    }

    public static SDHP_BILLSEARCH deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

        return SDHP_BILLSEARCH.create(
                header,
                new String(EndianUtils.readBytes(stream, Globals.MAX_IDSTRING)),
                EndianUtils.readShortBE(stream)
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract String id();

    public abstract Short number();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        EndianUtils.writeString(stream, id(), Globals.MAX_IDSTRING);
        EndianUtils.writeShortLE(stream, number());
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract SDHP_BILLSEARCH.Builder header(PBMSG_HEAD header);

        public abstract SDHP_BILLSEARCH.Builder id(String id);

        public abstract SDHP_BILLSEARCH.Builder number(Short number);

        public abstract SDHP_BILLSEARCH build();
    }
}
