package Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public  class  Constants {
    // ������� ��������
    public final static String operations = "-+*/";
    // ���������� ��������� ��� ��������
    public final static String regExpOperations = "\\s*[" + operations + "]\\s*";
    // ���������� ����� ���������
    public final static int countOperand = 2;
    // ��������� ������� ����� �� ���������
    public final static HashMap<Integer, Integer> romeNumberTable = new HashMap<>() {{
        put(73, 1);
        put(86, 5);
        put(88, 10);
    }};
    public final static String[] unitsRome = new String[]{
            "",
            "I",
            "II",
            "III",
            "IV",
            "V",
            "VI",
            "VII",
            "VIII",
            "IX",
    };
    // ��� ��������� ������� �����
    public static ArrayList<Byte> allRomeNumber = new ArrayList(Arrays.asList(73, 86, 88, 76, 67, 68, 77));
}
