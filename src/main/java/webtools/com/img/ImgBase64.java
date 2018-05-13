package webtools.com.img;

import sun.misc.BASE64Encoder;

import java.io.*;

//图片BASE64编解码
public class ImgBase64 {

    public String Base64Encoder(String imageURL) {
        BASE64Encoder encoder = new BASE64Encoder();
        StringBuilder pictureBuffer = new StringBuilder();
        InputStream input = null;
        ByteArrayOutputStream out = null;
        try {

            input = new FileInputStream(new File(imageURL));
            out = new ByteArrayOutputStream();
            byte[] temp = new byte[2048];
            for (int len = input.read(temp); len != -1; len = input.read(temp)) {
                out.write(temp, 0, len);
                pictureBuffer.append(encoder.encode(out.toByteArray()));
                //out(pictureBuffer.toString());
                out.reset();
            }

            // out(pictureBuffer.toString());
            //out("Encoding the picture Success");

            //   out.close();

            // BASE64Decoder decoder = new BASE64Decoder();
            // FileOutputStream write = new FileOutputStream(new File("c:/test2.png"));
            // byte[] decoderBytes = decoder.decodeBuffer(pictureBuffer.toString());
            // write.write(decoderBytes);
            // out("Decoding the picture Success");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                    input = null;
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
        }
        String rtn = pictureBuffer.toString();
        rtn = rtn.replaceAll("\r", "");
        rtn = rtn.replaceAll("\n", "");
        return rtn;
    }

}
