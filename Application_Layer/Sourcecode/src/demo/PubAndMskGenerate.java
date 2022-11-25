package demo;

import org.apache.commons.cli.*;
import scheme.Bswabe;
import scheme.BswabeMsk;
import scheme.BswabePub;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class PubAndMskGenerate {
//    final static String []u={"ECNU","teacher", "doctor","master","bachelor","2016","2015","2014"};

    public static void main(String[] args) throws Exception {

        String[] u = null;
        try {
            Options options = new Options();
            Option arg_help = Option.builder("h").longOpt("help").desc("show commands help").build();
            Option arg_attributes = Option.builder("a").longOpt("attributes").hasArg().desc("all attributes of user").build();
            options.addOption(arg_help);
            options.addOption(arg_attributes);
            CommandLine commandLine = new DefaultParser().parse(options, args);
            if (commandLine.hasOption("h")) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("java -jar PubAndMskGenerate.jar [options]", options);
                System.exit(0);
            }
            String str_attributes = commandLine.getOptionValue("a");
            u = str_attributes.split(" ");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("Use -h to check the usage");
            System.exit(0);
        }

        BswabePub pub = new BswabePub(); //A public key
        BswabeMsk msk = new BswabeMsk();//A master secret key

        Bswabe.setup(u, pub, msk);

        FileOutputStream fileOut1 = new FileOutputStream("./SerializedData/SearchableEncryption/Pub.ser");
        ObjectOutputStream PubOut = new ObjectOutputStream(fileOut1);
        PubOut.writeObject(pub);
        PubOut.flush();
        PubOut.close();
        System.out.println("Generate Pub.ser: ./SerializedData/SearchableEncryption/Pub.ser");

        FileOutputStream fileOut2 = new FileOutputStream("./SerializedData/SearchableEncryption/Msk.ser");
        ObjectOutputStream MskOut = new ObjectOutputStream(fileOut2);
        MskOut.writeObject(msk);
        MskOut.flush();
        MskOut.close();
        System.out.println("Generate Msk.ser: ./SerializedData/SearchableEncryption/Msk.ser");


    }

}
