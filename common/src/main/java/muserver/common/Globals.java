package muserver.common;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Globals {
 private static final Short UNSIGNED_BYTE_MAX_VALUE = ((Byte.MAX_VALUE << 1) + 1);
 private static final Integer UNSIGNED_SHORT_MAX_VALUE = ((Short.MAX_VALUE << 1) + 1);
 private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

 public static Short getUnsignedByteMaxValue() {
  return UNSIGNED_BYTE_MAX_VALUE;
 }

 public static Integer getUnsignedShortMaxValue() {
  return UNSIGNED_SHORT_MAX_VALUE;
 }

 public static ObjectMapper getObjectMapper() {
  return OBJECT_MAPPER;
 }
}