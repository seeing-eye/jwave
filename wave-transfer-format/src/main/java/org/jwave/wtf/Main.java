package org.jwave.wtf;


import org.jwave.wtf.util.MemoryUtil;


import java.util.Arrays;

/**
 * @author e.petrov. Created 11 - 2017.
 */
public class Main {
    public static void main(String[] args) {
        byte[] bytes = new byte[] {1, 2, 3, 4, 5, 127, 0};
        long dmAddr = MemoryUtil.UNSAFE.allocateMemory(bytes.length);
        MemoryUtil.UNSAFE.copyMemory(bytes, MemoryUtil.UNSAFE.arrayBaseOffset(byte[].class), null, dmAddr, bytes.length);
        byte[] target = new byte[bytes.length];
        MemoryUtil.UNSAFE.copyMemory(null, dmAddr, target, MemoryUtil.UNSAFE.arrayBaseOffset(byte[].class), bytes.length);
        System.out.println(Arrays.toString(target));
    }
}
