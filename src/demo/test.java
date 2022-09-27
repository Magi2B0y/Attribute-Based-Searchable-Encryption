//package demo;
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.security.Security;
//import java.util.Arrays;
//
//import javax.crypto.Cipher;
//import javax.crypto.CipherInputStream;
//import javax.crypto.CipherOutputStream;
//import javax.crypto.NoSuchPaddingException;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
///**
// * @author ZSL
// * @since 2016年12月7日上午10:18:27
// * @desc [文件加密]
// */
//public class test {
//
//    private static final String key = "password";
//
//    /**
//     * init AES Cipher
//     * @param passsword
//     * @param cipherMode
//     * @return
//     */
//    public static Cipher initAESCipher(String passsword, int cipherMode) {
//        Cipher cipher = null;
//        try {
//            SecretKey key = getKey(passsword);
//            cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
//            cipher.init(cipherMode, key);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//        return cipher;
//    }
//
//    private static SecretKey getKey(String password) {
//        int keyLength = 256;
//        byte[] keyBytes = new byte[keyLength / 8];
//        SecretKeySpec key = null;
//        try {
//            Arrays.fill(keyBytes, (byte) 0x0);
//            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//            byte[] passwordBytes = password.getBytes("UTF-8");
//            int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
//            System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
//
//            key = new SecretKeySpec(keyBytes, "AES");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return key;
//    }
//
//
//    /**
//     * AES 加密
//     * @param encryptPath
//     * @param decryptPath
//     * @param sKey
//     * @return
//     */
//    public static boolean encryptFile(String encryptPath, String decryptPath, String sKey){
//        File encryptFile = null;
//        File decryptfile = null;
//        CipherOutputStream cipherOutputStream = null;
//        BufferedInputStream bufferedInputStream = null;
//        try {
//            encryptFile = new File(encryptPath);
//            if(!encryptFile.exists()) {
//                throw  new NullPointerException("Encrypt file is empty");
//            }
//            decryptfile = new File(decryptPath);
//            if(decryptfile.exists()) {
//                decryptfile.delete();
//            }
//            decryptfile.createNewFile();
//
//            Cipher cipher = initAESCipher(sKey, Cipher.ENCRYPT_MODE);
//            cipherOutputStream = new CipherOutputStream(new FileOutputStream(decryptfile), cipher);
//            bufferedInputStream = new BufferedInputStream(new FileInputStream(encryptFile));
//
//            byte[] buffer = new byte[1024];
//            int bufferLength;
//
//            while ((bufferLength = bufferedInputStream.read(buffer)) != -1) {
//                cipherOutputStream.write(buffer, 0, bufferLength);
//            }
//            bufferedInputStream.close();
//            cipherOutputStream.close();
////            delFile(encryptPath);
//        }  catch (IOException e) {
//            delFile(decryptfile.getAbsolutePath());
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * AES 解密
//     * @param encryptPath
//     * @param decryptPath
//     * @param mKey
//     * @return
//     */
//
//    public static boolean decryptFile(String encryptPath, String decryptPath, String mKey){
//        File encryptFile = null;
//        File decryptFile = null;
//        BufferedOutputStream outputStream = null;
//        CipherInputStream inputStream = null;
//        try {
//            encryptFile = new File(encryptPath);
//            if(!encryptFile.exists()) {
//                throw new NullPointerException("Decrypt file is empty");
//            }
//            decryptFile = new File(decryptPath);
//            if(decryptFile.exists()) {
//                decryptFile.delete();
//            }
//            decryptFile.createNewFile();
//
//            Cipher cipher = initAESCipher(mKey, Cipher.DECRYPT_MODE);
//
//            outputStream = new BufferedOutputStream(new FileOutputStream(decryptFile));
//            inputStream = new CipherInputStream(new FileInputStream(encryptFile), cipher);
//
//            int bufferLength;
//            byte[] buffer = new byte[1024];
//
//            while ((bufferLength = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bufferLength);
//            }
//            inputStream.close();
//            outputStream.close();
////            delFile(encryptPath);
//        } catch (IOException e) {
//            delFile(decryptFile.getAbsolutePath());
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            return false;
//        }
//        return true;
//    }
//
//
//    /**
//     * delete File
//     * @param pathFile
//     * @return
//     */
//    public static boolean delFile(String pathFile) {
//        boolean flag = false;
//        if(pathFile == null && pathFile.length() <= 0) {
//            throw new NullPointerException("文件不能为空");
//        }else {
//            File file = new File(pathFile);
//            // 路径为文件且不为空则进行删除
//            if (file.isFile() && file.exists()) {
//                file.delete();
//                flag = true;
//            }
//        }
//        return flag;
//    }
//
//
//    public static void main(String[] args) {
//        boolean flag = AESFileUtil.encryptFile
//                ("E:/pdf/html/原文件.txt", "E:/pdf/html/加密后.txt", key);
//        System.out.println(flag);
//        flag = AESFileUtil.decryptFile
//                ( "E:/pdf/html/加密后.txt","E:/pdf/html/解密后.txt", key);
//        System.out.println(flag);
//    }
//
//
//}
