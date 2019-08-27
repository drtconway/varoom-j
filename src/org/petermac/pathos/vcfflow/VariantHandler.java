package org.petermac.pathos.vcfflow;

public interface VariantHandler {
    boolean apply(String chrom, Integer pos, String id, String ref, String alt, Double qual, String filter, VariantInfo info, VariantGenotype[] genotypes);
}
