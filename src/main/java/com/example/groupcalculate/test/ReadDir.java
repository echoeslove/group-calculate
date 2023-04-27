package com.example.groupcalculate.test;

import org.apache.lucene.util.RamUsageEstimator;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReadDir {

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, List<StockCandleData>> map = constructBaseData();
        System.out.println("");
    }

    public static Map<String, List<StockCandleData>> constructBaseData() throws FileNotFoundException {
        // 指定文件夹路径
        String folderPath = "csv/export";

        // 创建一个 File 对象，表示要读取的文件夹
        File folder = new File(folderPath);

        // 获取文件夹下所有的文件和子文件夹
        File[] files = folder.listFiles();


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        Map<String, List<StockCandleData>> map = new LinkedHashMap<>();
        for (File f : files) {
            Scanner scanner = new Scanner(f, "GBK");
            if (!scanner.hasNextLine()) {
                continue;
            }
            String title = scanner.nextLine();
            String code = title.split(" ")[0];
            String column = scanner.nextLine();

            while (scanner.hasNextLine()) {
                String[] datas = scanner.nextLine().split(",");
                if (!datas[0].startsWith("20")) {
                    continue;
                }
                StockCandleData stockCandleData = new StockCandleData();
                stockCandleData.date = LocalDate.parse(datas[0], dateTimeFormatter);
                stockCandleData.high = Math.max(Double.parseDouble(datas[2]), Double.parseDouble(datas[3]));
                stockCandleData.low = Math.min(Double.parseDouble(datas[2]), Double.parseDouble(datas[3]));
                stockCandleData.price = new double[]{
                        stockCandleData.high,
                        stockCandleData.low
                };
                stockCandleData.absHighLow = stockCandleData.high - stockCandleData.low;

                if (map.get(code) == null) {
                    List<StockCandleData> list = new ArrayList<>();
                    list.add(stockCandleData);
                    map.put(code, list);
                } else {
                    map.get(code).add(stockCandleData);
                }
            }
            scanner.close();
        }

        // 1year 5000 stocks = 200MB
//        System.out.println("size: " + RamUsageEstimator.humanSizeOf(map));

        map.entrySet().forEach(t -> Collections.sort(t.getValue()));
        return map;
    }
}
