

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestKNN {

    /**
     * 从数据文件中读取数据
     *
     * @param datas 存储数据的集合对象
     * @param path  数据文件的路径
     */
    public void read(List<List<Double>> datas, String path) {
        try {

            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            String data = br.readLine();
            List<Double> l = null;
            while (data != null) {
                String t[] = data.split(" ");
                l = new ArrayList<Double>();

                for (int i = 0; i < t.length; i++) {

                    l.add(Double.parseDouble(t[i]));
                }
                datas.add(l);
                data = br.readLine();

            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 程序执行入口
     *
     * @param args
     */
    public static void main(String[] args) {
        TestKNN t = new TestKNN();
        String datafile = new File("").getAbsolutePath() + File.separator + "knntrain***.txt";
        String testfile = new File("").getAbsolutePath() + File.separator + "knndata***.txt";
        String INPUT_PATH = ("/Users/apple/Downloads/股票编号.txt");
        File file = new File(INPUT_PATH);
        File file1 = new File("knn_out***.txt");
        try {
            List<List<Double>> datas = new ArrayList<List<Double>>();
            List<List<Double>> testDatas = new ArrayList<List<Double>>();
            t.read(datas, datafile);
            t.read(testDatas, testfile);
            KNN knn = new KNN();
            //写文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file1,true));
            for (int i = 0; i < testDatas.size(); i++) {
                List<Double> test = testDatas.get(i);
                bw.write(br.readLine()+"\t\t");
                Integer a = Math.round(Float.parseFloat((knn.knn(datas, test, 3))));
                if(a==1) bw.write("【negative----】\n");
                if(a==3) bw.write("【  neutral   】\n");
                if(a==2) bw.write("【++++positive】\n");
            }
            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}