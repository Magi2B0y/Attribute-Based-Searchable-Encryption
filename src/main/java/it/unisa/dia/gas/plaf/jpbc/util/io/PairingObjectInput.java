package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.IOException;
import java.io.ObjectInput;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingObjectInput implements ObjectInput {

    private ObjectInput objectInput;
    private CurveParameters curveParameters;
    private Pairing pairing;


    public PairingObjectInput(ObjectInput objectInput) {
        this.objectInput = objectInput;
    }


    public Object readObject() throws ClassNotFoundException, IOException {
        return objectInput.readObject();
    }

    public int read() throws IOException {
        return objectInput.read();
    }

    public int read(byte[] b) throws IOException {
        return objectInput.read(b);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return objectInput.read(b, off, len);
    }

    public long skip(long n) throws IOException {
        return objectInput.skip(n);
    }

    public int available() throws IOException {
        return objectInput.available();
    }

    public void close() throws IOException {
        objectInput.close();
    }

    public void readFully(byte[] b) throws IOException {
        objectInput.readFully(b);
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        objectInput.readFully(b, off, len);
    }

    public int skipBytes(int n) throws IOException {
        return objectInput.skipBytes(n);
    }

    public boolean readBoolean() throws IOException {
        return objectInput.readBoolean();
    }

    public byte readByte() throws IOException {
        return objectInput.readByte();
    }

    public int readUnsignedByte() throws IOException {
        return objectInput.readUnsignedByte();
    }

    public short readShort() throws IOException {
        return objectInput.readShort();
    }

    public int readUnsignedShort() throws IOException {
        return objectInput.readUnsignedShort();
    }

    public char readChar() throws IOException {
        return objectInput.readChar();
    }

    public int readInt() throws IOException {
        return objectInput.readInt();
    }

    public long readLong() throws IOException {
        return objectInput.readLong();
    }

    public float readFloat() throws IOException {
        return objectInput.readFloat();
    }

    public double readDouble() throws IOException {
        return objectInput.readDouble();
    }

    public String readLine() throws IOException {
        return objectInput.readLine();
    }

    public String readUTF() throws IOException {
        return objectInput.readUTF();
    }

    public CurveParameters readCurveParameters() throws IOException, ClassNotFoundException {
        this.curveParameters = (CurveParameters) objectInput.readObject();
        this.pairing = PairingFactory.getPairing(curveParameters);

        return curveParameters;
    }

    public Pairing getPairing() {
        return pairing;
    }

    public Field readField() throws IOException {
        Pairing.PairingFieldIdentifier identifier = Pairing.PairingFieldIdentifier.values()[readInt()];
        switch (identifier) {
            case G1:
                return pairing.getG1();
            case G2:
                return pairing.getG2();
            case GT:
                return pairing.getGT();
            case Zr:
                return pairing.getZr();
            default:
                throw new IllegalArgumentException("Illegal Field Identifier.");
        }
    }


    public Element readElement(Pairing.PairingFieldIdentifier fieldIdentifier) throws IOException {
        Element e;
        switch (fieldIdentifier) {
            case G1:
                e = pairing.getG1().newElement();
                break;
            case G2:
                e = pairing.getG2().newElement();
                break;
            case GT:
                e = pairing.getGT().newElement();
                break;
            case Zr:
                e = pairing.getZr().newElement();
                break;
            default:
                throw new IllegalArgumentException("Invalid Field Type.");
        }

        byte[] buffer = new byte[readInt()];
        readFully(buffer);
        e.setFromBytes(buffer);

        return e;
    }

    public Element[] readElements(Pairing.PairingFieldIdentifier identifier) throws IOException{
        int num = readInt();
        Element[] elements = new Element[num];
        for (int i = 0; i < num; i++) {
            elements[i] = readElement(identifier);
        }

        return elements;
    }

    public PairingPreProcessing readPairingPreProcessing() throws IOException {
        int size = readInt();
        byte[] buffer = new byte[size];
        readFully(buffer);

        return getPairing().pairing(buffer, 0);
    }

    public ElementPowPreProcessing readElementPowPreProcessing() throws IOException {
        // Read field identifier
        Field field = readField();

        // read the preprocessing information
        int size = readInt();
        byte[] buffer = new byte[size];
        readFully(buffer);

        return field.pow(buffer, 0);
    }

    public int[] readInts() throws IOException {
        int num = readInt();
        int[] elements = new int[num];
        for (int i = 0; i < num; i++) {
            elements[i] = readInt();
        }

        return elements;
    }

    public byte[] readBytes() throws IOException {
        int length = readInt();
        byte[] buffer = new byte[length];
        readFully(buffer);

        return buffer;
    }

}
