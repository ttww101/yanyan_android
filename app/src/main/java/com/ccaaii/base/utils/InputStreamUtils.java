package com.ccaaii.base.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 */
public class InputStreamUtils {

    final static int BUFFER_SIZE = 4096;

    /**
     * InputStream to String
     * @param in InputStream
     * @return String
     * @throws Exception
     *
     */
    public static String InputStream2String(InputStream in) throws Exception{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(),"ISO-8859-1");
    }

    /**
     * IputStream to String with encoding
     * @param in
     * @param encoding
     * @return
     * @throws Exception
     */
    public static String InputStream2String(InputStream in,String encoding) throws Exception{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(),"ISO-8859-1");
    }

    /**
     * String to InputStream
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream String2InputStream(String in) throws Exception{

        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));
        return is;
    }

    /**
     * InputStream to Byte
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] InputStream2Byte(InputStream in) throws IOException{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }

    /**
     * Byte[] to InputStream
     * @param in
     * @return InputStream
     * @throws Exception
     */
    public static InputStream byte2InputStream(byte[] in) throws Exception{

        ByteArrayInputStream is = new ByteArrayInputStream(in);
        return is;
    }

    /**
     * byte[] to String
     * @param in
     * @return String
     * @throws Exception
     */
    public static String byte2String(byte[] in) throws Exception{

        InputStream is = byte2InputStream(in);
        return InputStream2String(is);
    }

}