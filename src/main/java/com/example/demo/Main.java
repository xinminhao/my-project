package com.example.demo;

public class Main {
	public static class Data {
        String name;
        int errorMsg;

        public Data() {
            this.name = "";
            this.errorMsg = 0;
        }
        public String getName() {	
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		public int getErrorMsg() {
			return errorMsg;
		}
		public void setErrorMsg(int errorMsg) {
			this.errorMsg = errorMsg;
		}
    }

    // DataAll 包含一个 Data 数组
    public static class DataAll {
        private Data[] data;

        public DataAll(Data[] data) {
            this.data = data;
        }

        public Data[] getData() {
            return data;
        }

        public void setData(Data[] data) {
            this.data = data;
        }
    }

    public static void main(String[] args) {
        // 模拟原始数组
    	Object[][] raw = {
    		    {"张三", 0},
    		    {"李四", 1},
    		    {"王五", 1},
    		    {"赵六", 0}
    	};

        // 创建 Data 数组
        Data[] dataArray = new Data[raw.length];
        for (int i = 0; i < raw.length; i++) {
            Data d = new Data();
            d.setName((String) raw[i][0]);
            d.setErrorMsg((Integer) raw[i][1]);
            dataArray[i] = d;
        }

        // 封装成 DataAll
        DataAll dataAll = new DataAll(dataArray);

        // 输出验证
        for (Data d : dataAll.getData()) {
            System.out.println("姓名: " + d.getName() + "，错误码: " + d.getErrorMsg());
        }
    }
}
