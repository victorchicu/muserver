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

	char		Id[MAX_IDSTRING];
	short		Number;
	BYTE		cCertifyType;
	BYTE		PayCode;
	//union
	//{
		char        EndsDays[12];
	//	struct
	//	{
	//		int		nRemainPoint;
	//		int		nRemainSecond;
	//	};
	//};

	int			EndTime;
} SDHP_BILLSEARCH_RESULT, * LPSDHP_SDHP_BILLSEARCH_RESULT;
 */
@AutoValue
public abstract class SDHP_BILLSEARCH_RESULT extends AbstractPacket<SDHP_BILLSEARCH_RESULT> {
    public static Builder builder() {
        return new AutoValue_SDHP_BILLSEARCH_RESULT.Builder();
    }

    public static SDHP_BILLSEARCH_RESULT create(PBMSG_HEAD header, String id, Short number, Byte certifyType, Byte payCode, String endsDays, Integer endTime) {
        return builder()
                .header(header)
                .id(id)
                .number(number)
                .certifyType(certifyType)
                .payCode(payCode)
                .endsDays(endsDays)
                .endTime(endTime)
                .build();
    }

    public static SDHP_BILLSEARCH_RESULT deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);
        return SDHP_BILLSEARCH_RESULT.create(
                header,
                new String(readBytes(stream, Globals.MAX_IDSTRING)),
                readShortBE(stream),
                readByte(stream),
                readByte(stream),
                new String(readBytes(stream, 12)),
                readIntegerBE(stream)
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract String id();

    public abstract Short number();

    public abstract Byte certifyType();

    public abstract Byte payCode();

    public abstract String endsDays();

    public abstract Integer endTime();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        writeString(stream, id(), Globals.MAX_IDSTRING);
        writeShortLE(stream, number());
        writeByte(stream, certifyType());
        writeByte(stream, payCode());
        writeString(stream, endsDays(), 12);
        writeIntegerLE(stream, endTime());
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder id(String id);

        public abstract Builder number(Short number);

        public abstract Builder certifyType(Byte certifyType);

        public abstract Builder payCode(Byte payCode);

        public abstract Builder endsDays(String endsDays);

        public abstract Builder endTime(Integer endTime);

        public abstract SDHP_BILLSEARCH_RESULT build();
    }
}
