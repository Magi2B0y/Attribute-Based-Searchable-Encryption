
package demo;
import scheme.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Searcher {
    final static String[] u = {"ECNU", "teacher", "doctor", "master", "bachelor", "2016", "2015", "2014"};
    final static String[] attrs = {"ECNU", "teacher"};//用户上传
    final static String[] words = {"2","6","7"};
    private static boolean fileLog = true;
    private static String logFileName = "E:/DeskDocument/1.txt";//指定程序执行结果保存的文件路径

    public static void main(String[] args) throws Exception {
        BswabePub pub = new BswabePub(); //A public key
        BswabeMsk msk = new BswabeMsk();//A master secret key
        BswabePrv prv;//A private key
        BswabeToken token;//token
        List<BswabeToken> tokens = new ArrayList<BswabeToken>();

        for (int i = 0; i < words.length; i++) {
            Bswabe.setup(u, pub, msk);// 生成公钥、主密钥；传入所有属性、公钥、主密钥
            prv = Bswabe.keygen(u, pub, msk, attrs);//生成私钥；传入所有属性，公钥，主密钥，搜索用户属性，这里只用了u的length
            token = Bswabe.tokgen(prv, pub, words[i]);//生成 token， 传入私钥，公钥，要搜索的关键字
            System.out.println(token);
            tokens.add(token);
        }
        System.out.println(tokens);
    }

}
