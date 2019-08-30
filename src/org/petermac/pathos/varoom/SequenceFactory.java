package org.petermac.pathos.varoom;

import java.io.File;

public class SequenceFactory {
    private final FileFactory fileFactory;
    private final String home;

    public SequenceFactory(FileFactory fileFactory, String home) {
        this.fileFactory = fileFactory;
        this.home = home;
    }

    public Fasta getAccession(String accession) {
        String acc = accession;
        File f;
        if (SeqUtils.Hg19.refSeq2Chr.containsKey(acc)) {
            acc = SeqUtils.Hg19.refSeq2Chr.get(acc);
        }
        f = new File(home, acc);
        if (f.canRead()) {
            return Fasta.readFasta(fileFactory, f.getPath());
        }
        f = new File(home, acc + ".gz");
        if (f.canRead()) {
            return Fasta.readFasta(fileFactory, f.getPath());
        }
        return null;
    }
}
