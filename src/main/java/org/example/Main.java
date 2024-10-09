package org.example;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.CSVReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;



public class Main {
    public static void main(String[] args) {
        String fileName = "data.csv";
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        List<Employee> list = parseCSV(columnMapping, fileName);


        if (list != null) {
            String json = listToJson(list);
            System.out.println(json);
            writeString(json, "output.json");
        } else {
            System.out.println("Ошибка при чтении CSV файла.");
        }
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String listToJson(List<Employee> employees) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(employees, listType);
    }

    public static void writeString(String json, String filePath){
            try (FileWriter fileWriter = new FileWriter(filePath)){
                fileWriter.write(json);
                System.out.println("JSON записан в файл: " + filePath);
            }catch (IOException e) {
            e.printStackTrace();
            }
        }
    }