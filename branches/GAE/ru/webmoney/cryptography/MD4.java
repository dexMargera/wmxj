package ru.webmoney.cryptography;

import java.security.MessageDigest;

public final class MD4 extends MessageDigest {

    private static final int BYTE_SIZE = 8;
    private static final int INT32_SIZE = 32;

    // Block
    private static final int BLOCK_SIZE = 512;
    // State
    private static final int STATE_SIZE = 128;
    // Count
    private static final int COUNT_SIZE = 64;
    // Scope
    private static final int FINAL_SCOPE_SIZE = BLOCK_SIZE - COUNT_SIZE; // 448
    private static final byte END_MASK = (byte) 0x80;

    private static final int I0 = 0x67452301;
    private static final int I1 = 0xEFCDAB89;
    private static final int I2 = 0x98BADCFE;
    private static final int I3 = 0x10325476;

    private static final int C2 = 0x5A827999;
    private static final int C3 = 0x6ED9EBA1;

    private static final int FS1 = 3;
    private static final int FS2 = 7;
    private static final int FS3 = 11;
    private static final int FS4 = 19;

    private static final int GS1 = 3;
    private static final int GS2 = 5;
    private static final int GS3 = 9;
    private static final int GS4 = 13;

    private static final int HS1 = 3;
    private static final int HS2 = 9;
    private static final int HS3 = 11;
    private static final int HS4 = 15;

    private final int[] block;
    private final int[] state;
    private long count;

    public MD4() {
        super("MD4");

        block = new int[BLOCK_SIZE / INT32_SIZE];
        state = new int[STATE_SIZE / INT32_SIZE];

        engineReset();
    }

    public void engineReset() {

        for (int pos = 0; pos < BLOCK_SIZE / INT32_SIZE; pos++)
            block[pos] = 0;

        state[0] = I0;
        state[1] = I1;
        state[2] = I2;
        state[3] = I3;

        count = 0;
    }

    public void engineUpdate(byte b) {
        engineUpdate(new byte[]{b}, 0, 1);
    }

    public void engineUpdate(byte[] input, int offset, int count) {

        if (offset < 0  || input.length < offset)
            throw new IllegalArgumentException("offset");

        if (count < 0 || input.length < offset + count)
            throw new IllegalArgumentException("count");

        int internalOffset = (int) (this.count % (BLOCK_SIZE / BYTE_SIZE));
        this.count += count;

        if ((internalOffset > 0) && (internalOffset + count >= BLOCK_SIZE / BYTE_SIZE)) {
            Buffer.blockCopy(input, offset, block, internalOffset, BLOCK_SIZE / BYTE_SIZE - internalOffset);

            offset += BLOCK_SIZE / BYTE_SIZE - internalOffset;
            count -= BLOCK_SIZE / BYTE_SIZE - internalOffset;

            transformBlock();
            internalOffset = 0;
        }

        while (count >= BLOCK_SIZE / BYTE_SIZE) {
            Buffer.blockCopy(input, offset, block, 0, BLOCK_SIZE / BYTE_SIZE);

            offset += BLOCK_SIZE / BYTE_SIZE;
            count -= BLOCK_SIZE / BYTE_SIZE;

            transformBlock();
        }

        if (count > 0)
            Buffer.blockCopy(input, offset, block, internalOffset, count);
    }

    public byte[] engineDigest() {
        int internalOffset = (int) (count % (BLOCK_SIZE / BYTE_SIZE));

        int length;

        if (internalOffset >= FINAL_SCOPE_SIZE / BYTE_SIZE)
            length = BLOCK_SIZE / BYTE_SIZE + BLOCK_SIZE / BYTE_SIZE - internalOffset;
        else
            length = BLOCK_SIZE / BYTE_SIZE - internalOffset;

        byte[] array = new byte[length];

        array[0] = END_MASK;

        System.arraycopy(Buffer.getBytes(count * BYTE_SIZE), 0, array, length - (COUNT_SIZE / BYTE_SIZE), COUNT_SIZE / BYTE_SIZE);

        engineUpdate(array, 0, length);

        return Buffer.getBytes(state);
    }

