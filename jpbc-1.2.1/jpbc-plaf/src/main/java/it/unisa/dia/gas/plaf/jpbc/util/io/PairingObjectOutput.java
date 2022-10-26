package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.*;

import java.io.IOException;
import java.io.ObjectOutput;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingObjectOutput implements ObjectOutput {
    private ObjectOutput objectOutput;

    private Pairing pairing;


    public PairingObjectOutput(ObjectOutput objectOutput) {
        this.objectOutput = objectOutput;
    }

    public PairingObjectOutput(Pairing pairing, ObjectOutput objectOutput) {
        this.pairing = pairing;
        this.objectOutput = objectOutput;
    }


    public void writeObject(Object obj) throws IOException {
        objectOutput.writeObject(obj);
    }

    public void write(int b) throws IOException {
        objectOutput.write(b);
    }

    public void write(byte[] b) throws IOException {
        objectOutput.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        objectOutput.write(b, off, len);
    }

    public void flush() throws IOException {
        objectOutput.flush();
    }

    public void close() throws IOException {
        objectOutput.close();
    }

    public void writeBoolean(boolean v) throws IOException {
        objectOutput.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        objectOutput.writeByte(v);
    }

    public void writeShort(int v) throws IOException {
        objectOutput.writeShort(v);
    }

    public void writeChar(int v) throws IOException {
        objectOutput.writeChar(v);
    }

    public void writeInt(int v) throws IOException {
        objectOutput.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        objectOutput.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        objectOutput.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        objectOutput.writeDouble(v);
    }

    public void writeBytes(String s) throws IOException {
        objectOutput.writeBytes(s);
    }

    public void writeChars(String s) throws IOException {
        objectOutput.writeChars(s);
    }

    public void writeUTF(String s) throws IOException {
        objectOutput.writeUTF(s);
    }


    public void writeElement(Element element) throws IOException {
        if (element == null)
            writeInt(0);
        else {
            byte[] bytes = element.toBytes();
            writeInt(bytes.length);
            write(bytes);
        }
    }

    public void writeElements(Element[] elements) throws IOException {
        if (elements == null)
            writeInt(0);
        else {
            writeInt(elements.length);
            for (Element e : elements)
                writeElement(e);
        }
    }

    public void writePreProcessing(PreProcessing processing) throws IOException {
        byte[] buffer = processing.toBytes();

        if (processing instanceof ElementPowPreProcessing) {
            // write additional information on the field
            ElementPowPreProcessing powPreProcessing = (ElementPowPreProcessing) processing;
            writePairingFieldIdentifier(powPreProcessing.getField());
        }

        writeInt(buffer.length);
        writeBytes(buffer);
    }

    public void writeInts(int[] ints) throws IOException {
        if (ints == null) {
            writeInt(0);
        } else {
            writeInt(ints.length);
            for (int anInt : ints) writeInt(anInt);
        }
    }

    public void writeBytes(byte[] buffer) throws IOException{
        writeInt(buffer.length);
        write(buffer);
    }


    public Pairing getPairing() {
        return pairing;
    }


    protected void writePairingFieldIdentifier(Field field) throws IOException {
        Pairing.PairingFieldIdentifier identifier = getPairing().getPairingFieldIdentifier(field);
        if (identifier == Pairing.PairingFieldIdentifier.Unknown)
            throw new IllegalArgumentException("The field does not belong to the current pairing instance.");
        writeInt(identifier.ordinal());
    }



}