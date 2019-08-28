package org.petermac.pathos.vcfflow;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class StringFileFactory extends FileFactory {
    public HashMap<String,String> files;

    public StringFileFactory() {
        files = new HashMap<>();
    }

    @Override
    public InputStream in(String fileName) {
        String txt = files.get(fileName);
        return new ByteArrayInputStream(txt.getBytes());
    }

    private static class CapturingOutputStream extends ByteArrayOutputStream {
        private final HashMap<String, String> files;
        private final String fileName;

        CapturingOutputStream(HashMap<String,String> files, String fileName) {
            this.files = files;
            this.fileName = fileName;
        }

        @Override
        public void close() {
            files.put(fileName, this.toString());
        }
    }

    @Override
    public OutputStream out(String fileName) {
        return new CapturingOutputStream(files, fileName);
    }

    @Override
    public MapResult map(String fileName) throws UnsupportedEncodingException {
        String txt = files.get(fileName);
        byte[] bytes = txt.getBytes("US-ASCII");
        ByteBuffer res = ByteBuffer.wrap(bytes);
        return new MapResult(res, bytes.length);
    }
}