    private void transformBlock() {
        int a = state[0];
        int b = state[1];
        int c = state[2];
        int d = state[3];

        a = ff(a, b, c, d, block[0], FS1);
        d = ff(d, a, b, c, block[1], FS2);
        c = ff(c, d, a, b, block[2], FS3);
        b = ff(b, c, d, a, block[3], FS4);
        a = ff(a, b, c, d, block[4], FS1);
        d = ff(d, a, b, c, block[5], FS2);
        c = ff(c, d, a, b, block[6], FS3);
        b = ff(b, c, d, a, block[7], FS4);
        a = ff(a, b, c, d, block[8], FS1);
        d = ff(d, a, b, c, block[9], FS2);
        c = ff(c, d, a, b, block[10], FS3);
        b = ff(b, c, d, a, block[11], FS4);
        a = ff(a, b, c, d, block[12], FS1);
        d = ff(d, a, b, c, block[13], FS2);
        c = ff(c, d, a, b, block[14], FS3);
        b = ff(b, c, d, a, block[15], FS4);

        a = gg(a, b, c, d, block[0], GS1);
        d = gg(d, a, b, c, block[4], GS2);
        c = gg(c, d, a, b, block[8], GS3);
        b = gg(b, c, d, a, block[12], GS4);
        a = gg(a, b, c, d, block[1], GS1);
        d = gg(d, a, b, c, block[5], GS2);
        c = gg(c, d, a, b, block[9], GS3);
        b = gg(b, c, d, a, block[13], GS4);
        a = gg(a, b, c, d, block[2], GS1);
        d = gg(d, a, b, c, block[6], GS2);
        c = gg(c, d, a, b, block[10], GS3);
        b = gg(b, c, d, a, block[14], GS4);
        a = gg(a, b, c, d, block[3], GS1);
        d = gg(d, a, b, c, block[7], GS2);
        c = gg(c, d, a, b, block[11], GS3);
        b = gg(b, c, d, a, block[15], GS4);

        a = hh(a, b, c, d, block[0], HS1);
        d = hh(d, a, b, c, block[8], HS2);
        c = hh(c, d, a, b, block[4], HS3);
        b = hh(b, c, d, a, block[12], HS4);
        a = hh(a, b, c, d, block[2], HS1);
        d = hh(d, a, b, c, block[10], HS2);
        c = hh(c, d, a, b, block[6], HS3);
        b = hh(b, c, d, a, block[14], HS4);
        a = hh(a, b, c, d, block[1], HS1);
        d = hh(d, a, b, c, block[9], HS2);
        c = hh(c, d, a, b, block[5], HS3);
        b = hh(b, c, d, a, block[13], HS4);
        a = hh(a, b, c, d, block[3], HS1);
        d = hh(d, a, b, c, block[11], HS2);
        c = hh(c, d, a, b, block[7], HS3);
        b = hh(b, c, d, a, block[15], HS4);

        state[0] += a;
        state[1] += b;
        state[2] += c;
        state[3] += d;
    }

    private static int rot(int t, int s) {
        return (t << s) | (t >>> (INT32_SIZE - s));
    }

    private static int f(int x, int y, int z) {
        return (x & y) | (~x & z);
    }

    private static int g(int x, int y, int z) {
        return (x & y) | (x & z) | (y & z);
    }

    private static int h(int x, int y, int z) {
        return x ^ y ^ z;
    }

    private static int ff(int a, int b, int c, int d, int x, int s) {
        int t = a + f(b, c, d) + x;
        return rot(t, s);
    }

    private static int gg(int a, int b, int c, int d, int x, int s) {
        int t = a + g(b, c, d) + x + C2;
        return rot(t, s);
    }

    private static int hh(int a, int b, int c, int d, int x, int s) {
        int t = a + h(b, c, d) + x + C3;
        return rot(t, s);
    }
}