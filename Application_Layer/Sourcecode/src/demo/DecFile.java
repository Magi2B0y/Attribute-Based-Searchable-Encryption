package demo;

import org.apache.commons.cli.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.Base64.Decoder;

import static demo.cpabeDec.cpabeDecAPI;

public class DecFile {

    //    final static String[] attrs = {"ECNU", "teacher"};
    final static String ivParameter = "AAAABBBBCCCCDDDD";
//    private static String DirName = "../DownloadFiles";

    public static void main(String[] args) throws Exception {

        String[] attrs = null;
        String[] EncFiles = null;

        try {
            Options options = new Options();
            Option arg_help = Option.builder("h").longOpt("help").desc("show commands help").build();
            Option arg_SeacherAttrs = Option.builder("s").longOpt("UserAttrs").hasArg().desc("the attributes of files seacher").build();
            Option arg_EncFiles = Option.builder("e").longOpt("EncFile").hasArg().desc("keyword search").build();

            options.addOption(arg_help);
            options.addOption(arg_SeacherAttrs);
            options.addOption(arg_EncFiles);
            CommandLine commandLine = new DefaultParser().parse(options, args);

            if (commandLine.hasOption("h")) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("java -jar Decfile.jar [options]", options);
                System.exit(0);
            }
            String str_SeacherAttrs = commandLine.getOptionValue("s");
            String str_EncFiles = commandLine.getOptionValue("e");

            attrs = str_SeacherAttrs.split(" ");
            EncFiles = str_EncFiles.split(" ");

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("Use -h to check the usage");
            System.exit(0);
        }

        for (int i = 0; i < EncFiles.length; i++) {
            File file = new File(EncFiles[i]);
            String FileName = file.getName();
            String PlainFile = "./PlainFiles/" + FileName;
            decryptfile(EncFiles[i], ivParameter, PlainFile, attrs);
        }

    }

    public static void decryptfile(String encfile, String ivParameterm, String PlainFile, String[] attrs) throws Exception {
        // ciphertext.txt
        File file = new File(encfile);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String key = br.readLine();
//        System.err.println(key);
        String Data = br.readLine();
//        System.out.println("=====CipherText:=====\n" + Data);

        //解密AES密钥
        byte[] Basekey = key.getBytes();
        Decoder decoder1 = Base64.getDecoder();
        byte[] Enckey = decoder1.decode(Basekey);
        byte[] raw = cpabeDecAPI(attrs, Enckey);
//        System.out.println(new String(raw));

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivParameterm.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        // PlainFile
        byte[] data = Data.getBytes();
        Decoder decoder2 = Base64.getDecoder();
        byte[] bytIn = decoder2.decode(data);
        byte[] bytOut = cipher.doFinal(bytIn);

        File outfile = new File(PlainFile);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outfile));
//        System.out.println("====PlainText:====\n" + new String(bytOut));
        bos.write(bytOut);
        bos.close();
        System.out.println("Decrypted File: " + PlainFile);
    }
}