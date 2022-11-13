
package demo;

import index.Index;
import scheme.Bswabe;
import scheme.BswabeCph;
import scheme.BswabePub;
import java.io.*;

public class Uploader {
    final static String[] u = {"ECNU", "teacher", "doctor", "master", "bachelor", "2016", "2015", "2014"};
    final static String[] policy = {"ECNU", "teacher"};
    final static String[] files = {"SourceFiles\\1.txt", "SourceFiles\\2.txt"};//包含关键字的所有文件
    final static String FilesIndex = "4";

    public static void main(String[] args) throws Exception {
//        加密索引
        BswabePub pub = new BswabePub(); //A public key
        BswabeCph cph;//public BswabePolicy p

        FileInputStream fileIn = new FileInputStream("./tmp/Pub.ser");
        ObjectInputStream pubin = new ObjectInputStream(fileIn);
        pub = (BswabePub) pubin.readObject();
        pubin.close();

        //索引加密
        Index index = new Index(FilesIndex, files);
        cph = Bswabe.enc(u, pub, policy, index);//开始加密；传入所有属性，公钥，访问策略，要加密的索引

        // 序列化成byte数组
        FileOutputStream fileOut = new FileOutputStream("./tmp/InvertedIndex.ser");
        ObjectOutputStream cphOut = new ObjectOutputStream(fileOut);
        cphOut.writeObject(cph);
        cphOut.flush();
        cphOut.close();
        System.out.println("InvertedIndex.ser over");

        //验证
//        fileIn = new FileInputStream("./tmp/InvertedIndex.ser");
//        in = new ObjectInputStream(fileIn);
//        BswabeCph o = (BswabeCph) in.readObject();
//        in.close();
//        System.out.println(o.w);



//        List filelist = Arrays.asList(file);
//        dict.put(EncIndexes, filelist);
//        System.out.println(dict);

    }
}
