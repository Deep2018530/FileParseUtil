package com.deepz.fileparse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author DeepSleeping
 * @date 2019/7/29 16:06
 * @description
 */
public class StreamUtils {

    /**
     * @return
     * @description 将输入流转换为字节流数组，方便输入流的复用(解决了 Stream Closed、Inpustream have must > 0 bytes异常)
     * @author DeepSleeping
     * @date 2019/7/29 16:05
     */
    public static byte[] inputToByteArray(InputStream inputStream) {
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            try {
                if (!((len = inputStream.read(buffer)) > -1)) break;
                byte[] bytes = baos.toByteArray();
                baos.write(buffer, 0, len);
                baos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }
}
