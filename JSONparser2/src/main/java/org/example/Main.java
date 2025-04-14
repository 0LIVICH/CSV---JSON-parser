import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        // 1. Строки для колонок CSV
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        // 2. Имя CSV файла
        String fileName = "data.csv";

        // 3. Получаем список сотрудников
        List<Employee> list = parseCSV(columnMapping, fileName);

        // 4. Преобразуем список в JSON
        String json = listToJson(list);

        // 5. Записываем JSON в файл
        writeString(json);
    }

    // Метод для парсинга CSV
    public static List<Employee> parseCSV(String[] columnMapping, String fileName) throws Exception {
        // Создаем стратегию для маппинга CSV колонок
        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping(columnMapping);

        // Читаем CSV файл и парсим данные в список объектов Employee
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            return new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build()
                    .parse();
        }
    }

    // Метод для преобразования списка в JSON
    public static String listToJson(List<Employee> list) {
        // Определяем тип списка
        Type listType = new TypeToken<List<Employee>>() {}.getType();

        // Создаем экземпляр Gson для конвертации
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Преобразуем список в JSON
        return gson.toJson(list, listType);
    }

    // Метод для записи строки в файл
    public static void writeString(String json) {
        try (FileWriter fileWriter = new FileWriter("data.json")) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
