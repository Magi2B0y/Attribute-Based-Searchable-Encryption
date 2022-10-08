
package demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import index.Index;
import it.unisa.dia.gas.jpbc.Element;
import org.apache.commons.lang.StringUtils;
import scheme.Bswabe;
import scheme.BswabeCph;
import scheme.BswabeMsk;
import scheme.BswabePub;

import java.util.*;

public class Uploader {
    final static String[] u = {"ECNU", "teacher", "doctor", "master", "bachelor", "2016", "2015", "2014"};
    final static String[] policy = {"ECNU", "teacher"};
    final static String[] file = {"SourceFiles\\1.txt", "SourceFiles\\2.txt"};//包含关键字的所有文件
    final static String[] all_indexes = {"4", "2", "3"};
    List<String> UploadFilesList = null;

    public static void main(String[] args) throws Exception {
        //加密文件内容
        for (int i = 0; i < file.length; i++) {
            String EncFilePath = EncFile.main(file[i]);
            file[i] = EncFilePath;
        }

//        加密索引
        BswabePub pub = new BswabePub(); //A public key
        BswabeMsk msk = new BswabeMsk();//A master secret key
        BswabeCph cph;//public BswabePolicy p
        List<String> EncIndexes = new ArrayList<String>();
        Map dict = new HashMap();

//        for (int i = 0; i < all_indexes.length; i++) {
//            Index index = new Index(all_indexes[i], file);
//            Bswabe.setup(u, pub, msk);// 生成公钥、主密钥；传入所有属性、公钥、主密钥
//            cph = Bswabe.enc(u, pub, policy, index);//开始加密；传入所有属性，公钥，访问策略，要加密的文件
//            System.out.println(cph.u_gate.getClass().getSimpleName());
//
//            //序列化
//            String jsonStr = JSON.toJSONString(cph, SerializerFeature.IgnoreNonFieldGetter);
////            jsonStr = prettyJson(jsonStr);
//            System.out.println(jsonStr);
//            EncIndexes.add(jsonStr);

            //反序列化
//            TypeReference<BswabeCph> type = new TypeReference<BswabeCph>(Element.class){ };
//            BswabeCph stu = JSON.parseObject(jsonStr, new TypeReference<BswabeCph>(){} );
//            System.out.println(stu);

            //验证
//            String jsonStr2 = JSON.toJSONString(stu, SerializerFeature.IgnoreNonFieldGetter);
//            jsonStr2 = prettyJson(jsonStr2);
//            System.out.println(jsonStr2);
//        }

        List filelist = Arrays.asList(file);
        dict.put(EncIndexes, filelist);
        System.out.println(dict);

    }

    public static String prettyJson(String json) {
        if (StringUtils.isBlank(json)) {
            return json;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(json, Feature.OrderedField);
        } catch (Exception e) {
            return json;
        }
        return JSONObject.toJSONString(jsonObject, true);
    }

}
