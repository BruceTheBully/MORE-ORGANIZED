
package com.example.FM;

public class DustDSPProcessor {
    public static byte[] process(byte[] input, int length) {
        byte[] output = new byte[length];
        for (int i = 0; i < length; i++) {
            output[i] = (byte) Math.max(Byte.MIN_VALUE, Math.min(Byte.MAX_VALUE, input[i] * 0.9));
        }
        return output;
    }
}
