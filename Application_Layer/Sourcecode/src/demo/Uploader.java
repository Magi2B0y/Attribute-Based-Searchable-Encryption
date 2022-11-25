package demo;

import index.Index;
import org.apache.commons.cli.*;
import scheme.Bswabe;
import scheme.BswabeCph;
import scheme.BswabePub;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static demo.EncFile.EncFileAPI;

public class Uploader {
//    final static String[] a = {"ECNU", "teacher", "doctor", "master", "bachelor", "2016", "2015", "2014"};
//    final static String[] policy = {"ECNU", "teacher"};
//    final static String[] files = {"E:/DeskDocument/Application_Layer/SourceFiles/1.txt"};//包含关键字的所有文件
//    final static String FilesIndex = "4";

    public static void main(String[] args) throws Exception {
        String[] u = null;
        String[] policy = null;
        String[] files = null;
        String FilesIndex = null;

        try {
            Options options = new Options();
            Option arg_help = Option.builder("h").longOpt("help").desc("show commands help").build();
            Option arg_attributes = Option.builder("a").longOpt("attributes").hasArg().desc("all attributes of user").build();
            Option arg_policy = Option.builder("p").longOpt("policy").hasArg().desc("files access policy").build();
            Option arg_filesIndex = Option.builder("i").longOpt("index").hasArg().desc("index of the file list").build();
            Option arg_files = Option.builder("f").longOpt("files").hasArg().desc("the file list").build();
            options.addOption(arg_help);
            options.addOption(arg_attributes);
            options.addOption(arg_policy);
            options.addOption(arg_filesIndex);
            options.addOption(arg_files);
            CommandLine commandLine = new DefaultParser().parse(options, args);

            if (commandLine.hasOption("h")) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("java -jar FilesUploader.jar [options]", options);
                System.exit(0);
            }
            String str_attributes = commandLine.getOptionValue("a");
            String str_policy = commandLine.getOptionValue("p");
            String str_filesIndex = commandLine.getOptionValue("i");
            String str_files = commandLine.getOptionValue("f");

//            System.out.println(str_attributes);
//            System.out.println(str_policy);
//            System.out.println(str_filesIndex);
//            System.out.println(str_files);

            u = str_attributes.split(" ");
            policy = str_policy.split(" ");
            files = str_files.split(" ");
            FilesIndex = str_filesIndex;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("Use -h to check the usage");
            System.exit(0);
        }

        BswabePub pub = new BswabePub(); //A public key
        BswabeCph cph;//public BswabePolicy p

        FileInputStream fileIn = new FileInputStream("./SerializedData/SearchableEncryption/Pub.ser");
        ObjectInputStream pubin = new ObjectInputStream(fileIn);
        pub = (BswabePub) pubin.readObject();
        pubin.close();

        //索引加密
        Index index = new Index(FilesIndex, files);
        cph = Bswabe.enc(u, pub, policy, index);//开始加密；传入所有属性，公钥，访问策略，要加密的索引

        // 序列化成byte数组
        FileOutputStream fileOut = new FileOutputStream("./SerializedData/SearchableEncryption/InvertedIndex.ser");
        ObjectOutputStream cphOut = new ObjectOutputStream(fileOut);
        cphOut.writeObject(cph);
        cphOut.flush();
        cphOut.close();
        System.out.println("Encrypted FileIndex: ./SerializedData/SearchableEncryption/InvertedIndex.ser");

        //加密文件
        for (int i = 0; i < files.length; i++) {
            String EncFilePath = EncFileAPI(files[i],policy);
            System.out.println("Encrypted File: " + EncFilePath);
        }
    }

}
