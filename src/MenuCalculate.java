import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class MenuCalculate {

    public static void main(String[] args) {
        System.out.println("Введите арифмитическое выражение:");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        Calculate calculate;
        try {
            calculate = new Calculate(bufferedReader.readLine());
            System.out.println("Результат: " + calculate.getResult());
        } catch (IOException e) {
            System.out.println("IOException");
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                // NaN
                System.out.println(e.getStackTrace());
            }
        }
    }
}
