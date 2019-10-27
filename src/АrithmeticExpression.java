import Utils.Constants;
import Utils.Util;
import newException.ErrorOperand;
import newException.NoValidOperationFound;
import newException.UnsupportedNumber;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class �rithmeticExpression  {
    private String expression; //  ��������� ��������� � �����������
    private String operation; //  ���������������� ��� ��������� ��������� �������� treatmentOperation()
    private ArrayList<Integer> numbersInt; // ���������������� ��� ��������� ���������
    private ArrayList<String> numbersRome;
    private boolean isInt;

    public �rithmeticExpression(String expression) {
        numbersInt = new ArrayList<>();
        numbersRome = new ArrayList<>();
        this.expression = expression;
        treatmentExpression();
        if (!numbersInt.isEmpty())
            isInt = true;
    }

    public String getOperation() {
        return operation;
    }

    public ArrayList<Integer> getNumbers() {
        return numbersInt;
    }

    public ArrayList<String> getNumbersRome() {
        return numbersRome;
    }

    public boolean isInt() {
        return isInt;
    }

    public void setInt(boolean anInt) {
        isInt = anInt;
    }

    // ��������� ����� ���������
    private void treatmentExpression() {
        // ��������� �������� ��������
        // �������� �� �� ����������, ���������� ��������
        // � �� ������������ ������ �������� � ������������
        // ���������������� ��������� ���� ��������
        operation = treatmentOperation();
        // ��������� ��������� �����
        treatmentNumber();
    }

    // ��������� �������� ��������
    // ���������� ��������� ��������
    // � ������ ������� ���������
    private String treatmentOperation() {
        Pattern pattern = Pattern.compile(Constants.regExpOperations);
        Matcher matcher = pattern.matcher(expression);
        String searchOperaion = null;
        int countOperation = 0;
        while (matcher.find()) {
            searchOperaion = matcher.group();
            countOperation++;
        }
        if (countOperation == 0)
            Util.kickException(new NoValidOperationFound("� ��������� �� ������� ���������� ��������. " +
                    "���������� ��������: ��������, ���������, ���������, �������."));
        else if (countOperation > 1)
            Util.kickException(new NoValidOperationFound("�������� ��� ���������� �������� ������ ����."));
        return searchOperaion.trim();
    }

    // ��������� ����� � ���������
    private void treatmentNumber() {
        // ��������� ������ �� �������� ����� �������� � ������� ������ �����
        String[] numbersString = expression.trim().split(Constants.regExpOperations);
        // containNumberInt � containNumberRome - ��� �������� �� ���� ���������
        // sybmolIsInt � sybmolIsRome - ��� �������� � ����� �����
        boolean sybmolIsInt, sybmolIsRome, containNumberInt = false, containNumberRome = false;
        // ������ ��� ���������� �������� ������ �������
        StringBuilder stringBuilder = new StringBuilder();
        // �������� �� ������ � ���������� ����� � ������ ������
        for (int i = 0; i < numbersString.length; i++) {
            sybmolIsInt = false;
            sybmolIsRome = false;
            byte[] symbol = numbersString[i].getBytes();
            // ���� ����� ������� �� �� ������ �����
            for (int j = 0; j < symbol.length; j++) {
                byte b = symbol[j];
                if (b == 32 || b == 9) //  ������ ��� ���������
                    continue;
                //if (b == ) ������� ������ � ������� �������
                if (b >= 48 && b <= 57) // �� [0, 10]
                    containNumberInt = sybmolIsInt = true;
                else if (Constants.allRomeNumber.contains(new Integer(b))) // ��������� ���� �� ����� ������� �����
                    containNumberRome = sybmolIsRome = true;
                else if (!(sybmolIsInt && sybmolIsRome)) // ���� �� �������� � �� ������� ����� �� ����������
                    Util.kickException(new UnsupportedNumber("� ��������� ������������ �� ���������� �������."));
                stringBuilder.append((char)b);
            }
            if (sybmolIsInt && sybmolIsRome) //  ��� �������� ����� ������ �������� ��� ���������� �������� � ������� ����� "1X"
                Util.kickException(new UnsupportedNumber("� ��������� ���������� ������������� �������� � ������� ���� ������ � ����� ��������."));
            if (sybmolIsInt)
                numbersInt.add(Integer.valueOf(stringBuilder.toString()));
            if (sybmolIsRome)
                numbersRome.add(stringBuilder.toString());
            stringBuilder.delete(0, stringBuilder.length());
        }
        if (containNumberInt && containNumberRome) //  ��� ������� � ��������� ������ �������� �� �������� � ������� �� ������� ����
            Util.kickException(new UnsupportedNumber("� ��������� ���������� ������������� �������� � ������� ���� ������ � ����� ���������."));
        int sizeNumberInt = numbersInt.size();
        int sizeNumberRome = numbersRome.size();
        // ���� ��������� ������ ��� ������ ����������
        if (sizeNumberInt + sizeNumberRome > Constants.countOperand)
            Util.kickException(new ErrorOperand(String.format("��������� ����� ������� �� ����� ", Constants.countOperand)));
        else if (sizeNumberInt + sizeNumberRome < Constants.countOperand)
            Util.kickException(new ErrorOperand(String.format("��������� ������ ���� %d", Constants.countOperand)));
        // ���� �������� �������� �����
        if (containNumberInt)
            checkRangeInt();
        // ���� �������� ������� �����
        if (containNumberRome)
            treatmentRomeNumber();
    }

    // ��������� ����� �� ��������� � ��������[1, 10]
    private void checkRangeInt() {
        // ������� ��������� ����� �� �������� � �������� [1, 10]
        ArrayList<Integer> errorNumbers = new ArrayList<>();
        // ��������� ����� �� ��������� � ��������[1, 10]
        for (int i = 0; i < numbersInt.size(); i++)
            if (numbersInt.get(i) < 1 || numbersInt.get(i) > 10)
                errorNumbers.add(numbersInt.get(i));
        // ���� ���� ����� �� �������� � �������� �� �������� ���������� � ��������� ������
        if (!errorNumbers.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Integer i: errorNumbers)
                sb.append(i + ", ");
            sb.delete(sb.length() - 2, sb.length());
            Util.kickException(new UnsupportedNumber(String.format("������� ����� �� �������� � ���������� ��������: %s.", sb.toString())));
        }
    }

    // ��������� ������� ����
    private void treatmentRomeNumber() {
        byte[] rome;
        boolean isExist;// ���� ������, true - ��� ������
        for (String s: numbersRome) {
            isExist = false; // ���������� ������������ ��� ������ ����������� � �������
            rome = s.getBytes();
            for (byte b: rome) { // III + L
                for (Map.Entry entry: Constants.romeNumberTable.entrySet()) {
                    if ((int)entry.getKey() == (int)b) // ���� � ������� ����������� ������ ������ ���������� ���� ������
                        isExist = true;
                }
                if (isExist == false) // ��������� ������ �� � �������� ������ ������� �����
                    Util.kickException(new UnsupportedNumber("��������� ������� �����: " + Util.unitsRomeToString(Constants.unitsRome)));
            }
            // �������� �� ��������
            int number = Util.transferRomeInArabNumber(s);
            if (number < 0 || number > 10)
                Util.kickException(new UnsupportedNumber(String.format("������� ����� �� �������� � ���������� ��������: %s.", s)));
        }

    }


}
