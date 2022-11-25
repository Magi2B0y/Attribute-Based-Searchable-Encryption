package demo;

import org.apache.commons.cli.*;

import java.io.File;
import java.util.Arrays;

public class test {

        public static void main(String[] args) throws InterruptedException {

            String str_EncFiles = "../DownloadFiles/1.txt aaaa aaa";
            String[] EncFiles = str_EncFiles.split(" ");
            System.out.println(Arrays.toString(EncFiles));
        }
    }

