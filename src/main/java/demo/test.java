package demo;
import scheme.BswabeCph;
/**
 * @author ZSL
 * @since 2016年12月7日上午10:18:27
 * @desc [文件加密]
 */
import java.io.*;


public class test
{    public static void main(String[] args)
    {
        Employee e = new Employee();
        e.name = "Reyan Ali";
        e.address = "Phokka Kuan, Ambehta Peer";
        e.SSN = 11122333;
        e.number = 101;
        try
        {
//            FileInputStream fileIn = new FileInputStream("/tmp/employee.ser");
//            ObjectInputStream in = new ObjectInputStream(fileIn);
//            e = (Employee) in.readObject();
//            in.close();
//            fileIn.close();


            FileOutputStream fileOut = new FileOutputStream("./employee.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(e);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /tmp/employee.ser");
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }


}
class Employee implements java.io.Serializable
{
    public String name;
    public String address;
    public transient int SSN;
    public int number;
    public void mailCheck()
    {
        System.out.println("Mailing a check to " + name
                + " " + address);
    }
}