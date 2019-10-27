package Utils;

import newException.ErrorSearchRomeNumber;

import java.util.Map;

public class Util {
    // Выкидывет исключение переданное в параметре
    public static void kickException(Exception e) {
        // Выводим сообщение
        System.out.println(e.getMessage());
        // Прекращаем работу программы
        System.exit(1);
    }

    public static int transferRomeInArabNumber(String romeNumber) {

        // хранит арабскую цифру, результат перевода одного операнда
        int result;
        // Хранит байты римского числа
        byte[] b = romeNumber.getBytes();
        result = searchRome(b[0]);
        int lastNumber = result;
        // Проходим по каждому римскому числу
        for (int i = 1; i < b.length; i++) {
            // Данный символ из римского числа
            int n = searchRome(b[i]);
            // Если последий символ меньше чем настоящий
            if (lastNumber < n) {
                result = n - result;
            } else {
                result += n;
            }
            lastNumber = n;
        }
        return result;
    }

    // Поиск римского числа в словаре
    private static int searchRome (byte b) {
        for (Map.Entry entry: Constants.romeNumberTable.entrySet()) {
            if ((int)entry.getKey() == (int)b)
                return (int)entry.getValue();
        }
        Util.kickException(new ErrorSearchRomeNumber("Данное римское число неизвестно."));
        return 0;// костыль
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
