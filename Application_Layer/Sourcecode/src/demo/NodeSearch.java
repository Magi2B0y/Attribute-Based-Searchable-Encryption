
package demo;

import org.apache.commons.cli.*;
import scheme.Bswabe;
import scheme.BswabeCph;
import scheme.BswabePub;
import scheme.BswabeToken;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class NodeSearch {
    final static boolean DEBUG = true;
    static int flag = 0;

    public static void main(String[] args) throws Exception {

        BswabePub pub = new BswabePub(); //A public key
        BswabeToken token;//token
        BswabeCph cph;//public BswabePolicy p

        FileInputStream fileIn = new FileInputStream("./SerializedData/SearchableEncryption/Pub.ser");
        ObjectInputStream pubin = new ObjectInputStream(fileIn);
        pub = (BswabePub) pubin.readObject();
        pubin.close();

        FileInputStream fileIn1 = new FileInputStream("./SerializedData/SearchableEncryption/InvertedIndex.ser");
        ObjectInputStream in1 = new ObjectInputStream(fileIn1);
        cph = (BswabeCph) in1.readObject();
        in1.close();

        FileInputStream fileIn2 = new FileInputStream("./SerializedData/SearchableEncryption/UserToken.ser");
        ObjectInputStream in2 = new ObjectInputStream(fileIn2);
        token = (BswabeToken) in2.readObject();
        in2.close();

        boolean result = false;
        result = Bswabe.search(pub, token, cph);//把公钥、token、加密索引发送给服务器，让服务器进行匹配
//        println(result);
        if (result) {//搜索成功
            flag = 1;
            println("have found");
        }
        else {
            println("not found");
        }
    }
    private static void println(Object o) {
        if (DEBUG)
            System.out.println(o);
    }
}
