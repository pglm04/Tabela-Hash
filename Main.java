import java.util.Random;
import java.util.Scanner;

class Record {
    private int code;

    public Record(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

class Table {
    private int[] array;
    private int size;
    private int collisions = 0;
    private int comparisons = 0;

    public Table(int size) {
        this.size = size;
        array = new int[size];

        for (int i = 0; i < size; i++) {
            array[i] = -1;
        }
    }

    public int moduloHash(int code) {
        return code % size;
    }

    public int multiplicationHash(int code) {
        double A = (Math.sqrt(5) - 1) / 2;
        double value = A * code;
        double fraction = value - (int) value;
        return (int) (size * fraction);
    }

    public int foldingHash(int code) {
        String codeString = String.valueOf(code);
        int sum = 0;
        for (int i = 0; i < codeString.length(); i++) {
            sum += codeString.charAt(i);
        }
        return sum % size;
    }

    public void addRecord(Record newRecord, int hashFunctionChoice) {
        int code = newRecord.getCode();
        int arrayValue = -1;
        if (hashFunctionChoice == 1) {
            arrayValue = moduloHash(code);
        } else if (hashFunctionChoice == 2) {
            arrayValue = multiplicationHash(code);
        } else if (hashFunctionChoice == 3) {
            arrayValue = foldingHash(code);
        } else {
        }

        if (arrayValue != -1) {
            if (array[arrayValue] == -1) {
                array[arrayValue] = code;
            } else {
                while (array[arrayValue] != code && array[arrayValue] != -1) {
                    arrayValue = (arrayValue + 1) % size;
                    collisions = collisions + 1;
                }
                array[arrayValue] = code;
            }
        }
    }

    public void searchRecord(Record newRecord) {
        int code = newRecord.getCode();
        int value = moduloHash(code);

        while (value < size) {
            comparisons++;
            int key = array[value];

            if (code == key) {
                System.out.println("O registro " + code + " está na posição " + value);
                return;
            }
            value = (value + 1) % size;
        }
        System.out.println("O registro não está na tabela");
    }

    public void printArray() {
        for (int i = 0; i < size; i++) {
            System.out.println(i + "\t" + array[i]);
        }
    }

    public int getCollisions() {
        return collisions;
    }

    public int getComparisons() {
        return comparisons;
    }
}

public class Main {

    public static void main(String[] args) {
        long seed = 54321;

        int[] tableSizes = {10, 100, 500, 10000, 50000};
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha o tipo de função de hash:");
        System.out.println("1 - Resto");
        System.out.println("2 - Multiplicação");
        System.out.println("3 - Dobramento");
        System.out.print("Digite o número correspondente: ");

        int hashFunctionChoice = scanner.nextInt();
        scanner.close();
        for (int size : tableSizes) {
            Table table = new Table(size);
            Record[] dataSet = generateDataSet(size, seed);

            long startTimeInsertion = System.nanoTime();
            for (Record record : dataSet) {
                table.addRecord(record, hashFunctionChoice);
            }
            long endTimeInsertion = System.nanoTime();
            double insertionTime = (endTimeInsertion - startTimeInsertion); 

            int searchIndex = size / 2;
            Record searchRecord = dataSet[searchIndex];

            long startTimeSearch = System.nanoTime();
            table.searchRecord(searchRecord);
            long endTimeSearch = System.nanoTime();
            double searchTime = (endTimeSearch - startTimeSearch);

            int collisions = table.getCollisions();
            int comparisons = table.getComparisons();

            System.out.println("Tabela de tamanho " + size + " preenchida com sucesso.");
            System.out.println("Tempo de inserção: " + insertionTime + " nanosegundos");
            System.out.println("Tempo de busca: " + searchTime + " nanosegundos");
            System.out.println("Numero de colisões: " + collisions);
            System.out.println("Numero de comparações: " + comparisons);
            System.out.println();
        }
    }

    public static Record[] generateDataSet(int size, long seed) {
        Random random = new Random(seed);
        Record[] dataSet = new Record[size];
        for (int i = 0; i < size; i++) {
            int code = random.nextInt(900000000) + 100000000;
            dataSet[i] = new Record(code);
        }
        return dataSet;
    }
}
