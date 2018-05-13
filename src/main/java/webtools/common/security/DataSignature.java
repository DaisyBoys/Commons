package webtools.common.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class DataSignature {

    public String generateSHA1withRSASigature(String priKey, String src) {
        try {

            Signature sigEng = Signature.getInstance("SHA1withRSA");

            byte[] pribyte = hexStrToBytes(priKey.trim());

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");

            RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
            sigEng.initSign(privateKey);
            sigEng.update(src.getBytes());

            byte[] signature = sigEng.sign();
            return bytesToHexStr(signature);
        } catch (Exception e) {
            //LogTools.log(LogTools.isError(),"SignUtil", "SignUtil:generateSHA1withRSASigature", e.toString());
            return null;
        }
    }


    public boolean verifySHA1withRSASigature(String pubKey, String sign, String src) {
        try {
            Signature sigEng = Signature.getInstance("SHA1withRSA");

            byte[] pubbyte = hexStrToBytes(pubKey.trim());


            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);


            sigEng.initVerify(rsaPubKey);
            sigEng.update(src.getBytes());

            byte[] sign1 = hexStrToBytes(sign);
            return sigEng.verify(sign1);


        } catch (Exception e) {
            //LogTools.log(LogTools.isError(),"SignUtil", "SignUtil:verifySHA1withRSASigature", e.toString());
            return false;
        }
    }


    public String[] genRSAKeyPair() {
        KeyPairGenerator rsaKeyGen = null;
        KeyPair rsaKeyPair = null;
        String[] keys = new String[2];
        try {
            rsaKeyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = new SecureRandom();
            String tempStr = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            int length = tempStr.length();
            String temp = "";
            for (int i = 0; i < 50; i++) {
                int randomInt = random.nextInt(length);
                temp += tempStr.substring(randomInt, randomInt + 1);
            }
            random.setSeed(temp.getBytes());

            rsaKeyGen.initialize(1024, random);
            rsaKeyPair = rsaKeyGen.genKeyPair();
            PublicKey rsaPublic = rsaKeyPair.getPublic();
            PrivateKey rsaPrivate = rsaKeyPair.getPrivate();

            keys[0] = bytesToHexStr(rsaPublic.getEncoded());
            keys[1] = bytesToHexStr(rsaPrivate.getEncoded());
            //System.out.println("public key:" + bytesToHexStr(rsaPublic.getEncoded()));
            //System.out.println("private key:" + bytesToHexStr(rsaPrivate.getEncoded()));
            //System.out.println("1024-bit RSA key GENERATED.");
        } catch (Exception e) {

        }
        return keys;
    }

    private String bytesToHexStr(
            byte[] bcd) {
        StringBuffer s = new StringBuffer(bcd.length * 2);

        for (int i = 0; i < bcd.length; i++) {
            s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
            s.append(bcdLookup[bcd[i] & 0x0f]);
        }

        return s.toString();
    }

    private byte[] hexStrToBytes(
            String s) {
        byte[] bytes;

        bytes = new byte[s.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(
                    s.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }

    private static final char[] bcdLookup =
            {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
            };

}
