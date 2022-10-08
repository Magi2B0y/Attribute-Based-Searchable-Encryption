package demo;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64.Encoder;

public class EncFile {

    //实验操作路径
    public static String main(String sourcetext) {
        String EncFilePath = null;
        //key： 加密密钥
        String key = createRandomStr(32);
        System.out.println("key:" + key);
        //ivParameter：AES cbc加密模式的iv向量
        String ivParameter = "AAAABBBBCCCCDDDD";
        try {
            String CipherDir = "UploadFiles";
            File file = new File(sourcetext);
            EncFilePath = encryptfile(file, key, ivParameter, CipherDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EncFilePath;

    }

    public static String encryptfile(File file, String key, String ivParameterm, String CipherDir) throws Exception {
        //读取 file
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] bytIn = new byte[(int) file.length()];
        bis.read(bytIn);
        System.out.println("====PlainText:====\n" + new String(bytIn));
        bis.close();

        // hash文件名
        MessageDigest md = MessageDigest.getInstance("MD5");// 生成一个MD5加密计算摘要
        md.update(bytIn);// 计算md5函数
        String Filename = new BigInteger(1, md.digest()).toString(16);// 16是表示转换为16进制数

        // AES加密
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivParameterm.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        // 写入CipherFile
        byte[] bytOut = cipher.doFinal(bytIn);
        Encoder encoder = Base64.getEncoder();
        bytOut = encoder.encode(bytOut);
        key = key + "\r\n";
        byte[] BytesKey = key.getBytes("ASCII");
        bytOut = byteMerger(BytesKey,bytOut);

        String CipherFile = CipherDir + "\\"+Filename;
        System.out.println("CipherFile: " + CipherFile);

        File outfile = new File(CipherFile);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outfile));
        System.out.println("====CipherText:====\n" + new String(bytOut) + '\n');
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

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

}