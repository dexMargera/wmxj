package ru.webmoney.cryptography;

public abstract class Buffer {

    private static final int BYTE_SIZE = 8;
    private static final int INT32_SIZE = 32;

    public static void blockCopy(byte[] src, int srcOffset, int[] dst, int dstOffset, int count) {

        if (srcOffset < 0 || src.length < srcOffset)
            throw new IllegalArgumentException("srcOffset");

        if (dstOffset < 0 || dst.length * (INT32_SIZE / BYTE_SIZE) < dstOffset)
            throw new IllegalArgumentException("dstOffset");

        if (count < 0 || src.length < srcOffset + count || dst.length * (INT32_SIZE / BYTE_SIZE) < dstOffset + count)
            throw new IllegalArgumentException("count");

        if (0 == count)
            return;

        int offset = dstOffset % (INT32_SIZE / BYTE_SIZE);

        int pos = 0;

        if (offset == 0) {
            if (srcOffset + count > pos + srcOffset)
                dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] = (src[pos + srcOffset] & 0x000000FF);

            if (srcOffset + count > pos + srcOffset + 1)
                dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= ((src[pos + srcOffset + 1] << 8) & 0x0000FFFF);

            if (srcOffset + count > pos + srcOffset + 2)
                dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= ((src[pos + srcOffset + 2] << 16) & 0x00FFFFFF);

            if (srcOffset + count > pos + srcOffset + 3)
                dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= (src[pos + srcOffset + 3] << 24);
        }

        if (offset == 1) {
            dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] &= 0x000000FF;

            if (srcOffset + count > pos + srcOffset)
                dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= ((src[pos + srcOffset] << 8) & 0x0000FFFF);

            if (srcOffset + count > pos + srcOffset + 1)
                dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= ((src[pos + srcOffset + 1] << 16) & 0x00FFFFFF);

            if (srcOffset + count > pos + srcOffset + 2)
                dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= (src[pos + srcOffset + 2] << 24);
        }

        if (offset == 2) {
            dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] &= 0x0000FFFF;

            if (srcOffset + count > pos + srcOffset)
                dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= ((src[pos + srcOffset] << 16) & 0x00FFFFFF);

            if (srcOffset + count > pos + srcOffset + 1)
                dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= (src[pos + srcOffset + 1] << 24);
        }

        if (offset == 3) {
            dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] &= 0x00FFFFFF;

            if (srcOffset + count > pos + srcOffset)
                dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= (src[pos + srcOffset] << 24);
        }

        pos += 4;

        for (; pos <= count - INT32_SIZE / BYTE_SIZE; pos += INT32_SIZE / BYTE_SIZE) {
            dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] = (src[pos + srcOffset - offset] & 0x000000FF);
            dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= ((src[pos + srcOffset + 1 - offset] << 8) & 0x0000FFFF);
            dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= ((src[pos + srcOffset + 2 - offset] << 16) & 0x00FFFFFF);
            dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= (src[pos + srcOffset + 3 - offset] << 24);
        }

        if (srcOffset + count > pos + srcOffset - offset)
            dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] = (src[pos + srcOffset - offset] & 0x000000FF);

        if (srcOffset + count > pos + srcOffset + 1 - offset)
            dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= ((src[pos + srcOffset + 1 - offset] << 8) & 0x0000FFFF);

        if (srcOffset + count > pos + srcOffset + 2 - offset)
            dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= ((src[pos + srcOffset + 2 - offset] << 16) & 0x00FFFFFF);

        if (srcOffset + count > pos + srcOffset + 3 - offset)
            dst[(pos + dstOffset) / (INT32_SIZE / BYTE_SIZE)] |= (src[pos + srcOffset + 3 - offset] << 24);
    }

    public static byte[] getBytes(int[] value) {

        byte[] result = new byte[value.length * 4];

        for (int pos = 0; pos < value.length; pos++) {
            result[pos * 4] = (byte) (value[pos] & 0xFF);
            result[pos * 4 + 1] = (byte) ((value[pos] >> 8) & 0xFF);
            result[pos * 4 + 2] = (byte) ((value[pos] >> 16) & 0xFF);
            result[pos * 4 + 3] = (byte) ((value[pos] >> 24) & 0xFF);
        }

        return result;
    }

    public static byte[] getBytes(long value) {

        byte[] temp = new byte[8];

        temp[0] = (byte) (value & 0xFF);
        temp[1] = (byte) (value >> 8 & 0xFF);
        temp[2] = (byte) (value >> 16 & 0xFF);
        temp[3] = (byte) (value >> 24 & 0xFF);
        temp[4] = (byte) (value >> 32 & 0xFF);
        temp[5] = (byte) (value >> 40 & 0xFF);
        temp[6] = (byte) (value >> 48 & 0xFF);
        temp[7] = (byte) (value >> 56 & 0xFF);

        return temp;
    }
}
