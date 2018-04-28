package webtools.common;

import java.io.*;

/**
  * Created by �����շƴ�ҵ�Ƽ����޹�˾
 * User: ������
 * Date: 2008-5-6
 * Time: 8:20:21
 * �ļ����?�����ļ����룬�ļ��洢
 */
public class FileMgr {
	 /**
     * ��ȡ�ļ����ַ�strCodingFormat ����ȡ GBK��UTF-8��ISO8859-1�ȱ����ʽ��
     * @param File �ļ�����·����
     * @param strCodingFormat �ļ��ַ�����ʽ��
     * @return �����ַ����顣
     * */
     public  final String getFileContent(final String strFile,final String strCodingFormat) throws Exception
	{
		String strContent = "";
        String CodingFormat="GB2312";
         //�������ı����ʽ���ǡ���,then �滻ʹ�õĸ�ʽΪ����ĸ�ʽ
         if (strCodingFormat != null && !strCodingFormat.trim().equals("")){
               CodingFormat =strCodingFormat;
         }
        InputStreamReader readr  = null;
        FileInputStream fis=null;
        try{

        	final  File file = new File(strFile);
           
            if (file.exists()){
                fis = new FileInputStream(strFile);
                readr = new InputStreamReader(fis,CodingFormat);
                int  fileLength = fis.available();
                char  template[] = new char[fileLength];
                int nPos=readr.read(template);

                readr.close();
                readr=null;
                strContent =String.valueOf(template,0,nPos);

            }

        }catch(IOException ie){
        	if (readr==null) readr.close();
            return "";
        }finally{
        	if(readr!=null){
        		readr.close();
        	}
        	if(fis!=null){
        		fis.close();
        		fis=null;
        	}
        }
        return strContent;
	}

    /**
     * ����һ���ַ��ļ���strCodingFormat ����ȡ GBK��UTF-8��ISO8859-1�ȱ����ʽ��
     * @param File �ļ�����·����
     * @param Content ���ļ����ݡ�
     * @param strCodingFormat �ļ��ַ�����ʽ��
     * @return ���ɹ��򷵻�true,���򷵻�false��
     * */
    public  boolean saveContent2File(String File,String Content,String strCodingFormat)
	{
		String RealPath = File;
        OutputStreamWriter writer = null;
         String CodingFormat="GBK";
         //�������ı����ʽ���ǡ���,then �滻ʹ�õĸ�ʽΪ����ĸ�ʽ
         if (strCodingFormat != null && !strCodingFormat.trim().equals("")){
               CodingFormat =strCodingFormat;
         }
        try{
            writer  = new  OutputStreamWriter(new FileOutputStream(RealPath),CodingFormat);
            if (writer!=null){
                synchronized (writer){ //
                writer.write(Content);
                }
            }
            return true;
       	}catch(Exception ie){
        	return false;
       	}
        finally{
            try{
                writer.close();
            }
            catch(Exception ex)
            {
            }
        }
	}


    /**
	 * �ж��ļ���Ŀ¼�Ƿ����
	 * */
    public   boolean isFileExist(String pathName) throws Exception
	{
    	final File file = new File(pathName);
         if(file.exists()){
            return true;
         }
         return false;
    }
    /**
	 * ����Ŀ¼
	 * */
    public   void mkdir(String pathName) throws Exception
	{
    	final File file = new File(pathName);

        if(!file.exists()){
            synchronized(file){
              file.mkdirs();
            }
        }
    }

    /**
     * ɾ��һ���ļ�
     * */
    public   boolean deleteFile(String file) throws Exception
	{
    	final File f = new File(file);
        boolean b=false;
        if(f!=null){
            synchronized(f){
               b=f.delete();
            }
        }
	    return b;
    }
    public  void delDir(String dir ){
    	final File file = new File(dir);
        deleteDirFile(file);

    }
    /*ɾ��Ŀ¼*/
    private  void deleteDirFile(File file)
    {

        if (file.isDirectory())
        {
            File []array = file.listFiles();
            for (int i=0;i<array.length;i++) // ɾ��Ŀ¼�µ���Ŀ¼���ļ�
            {
                deleteDirFile(array[i]);
             }
        // ��Ŀ¼�µ�����ɾ����ٰѱ�Ŀ¼ɾ��
        //    System.out.println("fileDir="+file.getAbsolutePath());
            synchronized(file){
                file.delete();
                file.deleteOnExit();
            }
        } else{
           // System.out.println("fileName="+file.getAbsolutePath());
            synchronized(file){
                file.delete();
                file.deleteOnExit();
            }
        }
     }
    /**
     * ����
     * */
    public boolean copyFile(String from,String to){

        File fromFile,toFile;
        fromFile = new File(from);
        toFile = new File(to);
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try{
          toFile.createNewFile();
          fis = new FileInputStream(fromFile);
          fos = new FileOutputStream(toFile);
          int bytesRead;
          byte[] buf = new byte[4 * 1024];  // 4K buffer
          while((bytesRead=fis.read(buf))!=-1){
            fos.write(buf,0,bytesRead);
          }
          fos.flush();
          fos.close();
          fos=null;
          fis.close();
        }catch(IOException e){
         // System.out.println(e);
          return false;
        }finally{
        	if(fos!=null){
       		 try {
					fos.close();
					fos=null;
				} catch (IOException e) {
					
				}
                
        	}
        	if(fis!=null){
        		try {
        			fis.close();
        			fis=null;
				} catch (IOException e) {
					
				}
        	}
       	
       }
    return true;

  }
    public boolean copyFileByfis(FileInputStream fis ,String to){

        File fromFile,toFile;
        //fromFile = new File(from);
        toFile = new File(to);
        //FileInputStream fis = null;
        FileOutputStream fos = null;

        try{
          toFile.createNewFile();
         // fis = new FileInputStream(fromFile);
          fos = new FileOutputStream(toFile);
          int bytesRead;
          byte[] buf = new byte[8 * 1024];  // 4K buffer
          while((bytesRead=fis.read(buf))!=-1){
            fos.write(buf,0,bytesRead);
          }
          fos.flush();
          fos.close();
          fos=null;
         
        }catch(IOException e){
         // System.out.println(e);
          return false;
        }finally{
        	if(fos!=null){
        		 try {
					fos.close();
					fos=null;
				} catch (IOException e) {
					
				}
                 
        	}
        	
        }
    return true;

  }

}
