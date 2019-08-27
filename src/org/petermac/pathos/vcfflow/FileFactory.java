package org.petermac.pathos.vcfflow;

import java.io.*;
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
}
