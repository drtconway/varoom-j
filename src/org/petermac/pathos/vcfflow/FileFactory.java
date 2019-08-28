package org.petermac.pathos.vcfflow;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileFactory {
    public InputStream in(String fileName) throws IOException {
        if (fileName.equals("-")) {
            return System.in;
        }
        FileInputStream fin = new FileInputStream(fileName);
        if (fileName.endsWith(".gz")) {
            return new GZIPInputStream(fin);
        }
        return fin;
    }

    public OutputStream out(String fileName) throws IOException {
        if (fileName.equals("-")) {
            return System.out;
        }
        FileOutputStream fout = new FileOutputStream(fileName);
        if (fileName.endsWith(".gz")) {
            return new GZIPOutputStream(fout);
        }
        return fout;
    }

    public static class MapResult {
        public final ByteBuffer map;
        public final long length;

        public MapResult(ByteBuffer map, long length) {
            this.map = map;
            this.length = length;
        }
    }
    public MapResult map(String fileName) throws IOException {
        RandomAccessFile file = new RandomAccessFile(fileName, "r");
        FileChannel fileChannel = file.getChannel();
        ByteBuffer mres = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        return new MapResult(mres, file.length());
    }
}
