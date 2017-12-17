import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class word2vec {

    private static final int lenth = 1000;
    private File stock;
    private File[] files;
    private List<String> pathName = new ArrayList<String>();

    public void iteratorPath(String dir) {
        stock = new File(dir);
        files = stock.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    pathName.add(file.getName());
                } else if (file.isDirectory()) {
                    iteratorPath(file.getAbsolutePath());
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //用list记录模型的词
        String READ_PATH = ("features.txt");
        ArrayList<String> dic = new ArrayList<String>();
        File file = new File(READ_PATH);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp;
        while ((temp = br.readLine()) != null) {
            dic.add(temp);
        }
        br.close();
        String[] vec = new String[lenth];

        Integer id = 0;
        word2vec news = new word2vec();
        news.iteratorPath("tfidf_train/");
        for (String title : news.pathName) {
            String INPUT_PATH = ("tfidf_train/" + title);
            String OUT_PATH = "knntrain***.txt";
            //首先读取文件
            File file1 = new File(INPUT_PATH);
            for (int j = 0; j < vec.length; j++) {
                vec[j] = "0";
            }
            BufferedReader br1 = new BufferedReader(new FileReader(file1));
            BufferedWriter bw1 = new BufferedWriter(new FileWriter(OUT_PATH, true));
            String temp0;
            String[] temp1 = new String[2];
            int k = 0;
            while ((temp0 = br1.readLine()) != null) {
                temp1 = temp0.split(" ");
                k = dic.indexOf(temp1[0]);
                if (k != -1) {
                    vec[k] = temp1[1];
                }
            }
            br1.close();

            id=id+1;
            //bw1.write(id+" ");
            //System.out.println(title+" "+id);
            Integer tem=0;
            for (int i = 0; i < vec.length; i++) {

                bw1.write(vec[i].toString() + " ");
            }
            if (title.contains("n2")) {
                bw1.write("3 ");
            }
            else if (title.contains("p")) {
                bw1.write("2 ");
            }
            else {
                bw1.write("1 ");
            }
            bw1.newLine();
            bw1.close();

        }
    }
}
