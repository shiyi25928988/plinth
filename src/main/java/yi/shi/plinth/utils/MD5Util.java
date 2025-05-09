package yi.shi.plinth.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class MD5Util {

    public static String md5(Object...objs) {
        try {
            String input = Arrays.deepToString(objs);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger no = new BigInteger(1, hashBytes);
            String hashtext = no.toString(16);
            return hashtext;
        } catch (Exception e) {
            throw new RuntimeException("Error generating MD5 hash", e);
        }
    }
}
