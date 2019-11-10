package muserver.common;

public class Globals {
 public static final int MAX_CHAT = 60;
 public static final int MAX_MAGIC = 20;
 public static final int MAX_DBMAGIC = MAX_MAGIC * 3;
 public static final int MAX_IDSTRING = 10;
 public static final int MAX_ITEMNAME = 21;
 public static final int MAX_ANSWERSTR = 30;
 public static final int MAX_IPADDRESS = 16;
 public static final int MAX_SERVERNAME = 20;
 public static final int MAX_QUESTIONSTR = 30;
 public static final int MAX_GUILDNOTICE = 60;
 public static final int MAX_DBINVENTORY = 532;
 public static final int MAX_JOOMINNUMBERSTR = 18;
 public static final int MAX_GUILDNAMESTRING = 8;
 public static final int MAX_WAREHOUSEDBSIZE = 840;
 public static final byte PMHC_BYTE = (byte) 0xC1;
 public static final byte PMHC_WORD = (byte) 0xC2;

 public static final Long[] DEC2_MODULUS_KEY_TABLE = {
     Long.valueOf(0x11E6E),
     Long.valueOf(0x1ADA5),
     Long.valueOf(0x1821B),
     Long.valueOf(0x29C32)
 };

 public static final Long[] ENC1_MODULUS_KEY_TABLE = {
     Long.valueOf(0x1F44F),
     Long.valueOf(0x28386),
     Long.valueOf(0x1125B),
     Long.valueOf(0x1A192)
 };

 public static final Long[] DEC1_MODULUS_KEY_TABLE = {
     Long.valueOf(0x1F44F),
     Long.valueOf(0x28386),
     Long.valueOf(0x1125B),
     Long.valueOf(0x1A192)
 };

 public static final Long[] ENC2_MODULUS_KEY_TABLE = {
     Long.valueOf(0x11E6E),
     Long.valueOf(0x1ADA5),
     Long.valueOf(0x1821B),
     Long.valueOf(0x29C32)
 };

 public static final Long[] DEC2_KEY_TABLE = {
     Long.valueOf(0x4673),
     Long.valueOf(0x7684),
     Long.valueOf(0x607D),
     Long.valueOf(0x2B85),
 };

 public static final Long[] ENC1_KEY_TABLE = {
     Long.valueOf(0x5BC1),
     Long.valueOf(0x2E87),
     Long.valueOf(0x4D68),
     Long.valueOf(0x354F),
 };

 public static final Long[] DEC1_KEY_TABLE = {
     Long.valueOf(0x7B38),
     Long.valueOf(0x07FF),
     Long.valueOf(0xDEB3),
     Long.valueOf(0x27C7),
 };

 public static final Long[] ENC2_KEY_TABLE = {
     Long.valueOf(0x3371),
     Long.valueOf(0x4A5C),
     Long.valueOf(0x8A9A),
     Long.valueOf(0x7393),
 };

 public static final Long[] DEC2_XOR_KEY_TABLE = {
     Long.valueOf(0xF234),
     Long.valueOf(0x7684),
     Long.valueOf(0x607D),
     Long.valueOf(0x2B85)
 };

 public static final Long[] ENC1_XOR_KEY_TABLE = {
     Long.valueOf(0xBD1D),
     Long.valueOf(0xB455),
     Long.valueOf(0x3B43),
     Long.valueOf(0x9239)
 };

 public static final Long[] DEC1_XOR_KEY_TABLE = {
     Long.valueOf(0xBD1D),
     Long.valueOf(0xB455),
     Long.valueOf(0x3B43),
     Long.valueOf(0x9239)
 };

 public static final Long[] ENC2_XOR_KEY_TABLE = {
     Long.valueOf(0xF234),
     Long.valueOf(0x7684),
     Long.valueOf(0x607D),
     Long.valueOf(0x2B85)
 };
}
