package org.petermac.pathos.vcfflow;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class VariantInfo {
    private final String srcTxt;
    private ArrayList<String> fldNames;
    private ArrayList<Object> fldValues;

    public VariantInfo(String srcTxt) {
        this.srcTxt = srcTxt;
    }

    public Integer fieldAsInt(String name) {
        Integer idx = indexOf(name);
        if (idx == null) {
            return null;
        }
        if (fldValues.get(idx) instanceof String) {
            String v = (String) fldValues.get(idx);
            Integer w = Integer.parseInt(v);
            fldValues.set(idx, w);
        }
        return (Integer) fldValues.get(idx);
    }

    public ArrayList<Integer> fieldAsIntList(String name) {
        Integer idx = indexOf(name);
        if (idx == null) {
            return null;
        }
        if (fldValues.get(idx) instanceof String) {
            String v = (String) fldValues.get(idx);
            ArrayList<Integer> ws = new ArrayList<>();
            int p = 0;
            int q = v.indexOf(",", p);
            while (q >= 0) {
                String vv = v.substring(p, q);
                Integer w = Integer.parseInt(vv);
                ws.add(w);
                p = q + 1;
                q = v.indexOf(",", p);
            }
            String vv = v.substring(p);
            Integer w = Integer.parseInt(vv);
            ws.add(w);
            fldValues.set(idx, ws);
        }
        //noinspection unchecked
        return (ArrayList<Integer>) fldValues.get(idx);
    }

    public Double fieldAsFlt(String name) {
        Integer idx = indexOf(name);
        if (idx == null) {
            return null;
        }
        if (fldValues.get(idx) instanceof String) {
            String v = (String) fldValues.get(idx);
            Double w = Double.parseDouble(v);
            fldValues.set(idx, w);
        }
        return (Double) fldValues.get(idx);
    }

    public ArrayList<Double> fieldAsFltList(String name) {
        Integer idx = indexOf(name);
        if (idx == null) {
            return null;
        }
        if (fldValues.get(idx) instanceof String) {
            String v = (String) fldValues.get(idx);
            ArrayList<Double> ws = new ArrayList<>();
            int p = 0;
            int q = v.indexOf(",", p);
            while (q >= 0) {
                String vv = v.substring(p, q);
                Double w = Double.parseDouble(vv);
                ws.add(w);
                p = q + 1;
                q = v.indexOf(",", p);
            }
            String vv = v.substring(p);
            Double w = Double.parseDouble(vv);
            ws.add(w);
            fldValues.set(idx, ws);
        }
        //noinspection unchecked
        return (ArrayList<Double>) fldValues.get(idx);
    }

    public String fieldAsStr(String name) {
        Integer idx = indexOf(name);
        if (idx == null) {
            return null;
        }
        return (String) fldValues.get(idx);
    }

    @Nullable
    private Integer indexOf(String name) {
        if (fldNames == null) {
            indexFields();
        }
        for (int i = 0; i < fldNames.size(); i++) {
            if (name.equals(fldNames.get(i))) {
                return i;
            }
        }
        return null;
    }

    private void indexFields() {
        fldNames = new ArrayList<>();
        fldValues = new ArrayList<>();
        int p = 0;
        Integer q = srcTxt.indexOf(";");
        while (q >= 0) {
            Integer r = srcTxt.indexOf("=", p);
            if (r == -1 || r > q) {
                String nm = srcTxt.substring(p, q);
                String val = ".";
                fldNames.add(nm);
                fldValues.add(val);
            } else {
                String nm = srcTxt.substring(p, r).intern();
                String val = srcTxt.substring(r + 1, q);
                fldNames.add(nm);
                fldValues.add(val);
            }
            p = q + 1;
            q = srcTxt.indexOf(";", p);
        }
        int r = srcTxt.indexOf("=", p);
        if (r == -1) {
            String nm = srcTxt.substring(p);
            String val = ".";
            fldNames.add(nm);
            fldValues.add(val);

        } else {
            String nm = srcTxt.substring(p, r);
            String val = srcTxt.substring(r+1);
            fldNames.add(nm);
            fldValues.add(val);
        }
    }
}
