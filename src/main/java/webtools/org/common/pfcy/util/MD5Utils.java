/*     */ package webtools.org.common.pfcy.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/*     */ 
/*     */ public class MD5Utils
/*     */ {
/*  26 */   protected static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', 
/*  27 */     '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*  29 */   protected static MessageDigest messagedigest = null;
/*     */ 
/*     */   static {
/*     */     try { messagedigest = MessageDigest.getInstance("MD5");
/*     */     } catch (NoSuchAlgorithmException nsaex) {
/*  34 */       System.err.println(MD5Utils.class.getName() + 
/*  35 */         "��ʼ��ʧ�ܣ�MessageDigest��֧��MD5Util��");
/*  36 */       nsaex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getMD5String(String s)
/*     */   {
/*  47 */     return getMD5String(s.getBytes());
/*     */   }
/*     */ 
/*     */   public static boolean isEqualsToMd5(String password, String md5PwdStr)
/*     */   {
/*  60 */     String s = getMD5String(password);
/*  61 */     return s.equals(md5PwdStr);
/*     */   }
/*     */ 
/*     */   public static String getFileMD5String(File file)
/*     */     throws IOException
/*     */   {
/*  73 */     InputStream fis = new FileInputStream(file);
/*  74 */     byte[] buffer = new byte[1024];
/*  75 */     int numRead = 0;
/*  76 */     while ((numRead = fis.read(buffer)) > 0) {
/*  77 */       messagedigest.update(buffer, 0, numRead);
/*     */     }
/*  79 */     fis.close();
/*  80 */     return bufferToHex(messagedigest.digest());
/*     */   }
/*     */ 
/*     */   public static String getMD5String(byte[] bytes)
/*     */   {
/*  90 */     messagedigest.update(bytes);
/*  91 */     return bufferToHex(messagedigest.digest());
/*     */   }
/*     */ 
/*     */   private static String bufferToHex(byte[] bytes) {
/*  95 */     return bufferToHex(bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */   private static String bufferToHex(byte[] bytes, int m, int n) {
/*  99 */     StringBuffer stringbuffer = new StringBuffer(2 * n);
/* 100 */     int k = m + n;
/* 101 */     for (int l = m; l < k; l++) {
/* 102 */       appendHexPair(bytes[l], stringbuffer);
/*     */     }
/* 104 */     return stringbuffer.toString();
/*     */   }
/*     */ 
/*     */   private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
/* 108 */     char c0 = hexDigits[((bt & 0xF0) >> 4)];
/*     */ 
/* 110 */     char c1 = hexDigits[(bt & 0xF)];
/* 111 */     stringbuffer.append(c0);
/* 112 */     stringbuffer.append(c1);
/*     */   }
/** 
 * 加密解密算法 执行一次加密，两次解密 
*/  
public static String convertMD5(String inStr){  
	  
	        char[] a = inStr.toCharArray();  
	        for (int i = 0; i < a.length; i++){  
	            a[i] = (char) (a[i] ^ 't');  
	        }  
	        String s = new String(a);  
	        return s;  
	  
 }
public static void main(String[] args) {
	String s=("123sdfsdf");
	
	 
	        System.out.println("原始：" + s);  
	         System.out.println("MD5后：" + MD5Utils.getMD5String(s));  
	         System.out.println("加密的：" + MD5Utils.convertMD5(s));  
	        System.out.println("解密的：" + MD5Utils.convertMD5(MD5Utils.convertMD5(s)));  

}
/*     */ }

/* Location:           C:\Documents and Settings\10\����\a.zip
 * Qualified Name:     util.MD5Utils
 * JD-Core Version:    0.6.0
 */