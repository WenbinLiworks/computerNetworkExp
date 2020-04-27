package com.bit.tools;

public class CRC {

    public static int CRC_Remainder(byte[] bytes) {
        // crc = 1111,1111,1111,1111
        // polynomial = 0x11021生成式
        int crc = 0xFFFF, polynomial = 0x1021;

        // 求余数
        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = (b >> (7 - i) & 1) == 1;  //最左边是不是为1
                boolean c15 = (crc >> 15 & 1) == 1;
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xFFFF;
        return crc;
    }

    public static byte[] CRC_Bytes(byte[] bytes) {
        int crc = CRC_Remainder(bytes), l = bytes.length;
        byte[] crc_bytes = new byte[l + 2];
        System.arraycopy(bytes, 0, crc_bytes, 0, l);
        crc_bytes[l] = (byte)((crc & 0xFF00) >> 8); //左8位
        crc_bytes[l + 1] = (byte)(crc &0x00FF);//右八位
        return crc_bytes;
    }


}
