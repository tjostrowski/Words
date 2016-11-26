package pl.otto.words.utils;

import java.util.Random;

/**
 * Created by tomek on 26.11.16.
 */
public class Utils {

    public static final int randomRangeClosed(Random rnd, int fromIncl, int toIncl) {
        return fromIncl + rnd.nextInt(toIncl - fromIncl + 1);
    }

    public static byte[] longToBytes(long l) {
        int nBytes = 7;
        for (int i = 7; i >= 4; i--) {
            if (((l >> (8*i)) & 0xFF) != 0) {
                nBytes = i;
                break;
            }
        }

        byte[] result = new byte[nBytes + 1];
        for (int i = nBytes; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }

        return result;
    }

    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < b.length; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }
}
