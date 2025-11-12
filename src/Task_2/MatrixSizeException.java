package Task_2;

/**
 * Власний виняток, що генерується, коли розмірність матриці (n)
 * виходить за допустимі межі (1 <= n <= 15).
 * * Успадковано від ArithmeticException згідно з вимогою завдання.
 */
public class MatrixSizeException extends ArithmeticException {

    public MatrixSizeException(String message) {
        super(message);
    }
}
