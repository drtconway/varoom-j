package org.petermac.pathos.vcfflow;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Stream;

public class VcfFlow {
    private final VariantHandler handler;

    VcfFlow(VariantHandler handler) {
        this.handler = handler;
    }

    public  void process(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNext()){
                final String line = scanner.nextLine();
                boolean cont = processLine(line);
                if (!cont) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean processLine(String line) {
        if (line.startsWith("#")) {
            return true;
        }
        ArrayList<String> flds = splitLine(line);
        String chrom = flds.get(0);
        Integer pos = Integer.parseInt(flds.get(1));
        String id = flds.get(2);
        String ref = flds.get(3).intern();
        String alt = flds.get(4).intern();
        Double qual = null;
        if (!".".equals(flds.get(5))) {
            qual = Double.parseDouble(flds.get(5));
        }
        String filter = flds.get(6);
        VariantInfo info = new VariantInfo(flds.get(7));
        int n = flds.size() - 9;
        VariantGenotype[] genotypes = new VariantGenotype[n];
        for (int i = 0; i < n; ++i) {
            genotypes[i] = new VariantGenotype(flds.get(9+i));
        }
        return handler.apply(chrom, pos, id, ref, alt, qual, filter, info, genotypes);
    }

    private ArrayList<String> splitLine(String line) {
        ArrayList<String> res = new ArrayList<>();
        int p = 0;
        int q = line.indexOf("\t");
        return getStrings(line, res, p, q);
    }

    @NotNull
    private static ArrayList<String> getStrings(String line, ArrayList<String> res, int p, int q) {
        while (q >= 0) {
            String fld = line.substring(p, q);
            res.add(fld);
            p = q + 1;
            q = line.indexOf("\t", p);
        }
        String fld = line.substring(p);
        res.add(fld);
        return res;
    }
}
