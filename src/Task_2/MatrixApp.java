package Task_2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Головний клас програми з графічним інтерфейсом Swing.
 * Використовує JFrame, JPanel, JButton, JLabel, JTextField, JTable.
 */
public class MatrixApp extends JFrame {

    // --- Компоненти GUI ---
    private JTextField filenameField;
    private JButton processButton;
    private JTable matrixTable; // Для матриці X
    private JTable resultTable; // Для вектора L
    private JLabel statusLabel;

    public MatrixApp() {
        // --- 1. Налаштування головного вікна (JFrame) ---
        setTitle("Лабораторна робота №6 (Swing)");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Центрувати вікно

        // --- 2. Створення панелей та компонентів ---

        // Панель вводу (Північ)
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.add(new JLabel("Ім'я файлу:"));
        filenameField = new JTextField("matrix.txt", 25); // Поле для імені файлу
        inputPanel.add(filenameField);
        processButton = new JButton("Завантажити та Обчислити"); // Кнопка
        inputPanel.add(processButton);

        // Панель результатів (Центр)
        matrixTable = new JTable();
        resultTable = new JTable();

        // Додаємо таблиці в JScrollPane для можливості прокрутки
        JScrollPane matrixScrollPane = new JScrollPane(matrixTable);
        JScrollPane resultScrollPane = new JScrollPane(resultTable);

        // Використовуємо JSplitPane, щоб розділити простір між таблицями
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                matrixScrollPane, resultScrollPane);
        splitPane.setResizeWeight(0.7); // Матриця займає 70% місця

        // Статус-бар (Південь)
        statusLabel = new JLabel("Готово до роботи."); // Мітка статусу
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Color.LIGHT_GRAY);

        // --- 3. Збірка інтерфейсу ---
        // Використовуємо BorderLayout для головного вікна
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // --- 4. Обробник подій для кнопки ---
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processMatrixFile();
            }
        });
    }

    /**
     * Основний метод, що викликається при натисканні кнопки.
     * Читає файл та обробляє 3 типи винятків.
     */
    private void processMatrixFile() {
        String filename = filenameField.getText();
        if (filename.isEmpty()) {
            showError("Ім'я файлу не може бути порожнім.");
            return;
        }

        try (Scanner scanner = new Scanner(new File(filename))) {

            // --- Читання та перевірка розмірності ---
            int n = scanner.nextInt();

            // *** ГЕНЕРАЦІЯ ВЛАСНОГО ВИКЛЮЧЕННЯ (MatrixSizeException) ***
            if (n <= 0 || n > MatrixLogic.MAX_SIZE) {
                throw new MatrixSizeException(
                        "Розмір матриці 'n' (" + n + ") некоректний. Має бути 1 <= n <= " + MatrixLogic.MAX_SIZE
                );
            }

            // --- Читання матриці ---
            int[][] matrix = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = scanner.nextInt(); // Може кинути InputMismatchException
                }
            }

            // --- Обчислення та оновлення GUI ---
            boolean[] vectorL = MatrixLogic.calculateVectorL(matrix);
            updateMatrixTable(matrix);
            updateResultTable(vectorL);

            showStatus("Файл '" + filename + "' успішно оброблено.", Color.GREEN.darker());

        }
        // *** ОБРОБКА СТАНДАРТНОГО ВИКЛЮЧЕННЯ 1 (FileNotFoundException) ***
        catch (FileNotFoundException ex) {
            showError("Помилка: Файл не знайдено '" + filename + "'");
        }
        // *** ОБРОБКА СТАНДАРТНОГО ВИКЛЮЧЕННЯ 2 (InputMismatchException) ***
        catch (InputMismatchException | NumberFormatException ex) {
            showError("Помилка: Файл містить нечислові або некоректні дані.");
        }
        // *** ОБРОБКА ВЛАСНОГО ВИКЛЮЧЕННЯ 3 (MatrixSizeException) ***
        catch (MatrixSizeException ex) {
            showError(ex.getMessage());
        }
        // (Додатково) Обробка, якщо файл закінчився раніше, ніж очікувалось
        catch (NoSuchElementException ex) {
            showError("Помилка: Не вистачає даних у файлі.");
        }
        // (Додатково) Інші помилки
        catch (Exception ex) {
            showError("Виникла неочікувана помилка: " + ex.getMessage());
        }
    }

    // --- Допоміжні методи для оновлення GUI ---

    private void updateMatrixTable(int[][] matrix) {
        int n = matrix.length;
        String[] columnNames = new String[n];
        for (int i = 0; i < n; i++) {
            columnNames[i] = "Col " + (i + 1);
        }

        // JTable не працює напряму з int[][], потрібен Object[][]
        Object[][] data = new Object[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = matrix[i][j];
            }
        }

        matrixTable.setModel(new DefaultTableModel(data, columnNames));
    }

    private void updateResultTable(boolean[] vectorL) {
        int n = vectorL.length;
        String[] columnNames = {"Рядок", "Результат (L)"};

        Object[][] data = new Object[n][2];
        for (int i = 0; i < n; i++) {
            data[i][0] = "L[" + i + "]";
            data[i][1] = vectorL[i];
        }

        resultTable.setModel(new DefaultTableModel(data, columnNames));
    }

    // Методи для відображення статусу та помилок
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private void showError(String message) {
        showStatus(message, Color.RED);
        // Додатково показуємо діалогове вікно, оскільки статус-бар легко пропустити
        JOptionPane.showMessageDialog(this, message, "Помилка обробки", JOptionPane.ERROR_MESSAGE);
    }

    // --- Точка входу в програму ---
    public static void main(String[] args) {
        // Запуск GUI в потоці обробки подій (Event Dispatch Thread)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MatrixApp().setVisible(true);
            }
        });
    }
}
