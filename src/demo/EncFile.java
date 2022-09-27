package demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncFile {
    //实验操作路径
    private static String derectory = "E:\\DeskDocument";

    public static void main(String[] args) {
        //key： 加密密钥
        String key = createRandomStr(32);
        System.out.println("key:" + key);
        //ivParameter：AES cbc加密模式的iv向量
        String ivParameter = "AAAABBBBCCCCDDDD";
        try {
            String CipherFile = "ciphertext.txt";
            File file = new File(derectory + "\\" + "plaintext.txt");
            encryptfile(file, key, ivParameter, CipherFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void encryptfile(File file, String key, String ivParameterm, String CipherFile) throws Exception {
        //读取 file
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] bytIn = new byte[(int) file.length()];
        bis.read(bytIn);
        System.out.println("====PlainText:====\n" + new String(bytIn));
        bis.close();
        // AES加密
        byte[] raw = key.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivParameterm.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        // 写入CipherFile
        byte[] bytOut = cipher.doFinal(bytIn);
        File outfile = new File(derectory + "\\" + CipherFile);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outfile));
        System.out.println("====CipherText:====\n" + new String(bytOut));
        bos.write(bytOut);
        bos.close();
    }

    public static String createRandomStr(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            stringBuffer.append(str.charAt(number));
        }
        return stringBuffer.toString();
    }

}