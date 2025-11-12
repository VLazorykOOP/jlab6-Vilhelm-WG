package Task_2;

/**
 * Цей клас інкапсулює логіку обчислень з Лабораторної роботи №1 (частина 3).
 */
public class MatrixLogic {

    public static final int MAX_SIZE = 15;

    /**
     * Обчислює логічний вектор L на основі вхідної матриці X.
     * L[i] = true, якщо в i-му рядку матриці X кількість
     * від'ємних елементів більша за кількість додатних.
     *
     * @param X Вхідна матриця n x n.
     * @return Логічний вектор L розміром n.
     */
    public static boolean[] calculateVectorL(int[][] X) {
        int n = X.length;
        boolean[] L = new boolean[n];

        for (int i = 0; i < n; i++) {
            int neg = 0;
            int pos = 0;

            // Рахуємо елементи в i-му рядку
            for (int j = 0; j < n; j++) {
                if (X[i][j] < 0) {
                    neg++;
                } else if (X[i][j] > 0) {
                    pos++;
                }
            }

            // Записуємо результат у логічний вектор
            L[i] = (neg > pos);
        }
        return L;
    }
}