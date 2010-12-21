package ru.webmoney.cryptography;

public abstract class Algebra {

    private static final int INT32_SIZE = 32;
    private static final long LONG_MASK = 0xFFFFFFFFL;

    public static int significance(byte[] value) {

        int length = value.length;

        while (length > 1 && value[length - 1] == 0) {
            length--;
        }

        return length;
    }

    public static int significance(int[] value) {

        int length = value.length;

        while (length > 1 && value[length - 1] == 0) {
            length--;
        }

        return length;
    }

    public static int[] resize(int[] value, int length) {

        if (length < 0)
            throw new IllegalArgumentException("length");

        int[] array = new int[length];
        System.arraycopy(value, 0, array, 0, Math.min(value.length, array.length));
        return array;
    }

    public static int[] normalize(int[] value) {

        int length = significance(value);

        if (value.length != length) {
            return resize(value, length);
        }

        return value;
    }

    public static int getBitsCount(int value) {
        return INT32_SIZE - Integer.numberOfLeadingZeros(value);
    }

    public static int getBitsCount(int[] value) {

        int length = significance(value);
        return (length - 1) * INT32_SIZE + getBitsCount(value[length - 1]);
    }

    public static int compare(int[] lhs, int[] rhs) {

        int lhsLength = significance(lhs);
        int rhsLength = significance(rhs);

        if (lhsLength > rhsLength)
            return 1;
        else if (lhsLength < rhsLength)
            return -1;
        else {
            for (int pos = lhsLength - 1; pos >= 0; pos--) {
                if ((lhs[pos] & LONG_MASK) > (rhs[pos] & LONG_MASK))
                    return 1;
                else if ((lhs[pos] & LONG_MASK) < (rhs[pos] & LONG_MASK))
                    return -1;
            }
            return 0;
        }
    }

    public static void sub(int[] lhs, int[] rhs) {

        int lhsLength = significance(lhs);
        int rhsLength = significance(rhs);

        if (lhsLength < rhsLength)
            throw new ArithmeticException("Difference should not be negative.");

        int pos = 0;
        int borrow = 0;

        for (; pos < rhsLength; pos++) {
            long temp = (lhs[pos] & LONG_MASK)
                    - (rhs[pos] & LONG_MASK) - borrow;

            lhs[pos] = (int) temp;
            borrow = (((temp & (1L << INT32_SIZE)) != 0) ? 1 : 0);
        }

        if (rhsLength < lhsLength)
            for (; pos < lhsLength; pos++) {
                long temp = (lhs[pos] & LONG_MASK) - borrow;
                lhs[pos] = (int) temp;
                borrow = (((temp & (1L << INT32_SIZE)) != 0) ? 1 : 0);
            }

        if (1 == borrow)
            throw new ArithmeticException("Difference should not be negative.");
    }

    public static int[] shift(int[] lhs, int rhs) {

        int shiftBits = (rhs > 0 ? rhs : -rhs) % INT32_SIZE;
        int shiftWords = (rhs > 0 ? rhs : -rhs) / INT32_SIZE;

        int inBitsCount = getBitsCount(lhs);
        int inWordsCount = inBitsCount / INT32_SIZE + (inBitsCount % INT32_SIZE > 0 ? 1 : 0);

        int outBitsCount = inBitsCount + rhs;
        int outWordsCount = outBitsCount / INT32_SIZE + (outBitsCount % INT32_SIZE > 0 ? 1 : 0);

        if (outWordsCount <= 0)
            return new int[]{0};

        int[] result = new int[inWordsCount > outWordsCount ? inWordsCount : outWordsCount];

        if (rhs > 0) {
            if (0 == shiftBits) {
                System.arraycopy(lhs, 0, result, shiftWords, outWordsCount - shiftWords);
            } else {
                int pos = 0;
                int carry = 0;

                for (; pos < inWordsCount; pos++) {
                    int temp = lhs[pos];
                    result[pos + shiftWords] = (temp << shiftBits) | carry;
                    carry = temp >>> (INT32_SIZE - shiftBits);
                }

                if (pos + shiftWords < outWordsCount) {
                    result[pos + shiftWords] |= carry;
                }
            }
        } else {
            if (0 == shiftBits) {
                System.arraycopy(lhs, shiftWords, result, 0, outWordsCount);
            } else {
                int carry = 0;
                int pos = outWordsCount;

                if (pos + shiftWords < inWordsCount) {
                    carry = lhs[pos + shiftWords] << (INT32_SIZE - shiftBits);
                }

                pos--;

                for (; pos >= 0; pos--) {
                    int temp = lhs[pos + shiftWords];
                    result[pos] = (temp >>> shiftBits) | carry;
                    carry = temp << (INT32_SIZE - shiftBits);
                }
            }
        }

        return result;
    }

    public static void shiftRight(int[] value) {

        int len = significance(value);
        long carry = 0;

        for (int pos = len - 1; pos >= 0; pos--) {
            long temp = (value[pos] & LONG_MASK);

            long nextCarry = ((temp & 1) << (INT32_SIZE - 1) & LONG_MASK);
            value[pos] = (int) (((temp >>> 1) | carry) & LONG_MASK);
            carry = nextCarry;
        }
    }

    public static void remainder(int[] lhs, int[] rhs) {

        int rhsBitsCount = getBitsCount(rhs);

        if (0 == rhsBitsCount)
            throw new ArithmeticException("Attempt to divide by zero.");

        while (compare(lhs, rhs) >= 0) {
            int lhsBitsCount = getBitsCount(lhs);

            if (0 == lhsBitsCount)
                break;

            int shift = lhsBitsCount - rhsBitsCount;

            int[] temp = shift(rhs, shift);

            if (compare(lhs, temp) < 0)
                shiftRight(temp);

            while (compare(lhs, temp) >= 0)
                sub(lhs, temp);
        }
    }
}