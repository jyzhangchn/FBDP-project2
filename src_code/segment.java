import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

public class segment {
    static String[] data;
    static final String OUT_PATH="test.txt";

    File stock;
    File[] files;
    List<String> pathName = new ArrayList<String>();

    public void iteratorPath(String dir) {
        stock = new File(dir);
        files = stock.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    pathName.add(file.getName());
                }else if (file.isDirectory()) {
                    iteratorPath(file.getAbsolutePath());
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        segment news = new segment();
        int k = 0;
        //遍历文件夹将每个txt的新闻标题存入新的文件中
        news.iteratorPath("/Users/apple/Documents/FBDP/project2/download_data");
        for (String title : news.pathName) {
            String READ_PATH = ("/Users/apple/Documents/FBDP/project2/download_data/"+title);
            String OUT_PATH = ("/Users/apple/Documents/FBDP/project2/news/"+title);
            //首先读取文件
            ArrayList<String> list = new ArrayList<String>();
            File file = new File(READ_PATH);
//            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"gbk"));
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(OUT_PATH,true));
            String temp;
            while((temp=br.readLine())!=null){
                list.add(temp);
            }
            br.close();
            //然后存到数组中
            data = new String[6];
            for (int i = 0; i < list.size(); i++) {
                data = list.get(i).split("  ");
                if(data.length!=5||!data[4].contains("http")||data[3].contains("http")||data[3].isEmpty()) {
                    continue;
                }
                //对新闻标题分词
//                List<Word> tempseg = WordSegmenter.seg(list.toString());
                List<Word> tempseg = WordSegmenter.seg(data[3]);
                //把新闻的标题输出到文件
                String titles = StringUtils.strip(tempseg.toString().replaceAll("[,.%/(A-Za-z0-9)]",""),"[]");
                titles = titles.replace("\\s+"," ");
                titles = titles.replace("|"," ");
                bw.write(titles);
                bw.newLine();
            }
            bw.close();

        }
    }

}
