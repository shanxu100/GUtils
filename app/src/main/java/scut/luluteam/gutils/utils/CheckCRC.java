package scut.luluteam.gutils.utils;

/**
 * Created by Guan on 2017/9/23.
 */
public class CheckCRC {

    private CheckCRC() {
        throw new AssertionError("工具类不可实例化");
    }

    public static void main(String[] args) {
        String str = "aa";
        System.out.println(str.getBytes()[0]);
        byte[] data = {0x61, 0x61};
        byte[] result = CheckCRC.Make_CRC(data);
        System.out.println("CRC：" + result);
        System.out.println(Integer.toBinaryString((result[0] & 0xFF) + 0x100).substring(1) + " " +
                Integer.toBinaryString((result[1] & 0xFF) + 0x100).substring(1));
    }

    public static boolean check(byte[] data, byte[] CRC16_Bytes) {
        byte[] result = Make_CRC(data);
        return CRC16_Bytes[0] == result[0] && CRC16_Bytes[1] == result[1];
    }

    /**
     * 计算产生校验码
     * <p>
     * 名称          生成多项式              简记式*   标准引用
     * CRC-16        x16+x15+x2+1            8005      IBM SDLC
     *
     * @param data 需要校验的数据
     * @return 校验码
     */
    public static byte[] Make_CRC(byte[] data) {
        byte[] buf = new byte[data.length];// 存储需要产生校验码的数据
        for (int i = 0; i < data.length; i++) {
            buf[i] = data[i];
        }
        int len = buf.length;
        int crc = 0xFFFF;//16位
        for (int pos = 0; pos < len; pos++) {
            if (buf[pos] < 0) {
                crc ^= (int) buf[pos] + 256; // XOR byte into least sig. byte of
                // crc
            } else {
                crc ^= (int) buf[pos]; // XOR byte into least sig. byte of crc
            }
            for (int i = 8; i != 0; i--) { // Loop over each bit
                if ((crc & 0x0001) != 0) { // If the LSB is set
                    crc >>= 1; // Shift right and XOR 0xA001
                    /**
                     * 多项式: A001
                     */
                    crc ^= 0xA001;
                } else {
                    // Else LSB is not set
                    crc >>= 1; // Just shift right
                }
            }
        }
        //String c = Integer.toHexString(crc);
        byte[] CRC_Bytes = ByteUtil.toByteArray(crc);

        return CRC_Bytes;
    }

    public static byte[] Make_CRC(byte[] bytes, int offset, int length) {
        byte[] tmp_bytes = new byte[length];
        System.arraycopy(bytes, offset, tmp_bytes, 0, length);
        return Make_CRC(tmp_bytes);
    }

    public static byte[] Make_CRC(byte[] lengthBytes, byte[] dataBytes) {
        int count1 = lengthBytes.length;
        int count2 = dataBytes.length;
        byte[] data = new byte[count1 + count2];
        System.arraycopy(lengthBytes, 0, data, 0, count1);
        System.arraycopy(dataBytes, 0, data, count1, count2);
        return Make_CRC(data);
    }

}
