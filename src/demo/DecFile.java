package demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DecFile {

    private static final int ZERO = 0;
    private static final int ONE = 1;
    //实验操作路径
    private static String derectory = "E:\\DeskDocument";

    public static void main(String[] args) {
        //key： 加密密钥
        String key="JkdqTZrvGcgrRbpF8NjTtYoT1ZXicqAa";
        String ivParameter = "AAAABBBBCCCCDDDD";
        try {
            String PlainFile = "plaintext.txt";
            File file = new File(derectory+"\\"+"ciphertext.txt");
            decryptfile(file,key,ivParameter,PlainFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decryptfile(File file, String key, String ivParameterm, String PlainFile) throws Exception {
        //读取 ciphertext.txt
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] bytIn = new byte[(int) file.length()];
        bis.read(bytIn);
        System.out.println("=====CipherText:=====\n" + new String(bytIn));
        bis.close();
        // AES解密
        byte[] raw = key.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivParameterm.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        // 写入 PlainFile
        byte[] bytOut = cipher.doFinal(bytIn);
        File outfile = new File(derectory + "\\" + PlainFile);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outfile));
        System.out.println("====PlainText:====\n" + new String(bytOut));
        bos.write(bytOut);
        bos.close();
    }

}