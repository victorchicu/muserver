package muserver.base.types;

public enum JoinResult {
 PASSWORD_IS_INCORRECT((byte) 0),
 LOGIN_SUCCEED((byte) 1),
 YOUR_ACCOUNT_IS_INVALID((byte) 2),
 YOUR_ACCOUNT_IS_ALREADY_CONNECTED((byte) 3),
 THE_SERVER_IS_FULL((byte) 4),
 THIS_ACCOUNT_IS_BLOCKED((byte) 5),
 NEW_VERSION_OF_GAME_IS_REQUIRED((byte) 6),
 NO_CHARGE_INFO((byte) 9),
 YOUR_INDIVIDUAL_SUBSCRIPTION_TERM_IS_OVER((byte) 10),
 YOUR_INDIVIDUAL_SUBSCRIPTION_TIME_IS_OVER((byte) 11),
 SUBSCRIPTION_TERM_IS_OVER_ON_YOUR_IP((byte) 12),
 SUBSCRIPTION_TIME_IS_OVER_ON_YOUR_IP_((byte) 13),
 ONLY_PLAYERS_AGE_18_AND_OVER_ARE_PERMITTED((byte) 17),
 PLEASE_PURCHASE_GOLD_CHANNEL_TICKET_TO_ENTER((byte) 64);

 private final byte type;

 JoinResult(byte type) {
  this.type = type;
 }

 public static JoinResult fromByte(byte type) {
  for (JoinResult value : JoinResult.values()) {
   if (value.type() == type) {
    return value;
   }
  }
  throw new IllegalArgumentException("No enum constant: " + type);
 }

 public byte type() {
  return type;
 }
}
