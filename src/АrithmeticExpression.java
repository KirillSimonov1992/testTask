import Utils.Constants;
import Utils.Util;
import newException.ErrorOperand;
import newException.NoValidOperationFound;
import newException.UnsupportedNumber;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class АrithmeticExpression  {
    private String expression; //  введенное выражение в калькулятор
    private String operation; //  инициализируется при обработке введенных операций treatmentOperation()
    private ArrayList<Integer> numbersInt; // инициализируется при обработке выражения
    private ArrayList<String> numbersRome;
    private boolean isInt;

    public АrithmeticExpression(String expression) {
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

    // Обработка всего выражения
    private void treatmentExpression() {
        // Обработка веденных операций
        // Проверка на их количество, отсутствие операции
        // и на допустимость данной операции в калькуляторе
        // Инициализируется введенная одна операция
        operation = treatmentOperation();
        // Обработка введенных чисел
        treatmentNumber();
    }

    // Обработка вводимых операций
    // Возвращает введенную операцию
    // в случаи усппеха обработки
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
            Util.kickException(new NoValidOperationFound("В выражении не найдена допустимая операция. " +
                    "Допустимые операции: сложение, вычитание, умножение, деление."));
        else if (countOperation > 1)
            Util.kickException(new NoValidOperationFound("Операция для выполнения доступна только одна."));
        return searchOperaion.trim();
    }

    // Обработка чисел в выражении
    private void treatmentNumber() {
        // Разделяем строку по операции чтобы остались в массиве только числа
        String[] numbersString = expression.trim().split(Constants.regExpOperations);
        // containNumberInt и containNumberRome - для проверки во всем выражении
        // sybmolIsInt и sybmolIsRome - для проверки в одной цифре
        boolean sybmolIsInt, sybmolIsRome, containNumberInt = false, containNumberRome = false;
        // Строка для временного хранения одного символа
        StringBuilder stringBuilder = new StringBuilder();
        // Проходим по строке и записываем числа в нужное местое
        for (int i = 0; i < numbersString.length; i++) {
            sybmolIsInt = false;
            sybmolIsRome = false;
            byte[] symbol = numbersString[i].getBytes();
            // Если число состоит не из одного числа
            for (int j = 0; j < symbol.length; j++) {
                byte b = symbol[j];
                if (b == 32 || b == 9) //  пробел или табуляция
                    continue;
                //if (b == ) перевод строки и возврат каретки
                if (b >= 48 && b <= 57) // от [0, 10]
                    containNumberInt = sybmolIsInt = true;
                else if (Constants.allRomeNumber.contains(new Integer(b))) // Проверяем есть ли такое римское число
                    containNumberRome = sybmolIsRome = true;
                else if (!(sybmolIsInt && sybmolIsRome)) // Если не арабские и не римские цифры из диапозонов
                    Util.kickException(new UnsupportedNumber("В выражении используются не допустимые символы."));
                stringBuilder.append((char)b);
            }
            if (sybmolIsInt && sybmolIsRome) //  при варианте ввода одного операнда как смешивание арабской и римской цифры "1X"
                Util.kickException(new UnsupportedNumber("В выражении обнаружено использование арабских и римских цифр вместе в одном операнде."));
            if (sybmolIsInt)
                numbersInt.add(Integer.valueOf(stringBuilder.toString()));
            if (sybmolIsRome)
                numbersRome.add(stringBuilder.toString());
            stringBuilder.delete(0, stringBuilder.length());
        }
        if (containNumberInt && containNumberRome) //  При наличие в выражении одного операнда из арабских и другого из римских цифр
            Util.kickException(new UnsupportedNumber("В выражении обнаружено использование арабских и римских цифр вместе в одном выражении."));
        int sizeNumberInt = numbersInt.size();
        int sizeNumberRome = numbersRome.size();
        // Если операндов больше чем задано константой
        if (sizeNumberInt + sizeNumberRome > Constants.countOperand)
            Util.kickException(new ErrorOperand(String.format("Операндов можно вводить не более ", Constants.countOperand)));
        else if (sizeNumberInt + sizeNumberRome < Constants.countOperand)
            Util.kickException(new ErrorOperand(String.format("Операндов должно быть %d", Constants.countOperand)));
        // Если операнды арабские цифры
        if (containNumberInt)
            checkRangeInt();
        // Если операнды римские цифры
        if (containNumberRome)
            treatmentRomeNumber();
    }

    // Проверяем цифры на вхождение в диапозон[1, 10]
    private void checkRangeInt() {
        // Запишем ошибочные цифры не входящие в диапозон [1, 10]
        ArrayList<Integer> errorNumbers = new ArrayList<>();
        // Проверяем цифры на вхождение в диапозон[1, 10]
        for (int i = 0; i < numbersInt.size(); i++)
            if (numbersInt.get(i) < 1 || numbersInt.get(i) > 10)
                errorNumbers.add(numbersInt.get(i));
        // Если есть цифры не входящие в диапозон то выбросим исключение с описанием ошибки
        if (!errorNumbers.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Integer i: errorNumbers)
                sb.append(i + ", ");
            sb.delete(sb.length() - 2, sb.length());
            Util.kickException(new UnsupportedNumber(String.format("Введены числа не входящие в допустимый диапозон: %s.", sb.toString())));
        }
    }

    // Обработка римских цифр
    private void treatmentRomeNumber() {
        byte[] rome;
        boolean isExist;// флаг ошибки, true - нет ошибки
        for (String s: numbersRome) {
            isExist = false; // изначально предполагаем что символ отсутствует в словаре
            rome = s.getBytes();
            for (byte b: rome) { // III + L
                for (Map.Entry entry: Constants.romeNumberTable.entrySet()) {
                    if ((int)entry.getKey() == (int)b) // Если в таблице отсутствует данный символ установить флаг ошибки
                        isExist = true;
                }
                if (isExist == false) // Проверяем входит ли в диапозон данное римское число
                    Util.kickException(new UnsupportedNumber("Доступные римские цифры: " + Util.unitsRomeToString(Constants.unitsRome)));
            }
            // Проверим на диапозон
            int number = Util.transferRomeInArabNumber(s);
            if (number < 0 || number > 10)
                Util.kickException(new UnsupportedNumber(String.format("Введены числа не входящие в допустимый диапозон: %s.", s)));
        }

    }


}
