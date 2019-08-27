package org.petermac.pathos.vcfflow;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;

@SuppressWarnings("WeakerAccess")
public class Fasta {
    public final String label;
    public final String sequence;

    public Fasta(String label, String sequence) {
        this.label = label;
        this.sequence = sequence;
    }

    public static Fasta readFasta(FileFactory ffac, String fileName) {
        final Fasta[] res = {null};
        readFasta(ffac, fileName, (lab, seq) -> {
            res[0] = new Fasta(lab, seq);
        });
        return res[0];
    }

    public static void readFasta(FileFactory ffac, String fileName, List<Fasta> res) {
        readFasta(ffac, fileName, (lab, seq) -> {
            res.add(new Fasta(lab, seq));
        });
    }

    public static void readFasta(FileFactory ffac, String fileName, BiConsumer<String,String> handler) {
        try (Scanner scanner = new Scanner(ffac.in(fileName))) {
            String lab = null;
            StringBuilder bldr = new StringBuilder();
            while (scanner.hasNext()){
                final String line = scanner.nextLine().trim();
                if (line.startsWith((">"))) {
                    if (lab != null) {
                        handler.accept(lab, bldr.toString());
                        int l = bldr.length();
                        bldr.delete(0, l);
                    }
                    lab = line.substring(1);
                    continue;
                }
                bldr.append(line);
            }
            if (lab != null) {
                handler.accept(lab, bldr.toString());
                int l = bldr.length();
                bldr.delete(0, l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
