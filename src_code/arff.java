import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class arff {

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
        String OUT_PATH="data0.arff";
        BufferedWriter bw = new BufferedWriter(new FileWriter(OUT_PATH,true));
        int i;
        bw.write("@RELATION data0\n");
        bw.newLine();
        for(i=1;i<1001;i++){
            bw.write("@ATTRIBUTE"+"\t"+"word"+i+"\t"+"NUMERIC\n");
        }
        bw.write("@ATTRIBUTE"+"\t"+"class"+"\t"+"{negative,neutral,positive}"+"\n");
        bw.newLine();
        bw.write("@DATA");
        bw.newLine();
        //-------------
        String READ_PATH = ("chi_words.txt");
        ArrayList<String> dic = new ArrayList<String>();
        File file = new File(READ_PATH);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp;
        while ((temp = br.readLine()) != null) {
            dic.add(temp);
        }
        br.close();
        String[] vec = new String[1000];

        Integer id = 0;
        arff news = new arff();
        news.iteratorPath("tfidf_data/");
        for (String title : news.pathName) {
            String INPUT_PATH = ("tfidf_data/" + title);
            File file1 = new File(INPUT_PATH);
            for (int j = 0; j < vec.length; j++) {
                vec[j] = "0";
            }
            BufferedReader br1 = new BufferedReader(new FileReader(file1));
            String temp0;
            String[] temp1;
            int k;
            while ((temp0 = br1.readLine()) != null) {
                temp1 = temp0.split(" ");
                k = dic.indexOf(temp1[0]);
                if (k != -1) {
                    Integer a = Double.valueOf(temp1[1]).intValue();
                    vec[k] = a.toString();
                }
            }
            br.close();
            br1.close();

            for ( i = 0; i < vec.length; i++) {
                bw.write(vec[i].toString() + ",");
            }
//            bw.write("?");
//            if (title.contains("n2")) {
//                bw.write("neutral");
//            }
//            else if (title.contains("p")) {
//                bw.write("positive");
//            }
//            else {
//                bw.write("negative");
//            }
//            bw.write("neutral");
            bw.newLine();

        }
        //--------------
        bw.close();

    }
}