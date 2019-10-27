package Utils;

import newException.ErrorSearchRomeNumber;

import java.util.Map;

public class Util {
    // ��������� ���������� ���������� � ���������
    public static void kickException(Exception e) {
        // ������� ���������
        System.out.println(e.getMessage());
        // ���������� ������ ���������
        System.exit(1);
    }

    public static int transferRomeInArabNumber(String romeNumber) {

        // ������ �������� �����, ��������� �������� ������ ��������
        int result;
        // ������ ����� �������� �����
        byte[] b = romeNumber.getBytes();
        result = searchRome(b[0]);
        int lastNumber = result;
        // �������� �� ������� �������� �����
        for (int i = 1; i < b.length; i++) {
            // ������ ������ �� �������� �����
            int n = searchRome(b[i]);
            // ���� �������� ������ ������ ��� ���������
            if (lastNumber < n) {
                result = n - result;
            } else {
                result += n;
            }
            lastNumber = n;
        }
        return result;
    }

    // ����� �������� ����� � �������
    private static int searchRome (byte b) {
        for (Map.Entry entry: Constants.romeNumberTable.entrySet()) {
            if ((int)entry.getKey() == (int)b)
                return (int)entry.getValue();
        }
        Util.kickException(new ErrorSearchRomeNumber("������ ������� ����� ����������."));
        return 0;// �������
    }

    public static String unitsRomeToString(String[] massive) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < massive.length; i++) {
            stringBuilder.append(massive[i]);
            if (i + 1 != massive.length)
                stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

}
