package iotoolbox.net.binarytool;

public class SuperBinaryObject {

    private Type type;

    private int intData;
    private int dataSize;

    private byte[] binaryData;


    private SuperBinaryObject() {
    }

    public static SuperBinaryObject getInstance(int data, int dataSize) {
        SuperBinaryObject superBinaryObject = new SuperBinaryObject();
        superBinaryObject.intData = data;
        superBinaryObject.type = Type.Int;
        superBinaryObject.dataSize = dataSize;
        return superBinaryObject;
    }

    public static SuperBinaryObject getInstance(byte[] data) {
        SuperBinaryObject superBinaryObject = new SuperBinaryObject();
        superBinaryObject.binaryData = data;
        superBinaryObject.type = Type.Binary;
        return superBinaryObject;
    }

    public Type getType() {
        return type;
    }

    public int getDataSize() {
        if (this.type == Type.Binary) return binaryData.length;
        return dataSize;
    }

    public int getIntData() {
        return intData;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }
}

enum Type {
    Int, Binary
}
