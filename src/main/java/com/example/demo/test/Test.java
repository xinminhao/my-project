package com.example.demo.test;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static class DataAll {
        private Object[][] data;

        public DataAll() {
            data = data;
        }

        public Object[][] getData() {
            return data;
        }

        public void setData(Object[][] data) {
            this.data = data;
        }
    }

    public static void main(String[] args) {
        // 原始数据
        Object[][] raw = {
            {"张三", 0},
            {"李四", 1},
            {"王五", 1},
            {"赵六", 0}
        };

        // 新的结构：[行号, errorMsg是否为1]
        List<Object[]> processed = new ArrayList<>();
        for (int i = 0; i < raw.length; i++) {
            Object[] row = raw[i];
            int errorMsg = (Integer) row[1];

            // 构造新数组：第一列是索引，第二列根据是否为1填入 "1" 或 ""
            Object[] newRow = new Object[2];
            newRow[0] = String.valueOf(i); // 第几条（从0开始）
            newRow[1] = (errorMsg == 1) ? "1" : ""; // 是否是1
            processed.add(newRow);
        }

        // 转为二维数组
        Object[][] resultArray = processed.toArray(new Object[0][0]);

        // 封装进 DataAll
        DataAll dataAll = new DataAll();
        dataAll.setData(resultArray);

        // 输出验证
        for (Object[] row : dataAll.getData()) {
            System.out.println("行号: " + row[0] + ", 标记: " + row[1]);
        }
    }
}
