package com.silver.fox.media.audio.pcm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PcmUtils {
    private static float normalizeVolume(int volume) {
        return volume / 100f * 1;
    }

    public static void mixPcm(String pcm1Path, String pcm2Path, String toPath
            , int vol1, int vol2) throws IOException {
        float volume1 = normalizeVolume(vol1);
        float volume2 = normalizeVolume(vol2);
        byte[] buffer1 = new byte[2048];
        byte[] buffer2 = new byte[2048];
        byte[] buffer3 = new byte[2048];

        FileInputStream is1 = new FileInputStream(pcm1Path);
        FileInputStream is2 = new FileInputStream(pcm2Path);

        FileOutputStream fileOutputStream = new FileOutputStream(toPath);

        boolean end1 = false, end2 = false;
        short temp2, temp1;
        int temp;
        try {
            while (!end1 || !end2) {
                if (!end1) {
                    end1 = (is1.read(buffer1) == -1);

                    System.arraycopy(buffer1, 0, buffer3, 0, buffer1.length);
                }
                if (!end2) {
                    end2 = (is2.read(buffer2) == -1);
                    int voice = 0;
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < buffer2.length; i += 2) {
                        temp1 = (short) ((buffer1[i] & 0xff) | (buffer1[i + 1] & 0xff) << 8);
                        stringBuilder.append(temp1 + " ");
                        temp2 = (short) ((buffer2[i] & 0xff) | (buffer2[i + 1] & 0xff) << 8);
                        temp = (int) (temp2 * volume2 + temp1 * volume1);
                        if (temp > 32767) {
                            temp = 32767;
                        } else if (temp < -32768) {
                            temp = -32768;
                        }
                        buffer3[i] = (byte) (temp & 0xFF);
                        buffer3[i + 1] = (byte) ((temp >>> 8) & 0xFF);
                    }
                }
                fileOutputStream.write(buffer3);
            }
        } finally {
            is1.close();
            is2.close();
            fileOutputStream.close();
        }
    }
}
