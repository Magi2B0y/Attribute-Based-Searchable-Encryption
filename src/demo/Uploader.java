
package demo;
import com.alibaba.fastjson.JSON;
import index.Index;
import scheme.Bswabe;
import scheme.BswabeCph;
import scheme.BswabeMsk;
import scheme.BswabePub;

import java.util.ArrayList;
import java.util.List;

public class Uploader {
    final static String[] u = {"ECNU", "teacher", "doctor", "master", "bachelor", "2016", "2015", "2014"};
    final static String[] policy = {"ECNU", "teacher"};
    final static String[] file = {"SourceFiles\\1.txt", "SourceFiles\\2.txt"};//包含关键字的所有文件
    final static String[] all_indexes = {"4", "2", "3"};

    public static void main(String[] args) throws Exception {
        //加密文件内容
//        for (int i=0;i< file.length;i++){
//            EncFile.main(file[i]);
//            file[i] = file[i].replace("SourceFiles","UploadFiles");
//        }

//        加密索引
        BswabePub pub = new BswabePub(); //A public key
        BswabeMsk msk = new BswabeMsk();//A master secret key
        BswabeCph cph;//public BswabePolicy p
        List<BswabeCph> EncIndexes = new ArrayList<BswabeCph>();

        for (int i = 0; i < all_indexes.length; i++) {
            Index index = new Index(all_indexes[i], file);
            Bswabe.setup(u, pub, msk);// 生成公钥、主密钥；传入所有属性、公钥、主密钥
            cph = Bswabe.enc(u, pub, policy, index);//开始加密；传入所有属性，公钥，访问策略，要加密的文件
            System.out.println(cph.u_gate);


            //序列化
            String jsonStr = JSON.toJSONString(cph);
            //反序列化
//            BswabeCph stu = JSON.parseObject(jsonStr, BswabeCph.class);
            System.out.println(jsonStr);


//            EncIndexes.add(cph);
        }
//        System.out.println(EncIndexes.get(0));





    }

}
