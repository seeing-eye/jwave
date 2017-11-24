package org.jwave.wtf.util;


import sun.misc.Unsafe;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;

public class MemoryUtil {




    public static final Unsafe UNSAFE = unsafe();
    private static final long PER_BYTE_THRESHOLD = 0;


    /**
     * Copy memory between offheap locations.
     *
     * @param srcAddr Source address.
     * @param dstAddr Destination address.
     * @param len Length.
     */
    public static void copyOffheapOffheap(long srcAddr, long dstAddr, long len) {
        if (len <= PER_BYTE_THRESHOLD) {
            for (int i = 0; i < len; i++)
                UNSAFE.putByte(dstAddr + i, UNSAFE.getByte(srcAddr + i));
        }
        else
            UNSAFE.copyMemory(srcAddr, dstAddr, len);
    }

    /**
     * Copy memory from offheap to heap.
     *
     * @param srcAddr Source address.
     * @param dstBase Destination base.
     * @param dstOff Destination offset.
     * @param len Length.
     */
    public static void copyOffheapHeap(long srcAddr, Object dstBase, long dstOff, long len) {
        if (len <= PER_BYTE_THRESHOLD) {
            for (int i = 0; i < len; i++)
                UNSAFE.putByte(dstBase, dstOff + i, UNSAFE.getByte(srcAddr + i));
        }
        else
            UNSAFE.copyMemory(null, srcAddr, dstBase, dstOff, len);
    }

    /**
     * Copy memory from heap to offheap.
     *
     * @param srcBase Source base.
     * @param srcOff Source offset.
     * @param dstAddr Destination address.
     * @param len Length.
     */
    public static void copyHeapOffheap(Object srcBase, long srcOff, long dstAddr, long len) {
        if (len <= PER_BYTE_THRESHOLD) {
            for (int i = 0; i < len; i++)
                UNSAFE.putByte(dstAddr + i, UNSAFE.getByte(srcBase, srcOff + i));
        }
        else
            UNSAFE.copyMemory(srcBase, srcOff, null, dstAddr, len);
    }


    private static Unsafe unsafe() {
        try {
            return Unsafe.getUnsafe();
        } catch (SecurityException se) {
            Class<Unsafe> ucl = Unsafe.class;
            try {
                return AccessController.doPrivileged((PrivilegedExceptionAction<Unsafe>) () ->
                        Arrays.stream(ucl.getDeclaredFields())
                        .filter(f -> ucl.isAssignableFrom(f.getType()))
                        .findAny()
                        .map(f -> {
                            try {
                                f.setAccessible(true);
                                return ucl.cast(f.get(null));
                            } catch (Exception e) {
                                return null;
                            }
                        }).orElseThrow(IllegalAccessError::new));
            } catch (PrivilegedActionException pe) {
                return null;
            }
        }
    }


    private MemoryUtil() {
    }
}
