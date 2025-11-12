import javax.swing.*;

/**
 * Головний клас для запуску симуляції.
 * Створює вікно та додає до нього панель анімації.
 */
public class OrbitalSimulation {

    public static void main(String[] args) {
        // Усі операції з GUI в Swing мають виконуватись
        // у потоці обробки подій (Event Dispatch Thread, EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        // 1. Створюємо головне вікно
        JFrame frame = new JFrame("Лабораторна робота №6: Еліптична орбіта");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 2. Створюємо та додаємо нашу панель для малювання
        frame.add(new OrbitPanel());

        // 3. "Пакуємо" вікно, щоб воно прийняло
        // бажаний розмір (setPreferredSize) нашої панелі
        frame.pack();

        // 4. Центруємо вікно на екрані
        frame.setLocationRelativeTo(null);

        // 5. Робимо вікно видимим
        frame.setVisible(true);
    }
}