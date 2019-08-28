package org.petermac.pathos.vcfflow;

import java.io.IOException;
import java.io.OutputStream;

public class SeqStore {
    private final FileFactory fileFactory;

    public SeqStore(FileFactory fileFactory) {
        this.fileFactory = fileFactory;
    }

    public void storeSequence(Fasta fasta, String fileName) throws IOException {
        OutputStream out = fileFactory.out(fileName);
        out.write(fasta.sequence.getBytes());
        out.flush();
        out.close();
    }

    public Rope loadRope(String fileName) throws IOException {
        FileFactory.MapResult m = fileFactory.map(fileName);
        return Rope.atom(m.map, (int)m.length);
    }
}
