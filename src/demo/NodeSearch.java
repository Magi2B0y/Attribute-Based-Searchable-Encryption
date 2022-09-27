//
//package demo;
//
//import scheme.Bswabe;
//import scheme.BswabeCph;
//import scheme.BswabePub;
//import scheme.BswabeToken;
//import demo.Searcher;
//import demo.Uploader;
//
//public class NodeSearch {
//    final static boolean DEBUG = true;
//    static int flag = 0;
//
//    public static void main(String[] args) throws Exception {
//        BswabePub pub = new BswabePub(); //A public key
//        BswabeToken token;//token
//        BswabeCph cph;//public BswabePolicy p
//
//
//
//        boolean result = false;
//        result = Bswabe.search(pub, token, cph);//把公钥、token、加密索引发送给服务器，让服务器进行匹配
//
//        if (result) {//搜索成功
//            flag = 1;
//            println("have found!");
//            String[] fileReturned = index.file;
//            for (int i = 0; i < fileReturned.length; i++) {
//                println(fileReturned[i] + " ");
//            }
//        }
//
//
//    }
//
//    private static void println(Object o) {
//        if (DEBUG)
//            System.out.println(o);
//    }
//}
