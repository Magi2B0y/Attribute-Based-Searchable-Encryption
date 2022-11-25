
package demo;

import org.apache.commons.cli.*;
import scheme.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Searcher {
//    final static String[] u = {"ECNU", "teacher", "doctor", "master", "bachelor", "2016", "2015", "2014"};
//    final static String[] attrs = {"ECNU", "teacher"};//用户上传
//    final static String word = "4";

    public static void main(String[] args) throws Exception {

        String[] u = null;
        String[] attrs = null;
        String word = null;

        try {
            Options options = new Options();
            Option arg_help = Option.builder("h").longOpt("help").desc("show commands help").build();
            Option arg_attributes = Option.builder("a").longOpt("attributes").hasArg().desc("all attributes of user").build();
            Option arg_SeacherAttrs = Option.builder("s").longOpt("UserAttrs").hasArg().desc("the attributes of files seacher").build();
            Option arg_word = Option.builder("w").longOpt("keyword").hasArg().desc("keyword search").build();

            options.addOption(arg_help);
            options.addOption(arg_attributes);
            options.addOption(arg_SeacherAttrs);
            options.addOption(arg_word);
            CommandLine commandLine = new DefaultParser().parse(options, args);

            if (commandLine.hasOption("h")) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("java -jar FilesSeacher.jar [options]", options);
                System.exit(0);
            }
            String str_attributes = commandLine.getOptionValue("a");
            String str_SeacherAttrs = commandLine.getOptionValue("s");
            String str_word = commandLine.getOptionValue("w");

            u = str_attributes.split(" ");
            attrs = str_SeacherAttrs.split(" ");
            word = str_word;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("Use -h to check the usage");
            System.exit(0);
        }

        BswabePub pub = new BswabePub(); //A public key
        BswabeMsk msk = new BswabeMsk();//A master secret key
        BswabePrv prv;//A private key
        BswabeToken token;//token

        FileInputStream fileIn = new FileInputStream("./SerializedData/SearchableEncryption/Pub.ser");
        ObjectInputStream pubin = new ObjectInputStream(fileIn);
        pub = (BswabePub) pubin.readObject();
        pubin.close();

        FileInputStream fileIn2 = new FileInputStream("./SerializedData/SearchableEncryption/Msk.ser");
        ObjectInputStream mskin = new ObjectInputStream(fileIn2);
        msk = (BswabeMsk) mskin.readObject();
        mskin.close();

        prv = Bswabe.keygen(u, pub, msk, attrs);//生成私钥；传入所有属性，公钥，主密钥，搜索用户属性，这里只用了u的length
        token = Bswabe.tokgen(prv, pub, word);//生成 token， 传入私钥，公钥，要搜索的关键字

        FileOutputStream fileOut = new FileOutputStream("./SerializedData/SearchableEncryption/UserToken.ser");
        ObjectOutputStream TokenOut = new ObjectOutputStream(fileOut);
        TokenOut.writeObject(token);
        TokenOut.flush();
        TokenOut.close();
        System.out.println("Generate UserToken.ser: ./SerializedData/SearchableEncryption/UserToken.ser");

    }

}
