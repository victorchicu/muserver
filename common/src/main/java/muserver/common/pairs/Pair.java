package muserver.common.pairs;

import java.util.Objects;

public class Pair<H, S> {
 private final H headCode;
 private final S subCode;

 public Pair(H headCode, S subCode) {
  this.headCode = headCode;
  this.subCode = subCode;
 }

 public H headCode() {
  return headCode;
 }

 public S subCode() {
  return subCode;
 }

 @Override
 public boolean equals(Object obj) {
  if (this == obj) {
   return true;
  }

  if (obj == null || getClass() != obj.getClass()) {
   return false;
  }

  Pair<?, ?> pair = (Pair<?, ?>) obj;

  return headCode.equals(pair.headCode) && subCode.equals(pair.subCode);
 }

 @Override
 public int hashCode() {
  return Objects.hash(headCode, subCode);
 }
}