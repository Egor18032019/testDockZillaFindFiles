import utils.FindAll;

import java.util.*;

public class Answer {
    private static final String PACKAGE = "storage";
    private static final String REC = "require ‘";

    public static void main(String[] args) {
        HashMap<String, String> filesStorage = FindAll.find(PACKAGE);

        List<String> answer = new ArrayList<>();

        int countIsFileFirst = 0;
        boolean cycleError = true;
        Set<Integer> countLInks = new HashSet<>();
        for (Map.Entry<String, String> entry : filesStorage.entrySet()) {
            String value = entry.getValue();
            // в задание не прописано что бы удалять путь к следующему файлу
            // Поэтому не стал убирать
            boolean isFileFirst = !value.contains(REC);
            if (isFileFirst) {
                // добавляем в начало
                answer.add(0, value);
                countIsFileFirst++;
                cycleError = false;
                countLInks.add(0);
            } else {
                int index = count(value, REC);
                if (!countLInks.add(index)) {
                    System.out.println("Неправильные цикличные ссылки в файле " + entry.getKey());
                }
                if (index > answer.size()) {
                    answer.add(value);
                } else {
                    answer.add(index, value);
                }
            }
        }
        if (cycleError) {
            //Todo В случае циклической зависимости вывести объяснение ошибки
            // - указать цикл зависимостей между файлами.
            System.out.println("Нет начального файла");
            // а если ошибка цикличной зависимости  + нет начального файла -> то что выводить?
        }
        System.out.println(answer.toString());
    }

    public static int count(String str, String target) {
        return (str.length() - str.replace(target, "").length()) / target.length();
    }
    /**
     * Скалдываем в HashMap allFilesStorage
     * Ключ = путь к файлу
     * Значение = строки внутри файла
     *  ? или заранее проверять файл на require ??
     * Далее циклом бежим по HashMap и ищем файл firstFile в котором нет require ‘
     * Если не нашли то кидаем ошибку
     * Строим правильную  storage
     * Находим файл где больше всего ссылок
     * и ставим его в конец списка
     * ищем у него пути вызова
     * дергаем text по этим ключам из allFilesStorage
     * проверям есть ли  require ‘ (isFileFirst) если нет то ставим в начало
     * если есть то ставим в на место по количеству require ‘
     *

     */
}
