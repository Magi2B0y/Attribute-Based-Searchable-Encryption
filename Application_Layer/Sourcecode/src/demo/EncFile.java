package demo;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Random;

import static demo.cpabeEnc.cpabeEncAPI;

public class EncFile {

    //实验操作路径
    public static String EncFileAPI(String sourcetext, String[] policy) {
        String EncFilePath = null;
        //key： 加密密钥
        String key = createRandomStr(32);
//        System.out.println("key:" + key);
        //ivParameter：AES cbc加密模式的iv向量
        String ivParameter = "AAAABBBBCCCCDDDD";
        try {
            String CipherDir = "./UploadFiles";
            File file = new File(sourcetext);
            EncFilePath = encryptfile(file, key, policy, ivParameter, CipherDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EncFilePath;

    }

    public static String encryptfile(File file, String key, String[] policy, String ivParameterm, String CipherDir) throws Exception {
        //读取 file
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] bytIn = new byte[(int) file.length()];
        bis.read(bytIn);
//        System.out.println("====PlainText:====\n" + new String(bytIn));
        bis.close();

        // AES加密
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivParameterm.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        String Filename = file.getName();
        String CipherFile = CipherDir + "/"+Filename;
//        System.out.println("CipherFile: " + CipherFile);
        File outfile = new File(CipherFile);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outfile));

        // 写入CipherFile
        byte[] bytOut = cipher.doFinal(bytIn);
        Encoder encoder = Base64.getEncoder();
        bytOut = encoder.encode(bytOut);

        byte[] Enckey = cpabeEncAPI(key,policy);
        Encoder encoder2 = Base64.getEncoder();
        Enckey = encoder2.encode(Enckey);
        key = new String(Enckey) + "\r\n";
        byte[] BytesKey = key.getBytes("UTF-8");
        bos.write(BytesKey);

//        System.out.println("====CipherText:====\n" + new String(bytOut) + '\n');
        bos.write(bytOut);
        bos.close();
        return CipherFile;
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