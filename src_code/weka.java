import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ArffLoader;


public class weka {

    public static void getWekaResult(String arff, String out) {
        try{
            BufferedReader br_stock = new BufferedReader(new FileReader("stock.txt"));
            BufferedReader br_data = new BufferedReader(new FileReader(arff));
            BufferedWriter bw_out = new BufferedWriter(new FileWriter(out));

            ArrayList<String> list = new ArrayList<String>();
            String[] name;
            String tmp;
            while(((tmp=br_stock.readLine())!=null)){
                name = tmp.split(" ");
                list.add(name[0]);
            }
            br_stock.close();
            for(int k=0; k<1005; k++ )  //有效数据从1005行开始
                br_data.readLine();
            String label;
            for(int i = 0; i<list.size(); i++){
                bw_out.write(list.get(i)+"\t\t");
                label = br_data.readLine();
                label = label.replaceAll("[,0-9]","");
                bw_out.write(label);
                bw_out.newLine();

            }

            br_data.close();
            bw_out.close();

        }catch(Exception e){
            e.printStackTrace();
        }

    }


    public static void classify(String classifyName, Instances train, String result) throws Exception{
        try{


            Classifier cfs;  //初始化分类器
            cfs = (Classifier)Class.forName(classifyName).newInstance();
            cfs.buildClassifier(train);  //训练分类器

            //对无标签数据集分类
            Instances unlabled = new Instances(new BufferedReader(new FileReader("data.arff")));  //数据集
            unlabled.setClassIndex(unlabled.numAttributes()-1);
            Instances labled = new Instances(unlabled);

            for(int k = 0;k < unlabled.numInstances();k++){
                double clslable = cfs.classifyInstance(unlabled.instance(k));
                labled.instance(k).setClassValue(clslable);
            }
            BufferedWriter bw1  = new BufferedWriter(new FileWriter(result));
            bw1.write(labled.toString());
            bw1.newLine();
            bw1.flush();
            bw1.close();

            //交叉验证评估
            Evaluation eval = new Evaluation(train);
            eval.crossValidateModel(cfs, train, 10, new Random(1));
            System.out.println( classifyName+"的正确率：" + ((1- eval.errorRate())*100)+"%");
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        try{

            //读入训练文件
            File trainfile= new File("train.arff");   //训练集
            ArffLoader loader = new ArffLoader();
            loader.setFile(trainfile);
            Instances insTrain = loader.getDataSet();  //读入训练文件
            insTrain.setClassIndex(insTrain.numAttributes()-1);    //在使用样本之前一定要首先设置instances的classIndex，否则在使用instances对象是会抛出异常

            //分类
            classify("weka.classifiers.bayes.NaiveBayes",insTrain,"result_bayes.arff");
            classify("weka.classifiers.trees.J48",insTrain,"result_tree.arff");
            classify("weka.classifiers.trees.RandomForest",insTrain,"result_randomforest.arff");


            //输出分类结果
            weka.getWekaResult("result_bayes.arff","bayes_out.txt");
            weka.getWekaResult("result_tree.arff","tree_out.txt");
            weka.getWekaResult("result_randomforest.arff","randomforest_out.txt");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}