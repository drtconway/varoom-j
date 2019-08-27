package org.petermac.pathos.vcfflow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoFormat {
    private final String nameStr;
    private final Type typeVal;
    private final String descrStr;

    public enum Type { INT, FLT, CHR, STR }

    private static final Pattern p;

    static {
        p = Pattern.compile("<ID=([^,]+),Number=(A|G|[0-9]+),Type=(Integer|Float|String|Character),Description=\"([^\"]+)\"(.*)>");
    }

    public static InfoFormat make(String srcTxt) {
        Matcher m = p.matcher(srcTxt);
        if (!m.matches()) {
            return null;
        }
        String nm = m.group(1);
        String typeStr = m.group(2);
        String numberStr = m.group(3);
        String descrStr = m.group(4);
        return new InfoFormat(nm, strToType(typeStr), descrStr);
    }

    private static Type strToType(String typeStr) {
        switch (typeStr) {
            case "Integer":     return Type.INT;
            case "Float":       return Type.FLT;
            case "String":      return Type.STR;
            case "Character":   return Type.CHR;
        }
        return null;
    }

    private InfoFormat(String nameStr, Type typeVal, String descrSttr) {
        this.nameStr = nameStr;
        this.typeVal = typeVal;
        this.descrStr = descrSttr;
    }

    public String name() {
        return nameStr;
    }

    public Type type() {
        return typeVal;
    }

    public String description() {
        return descrStr;
    }

}
