package org.petermac.pathos.vcfflow.hgvs;

import org.petermac.pathos.vcfflow.VariantGenotype;
import org.petermac.pathos.vcfflow.VariantHandler;
import org.petermac.pathos.vcfflow.VariantInfo;

public class HgvsVcfHandler implements VariantHandler {
    private HgvsGProcessor processor;

    public HgvsVcfHandler(HgvsGProcessor processor) {
        this.processor = processor;
    }

    @Override
    public boolean apply(String chrom, Integer pos, String id, String ref, String alt, Double qual, String filter, VariantInfo info, VariantGenotype[] genotypes) {
        int p = 0;
        int q = alt.indexOf(',', p);
        while (q >= 0) {
            String s = alt.substring(p, q);
            apply1(chrom, pos, ref, alt);
            p = q + 1;
            q = alt.indexOf(',', p);
        }
        String s = alt.substring(p);
        apply1(chrom, pos, ref, alt);
        return true;
    }
    public void apply1(String chrom, Integer pos, String ref, String alt) {
        assert(!alt.contains(","));
        if (ref.length() == 1 && alt.length() == 1) {
            processor.sub(chrom, pos, ref, alt);
            return;
        }
        if (ref.length() == 1 && alt.length() > 1) {
            Integer pos1 = pos + 1;
            processor.ins(chrom, pos, pos1, alt.substring(1));
            return;
        }
        if (ref.length() > 1 && alt.length() == 1) {
            int n = ref.length() - 1;
            Integer pos1 = pos + 1;
            Integer pos1n = pos + 1 + n;
            processor.del(chrom, pos1, pos1n);
            return;
        }
        int n = ref.length() - 1;
        int pos1 = pos + 1;
        int pos1n = pos + 1 + n;
        processor.delins(chrom, pos1, pos1n, alt.substring(1));
        return;
    }
}
