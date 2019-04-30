package scut.luluteam.gutils.utils.frame.guan;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Guan
 * @date Created on 2019/3/3
 */
public class FrameManager {

    /**
     *
     */
    private static GArrayBlockingQueue<Byte> queue = new GArrayBlockingQueue<>(2048);
    private static ExecutorService executor = Executors.newSingleThreadExecutor();


    static {
        /**
         * 静态代码块：在加载该类的时候就开始工作
         */
        work();
    }

    /**
     * TODO 数据的入口：无论通过什么途径接收到的数据，都要入队
     *
     * @param bytes
     */
    public static void put(byte[] bytes) {
        Byte[] arr = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            arr[i] = bytes[i];
        }
        queue.offerAll(arr);
    }

    /**
     * 开启新线程对queue中的数据帧进行识别
     */
    private static void work() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        byte[] frameByte = takeCustomFrame2();
                        if (frameByte != null) {
//                            System.out.println("识别一个帧：" + ByteUtil.byte2hex(frameByte));
                            CustomFrame customFrame = CustomFrame.Builder.decode(frameByte);
                            FrameProcessor.process(customFrame);
                        } else {
                            //未识别出来数据帧，可能
                            Thread.sleep(500);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    /**
     * TODO 按照应用层帧的格式，在queue中解析数据。
     * 要根据不同的协议做不同的解析
     *
     * @return
     * @throws InterruptedException
     */
    private static byte[] takeCustomFrame2() throws InterruptedException {


        while (queue.peekWithBlock() != CustomFrame.SOF_FRAME) {
            queue.poll();
        }
        //认定当前是SOF后，进行下面的识别
        //如果识别过程中有任何一个条件不满足，就认为当前不是SOF

        Object[] objects = queue.toArray();
        if (objects.length >= 6) {
            //一个帧的最小长度是6
            byte[] totalByte = new byte[objects.length];
            for (int i = 0; i < objects.length; i++) {
                totalByte[i] = (byte) objects[i];
            }

//            System.out.println("queue的样子："+ByteUtil.byte2hex(totalByte));

            byte[] length = new byte[CustomFrame.LENGTH_SIZE];
            length[0] = totalByte[1];
            length[1] = totalByte[2];
            int len = CustomFrame.toInteger(length[0], length[1]);
            if (len > CustomFrame.MAX_LENGTH) {
                //长度字段超出最大长度，说明这是一个无效的字段，舍弃takeIndex所指的数据
                //即移除一个元素
                queue.poll();
                return null;
            }
            if (6 + len > queue.size()) {
                //长度字段超过了队列中元素的个数，说明还有一些的数据没有发过来，即发生了 “半包”
//                System.out.println("出现半包情况---------");
                return null;
                //这样是有问题的：比如只发了一半的数据帧，就不发了，那么这个队列就永远不会继续读出数据了
            }
            //如果不是上面的任何一种情况，说明“帧”已经在这个totalByte里面了
            //所以跳过data和crc字段，只需要匹配EOF是不是一致就行
            int eofIndex = 1 + 2 + len + 2;
            if (CustomFrame.EOF_FRAME == totalByte[eofIndex]) {
                //说明这是一个帧
                byte[] result = new byte[6 + len];
                for (int i = 0; i < 6 + len; i++) {
                    result[i] = queue.take();
                }
                return result;
            }
            //否则，不是一个帧
            queue.poll();
        }
        //一个帧的最小长度是6，但是queue里面的元素个数小于6，说明这肯定不是一个帧
        return null;
    }

}
