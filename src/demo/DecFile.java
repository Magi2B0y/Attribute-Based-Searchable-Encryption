package demo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64.Decoder;

public class DecFile {

    //ʵ�����·��
    private static String derectory = "UploadFiles";

    public static void main(String[] args) {

        String ivParameter = "AAAABBBBCCCCDDDD";
        try {
            String PlainFile = "DownloadFiles\\plaintext.txt";
            File file = new File(derectory + "\\" + "1.txt");
            decryptfile(file, ivParameter, PlainFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decryptfile(File file, String ivParameterm, String PlainFile) throws Exception {
        //��ȡ ciphertext.txt
        BufferedReader br = new BufferedReader(new FileReader(file));
        String key = br.readLine();
        String DATA = br.readLine();
        System.out.println("=====CipherText:=====\n" + DATA);
        //AES����
        byte[] raw = key.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivParameterm.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        // д�� PlainFile
        byte[] data = DATA.getBytes();
        Decoder decoder = Base64.getDecoder();
        byte[] bytIn = decoder.decode(data);
        byte[] bytOut = cipher.doFinal(bytIn);
        File outfile = new File(PlainFile);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outfile));
        System.out.println("====PlainText:====\n" + new String(bytOut));
        bos.write(bytOut);
        bos.close();
    }
}