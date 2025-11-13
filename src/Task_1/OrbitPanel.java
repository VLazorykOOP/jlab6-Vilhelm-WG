package Task_1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Панель, що моделює та малює еліптичну орбіту.
 */
public class OrbitPanel extends JPanel implements ActionListener {

    // --- Параметри моделі ---

    // Розміри орбіти
    private final int SEMI_MAJOR_AXIS = 250; // Велика піввісь (горизонтальна)

    // Мала піввісь МАЄ БУТИ МЕНШОЮ за радіус планети, щоб супутник ховався
    private final int SEMI_MINOR_AXIS = 35; // Мала піввісь (вертикальна)

    // Розміри об'єктів
    private final int PLANET_RADIUS = 50;
    private final int SATELLITE_RADIUS = 10;

    // Кут для анімації
    private double angle = 0;
    // Швидкість обертання (радіан за кадр)
    private final double angularVelocity = 0.03;

    // Таймер для анімації
    private Timer timer;

    public OrbitPanel() {
        // Встановлюємо бажаний розмір панелі
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        // Ініціалізуємо та запускаємо таймер
        timer = new Timer(16, this);
        timer.start();
    }

    /**
     * Цей метод викликається щоразу, коли таймер спрацьовує.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // 1. Оновлюємо стан моделі (змінюємо кут)
        angle += angularVelocity;

        // Скидаємо кут, щоб уникнути переповнення
        if (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }

        // 2. Запитуємо перемалювання панелі з новим станом
        repaint();
    }

    /**
     * Цей метод викликається щоразу, коли панель потрібно перемалювати.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Очищуємо панель (заливаємо фоном)

        // Використовуємо Graphics2D для згладжування (Antialiasing)
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- Визначаємо координати центру ---
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // --- 1. Малюємо орбіту (сірий еліпс) ---
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawOval(
                centerX - SEMI_MAJOR_AXIS,
                centerY - SEMI_MINOR_AXIS,
                2 * SEMI_MAJOR_AXIS,
                2 * SEMI_MINOR_AXIS
        );

        // --- 2. Розраховуємо позицію супутника ---
        int satelliteX = (int) (centerX + SEMI_MAJOR_AXIS * Math.cos(angle));
        int satelliteY = (int) (centerY + SEMI_MINOR_AXIS * Math.sin(angle));

        // --- 3. Логіка затінення (Порядок малювання) ---

        // Визначаємо, чи супутник "попереду" (Y < centerY)
        // (Пам'ятаємо, що в Swing Y=0 вгорі)
        boolean isInFront = satelliteY > centerY;

        // 3.1. Якщо супутник позаду (його Y > centerY), малюємо його ПЕРШИМ
        if (!isInFront) {
            drawSatellite(g2d, satelliteX, satelliteY);
        }

        // 3.2. Малюємо планету (ЗАВЖДИ)
        // Планета буде намальована поверх супутника, якщо він позаду
        drawPlanet(g2d, centerX, centerY);

        // 3.3. Якщо супутник попереду (його Y < centerY), малюємо його ОСТАННІМ
        // Супутник буде намальований поверх планети
        if (isInFront) {
            drawSatellite(g2d, satelliteX, satelliteY);
        }
    }

    /**
     * Допоміжний метод для малювання планети
     */
    private void drawPlanet(Graphics2D g, int x, int y) {
        g.setColor(new Color(0, 100, 200)); // Темно-синій
        g.fillOval(
                x - PLANET_RADIUS,
                y - PLANET_RADIUS,
                2 * PLANET_RADIUS,
                2 * PLANET_RADIUS
        );
        // Додамо атмосферу для краси
        g.setColor(new Color(173, 216, 230, 100)); // Напівпрозорий блакитний
        g.fillOval(
                x - (PLANET_RADIUS + 5),
                y - (PLANET_RADIUS + 5),
                2 * (PLANET_RADIUS + 5),
                2 * (PLANET_RADIUS + 5)
        );
    }

    /**
     * Допоміжний метод для малювання супутника
     */
    private void drawSatellite(Graphics2D g, int x, int y) {
        g.setColor(Color.RED);
        g.fillOval(
                x - SATELLITE_RADIUS,
                y - SATELLITE_RADIUS,
                2 * SATELLITE_RADIUS,
                2 * SATELLITE_RADIUS
        );
        // Маленький білий відблиск
        g.setColor(Color.WHITE);
        g.fillOval(
                x - SATELLITE_RADIUS / 2,
                y - SATELLITE_RADIUS / 2,
                SATELLITE_RADIUS,
                SATELLITE_RADIUS
        );
    }
}