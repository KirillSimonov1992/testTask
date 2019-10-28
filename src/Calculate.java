import Utils.Constants;
import Utils.Util;

import java.util.ArrayList;

public class Calculate {
    private АrithmeticExpression arithmeticExpression;

    public Calculate(String arithmeticExpression) {
        this.arithmeticExpression = new АrithmeticExpression(arithmeticExpression);
    }

    public String getResult() {
        String result;
        if (arithmeticExpression.isInt())
            result = String.valueOf(operationNumberInt(arithmeticExpression.getNumbers()));
        else
            result = operationRomeNumber(arithmeticExpression.getNumbersRome());
        return result;
    }

    private float operationNumberInt(ArrayList<Integer> numbers) {
        float result = numbers.get(0);
        switch (arithmeticExpression.getOperation()) {
            case "+":
                for (int i = 1; i < numbers.size(); i++)
                    result += numbers.get(i);
                return result;
            case "-":
                for (int i = 1; i < numbers.size(); i++)
                    result -= numbers.get(i);
                return result;
            case "*":
                for (int i = 1; i < numbers.size(); i++)
                    result *= numbers.get(i);
                return result;
            default: // значит деление
                for (int i = 1; i < numbers.size(); i++)
                    result /= numbers.get(i);
                return result; // time
        }
    }

    private String operationRomeNumber(ArrayList<String> romeNumbers) {
        // Хранит арабские числа
        ArrayList<Integer> numbers = new ArrayList<>(romeNumbers.size());
        for (String s: romeNumbers) {
            numbers.add(Util.transferRomeInArabNumber(s));
        }
        // Произведем операцию с арабскими числами
        int result = (int)operationNumberInt(numbers);
        // Переведем арабские в римские
        return transferIntInRome(result);
    }

    private String transferIntInRome(int result) {
        if (result == 0)
            return "0";
        StringBuilder numberRome = new StringBuilder();
        // Количество:
        // тысячи
        // int thousands = 0;
        // пятисотки
        //int fiveРundred = result / 5;
        // сотен С
        int hundreds = result / 100;
        if (hundreds > 0)
            numberRome.append(C(hundreds));
        // пятьдесят L
        int fifty = (result % 100) / 50;
        if (fifty > 0)
            numberRome.append(L(fifty));
        // десяток X
        int tens = ((result % 100) % 50) / 10;
        if (tens > 0)
            numberRome.append(X(tens));
        // единиц V I
        int unit = result % 10;
        if (unit > 0)
            numberRome.append(Constants.unitsRome[unit]);
        return numberRome.toString();
    }

    // Сотни
    private String C(int count) {
        StringBuilder sb = new StringBuilder();
        if (count == 4)
            sb.append("CD"); //  400 (С - 100, D - 500)
        else if (count != 0 && count < 4) {
            int i = 0;
            while (i < count) {
                sb.append("C");
                i++;
            }
        }
        return sb.toString();
    }

    // Пятьдесят
    private String L(int count) {
        StringBuilder sb = new StringBuilder();
        if (count == 2)
            sb.append("XL"); //  90 (X - 10, C - 100)
        else
            sb.append("L");
        return sb.toString();
    }

    // Десятки
    private String X(int count) {
        StringBuilder sb = new StringBuilder();
        if (count == 4)
            return sb.append("XL").toString(); // 40 (X - 10, C - 50)
        else if (count != 0 && count < 4) {
            int i = 0;
            while (i < count) {
                sb.append("X");
                i++;
            }
        }
        return sb.toString();
    }
}

