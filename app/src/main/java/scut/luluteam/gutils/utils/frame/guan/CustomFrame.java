package scut.luluteam.gutils.utils.frame.guan;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Guan
 */
public class CustomFrame {
    public static final int HEADER_SIZE = 1;
    public static final int LENGTH_SIZE = 2;
    public static final int CRC16_SIZE = 2;
    public static final int TAIL_SIZE = 1;

    public static final byte SOF_FRAME = 0x5E;
    public static final byte EOF_FRAME = 0x24;
    public static final int MAX_LENGTH = 2048;

    int totalLength = 0;
    private byte header;
    private byte[] length;
    private byte[] data;
    private byte[] crc16;
    private byte tail;
    private Object tag;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public byte getHeader() {
        return header;
    }

    public void setHeader(byte header) {
        this.header = header;
    }

    public byte[] getLength() {
        return length;
    }

    public void setLength(byte[] length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getCrc16() {
        return crc16;
    }

    public void setCrc16(byte[] crc16) {
        this.crc16 = crc16;
    }

    public byte getTail() {
        return tail;
    }

    public void setTail(byte tail) {
        this.tail = tail;
    }

    public int getIntLength() {
        if (length != null) {
            return toInteger(length[0], length[1]);
        } else {
            return 0;
        }
    }

    public int getTotalLength() {
        totalLength = data.length + HEADER_SIZE + LENGTH_SIZE + CRC16_SIZE + TAIL_SIZE;
        return totalLength;
    }

    public CustomFrame() {

    }

    private CustomFrame(byte header, byte[] length) {
        this.header = header;
        this.length = new byte[length.length];
        System.arraycopy(length, 0, this.length, 0, length.length);
    }

    public CustomFrame(byte header, byte[] length, byte[] data, byte[] crc16, byte tail) {
        this.header = header;
        this.length = length;
        this.data = data;
        this.crc16 = crc16;
        this.tail = tail;
    }

    /**
     * 由CustomFrame对象，转换为 byte[]
     *
     * @return
     */
    public byte[] toBytes() {
        totalLength = data.length + 6;
        byte[] result = new byte[totalLength];
        result[0] = header;
        System.arraycopy(length, 0, result, 1, 2);
        System.arraycopy(data, 0, result, 3, getIntLength());
        System.arraycopy(crc16, 0, result, totalLength - 3, 2);
        result[totalLength - 1] = tail;
        return result;
    }

    /**
     * 将byte[]转换为CustomFrame对象，即解码
     * 注意：这里只是将数组解析成对应的帧，不是处理原始数据的拆包和粘包问题
     */
    public static class Builder {

        private static final String TAG = "CustomFrame.Builder";


        /**
         * byte[]里面包含多个帧，一次性解析并封装多个对象
         *
         * @param bytes
         * @return
         */
        public static List<CustomFrame> decodeFrames(byte[] bytes) {
            List<CustomFrame> list = new ArrayList<>();
            int readIndex = 0;
            while (readIndex < bytes.length) {
                CustomFrame customFrame = decode(bytes, readIndex);
                readIndex = (int) customFrame.getTag();
                list.add(customFrame);
            }
            return list;
        }

        /**
         * byte[]里面包含一个帧，将这个帧封装成对象
         *
         * @param bytes
         * @return
         */
        public static CustomFrame decode(byte[] bytes) {
            return decode(bytes, 0);
        }


        //==========================================================================
        //
        //==========================================================================

        /**
         * 具体地封装对象
         *
         * @param bytes
         * @param readIndex
         * @return
         */
        private static CustomFrame decode(byte[] bytes, int readIndex) {
            if (bytes == null || bytes.length < 6) {
                return null;
            }
            //校验 SOF
            if (bytes[readIndex] != SOF_FRAME) {
                printErrorFrame("帧头部校验错误", bytes);
                return null;
            }
            readIndex++;

            //校验长度
            int dLength = toInteger(bytes[readIndex], bytes[readIndex + 1]);
            if (dLength > MAX_LENGTH - 6) {
                //超出最大长度
                printErrorFrame("数据帧超出最大长度", bytes);
                return null;
            }
            byte[] length = new byte[2];
            System.arraycopy(bytes, readIndex, length, 0, 2);
            readIndex += 2;

            //获取data数据
            byte[] data = new byte[dLength];
            System.arraycopy(bytes, readIndex, data, 0, dLength);
            readIndex += dLength;

            //获取CRC数据
            byte[] crc = new byte[2];
            System.arraycopy(bytes, readIndex, crc, 0, 2);
            readIndex += 2;

            //校验 EOF
//        int EOFIndex = 1 + 2 + dLength + 2;
            if (bytes[readIndex] != EOF_FRAME) {
                printErrorFrame("帧尾部校验错误", bytes);
                return null;
            }
            readIndex++;

            CustomFrame customFrame = new CustomFrame(SOF_FRAME, length, data, crc, EOF_FRAME);
            customFrame.setTag(readIndex);
            return customFrame;
        }

        private static void printErrorFrame(String title, byte[] bytes) {
            Log.e(TAG, " Frame Decoder:  " + title + " --frame: " + byte2hex(bytes));
        }
    }


    @Override
    public String toString() {
        return "CustomFrame_HEX: {" +
                "header:" + byte2hex(new byte[]{header})
                + "  length:" + byte2hex(length)
                + "  data: " + byte2hex(data)
                + "  crc16:" + byte2hex(crc16)
                + "  tail:" + byte2hex(new byte[]{tail})
                + '}';
    }

    /**
     * 将2字节byte[]数组型数据转换为0~65535
     * byte[]高位在前，低位在后
     *
     * @param byte0
     * @param byte1
     * @return
     */
    public static int toInteger(byte byte0, byte byte1) {
        int result = 0;
        result += (byte0 & 0x0FF) << 8;
        result += byte1 & 0x0FF;
        return result;
    }

    /**
     * 将byte以16进制的形式打印出来
     *
     * @param buffer
     * @return
     */
    public static String byte2hex(byte[] buffer) {
        String h = "";

        for (int i = 0; i < buffer.length; i++) {
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + " " + temp;
        }

        return h;
    }

}
