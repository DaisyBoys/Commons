package webtools.common;

import java.io.*;


public class FileMgr {

    public final String getFileContent(final String strFile, final String strCodingFormat) throws Exception {
        String strContent = "";
        String CodingFormat = "GB2312";
        if (strCodingFormat != null && !strCodingFormat.trim().equals("")) {
            CodingFormat = strCodingFormat;
        }
        InputStreamReader readr = null;
        FileInputStream fis = null;
        try {

            final File file = new File(strFile);

            if (file.exists()) {
                fis = new FileInputStream(strFile);
                readr = new InputStreamReader(fis, CodingFormat);
                int fileLength = fis.available();
                char template[] = new char[fileLength];
                int nPos = readr.read(template);

                readr.close();
                readr = null;
                strContent = String.valueOf(template, 0, nPos);

            }

        } catch (IOException ie) {
            if (readr == null) readr.close();
            return "";
        } finally {
            if (readr != null) {
                readr.close();
            }
            if (fis != null) {
                fis.close();
                fis = null;
            }
        }
        return strContent;
    }


    public boolean saveContent2File(String File, String Content, String strCodingFormat) {
        String RealPath = File;
        OutputStreamWriter writer = null;
        String CodingFormat = "GBK";
        if (strCodingFormat != null && !strCodingFormat.trim().equals("")) {
            CodingFormat = strCodingFormat;
        }
        try {
            writer = new OutputStreamWriter(new FileOutputStream(RealPath), CodingFormat);
            if (writer != null) {
                synchronized (writer) {
                    writer.write(Content);
                }
            }
            return true;
        } catch (Exception ie) {
            return false;
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
    }


    public boolean isFileExist(String pathName) throws Exception {
        final File file = new File(pathName);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public void mkdir(String pathName) throws Exception {
        final File file = new File(pathName);

        if (!file.exists()) {
            synchronized (file) {
                file.mkdirs();
            }
        }
    }


    public boolean deleteFile(String file) throws Exception {
        final File f = new File(file);
        boolean b = false;
        if (f != null) {
            synchronized (f) {
                b = f.delete();
            }
        }
        return b;
    }

    public void delDir(String dir) {
        final File file = new File(dir);
        deleteDirFile(file);

    }


    private void deleteDirFile(File file) {

        if (file.isDirectory()) {
            File[] array = file.listFiles();
            for (int i = 0; i < array.length; i++) {
                deleteDirFile(array[i]);
            }
            synchronized (file) {
                file.delete();
                file.deleteOnExit();
            }
        } else {
            synchronized (file) {
                file.delete();
                file.deleteOnExit();
            }
        }
    }


    public boolean copyFile(String from, String to) {

        File fromFile, toFile;
        fromFile = new File(from);
        toFile = new File(to);
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            toFile.createNewFile();
            fis = new FileInputStream(fromFile);
            fos = new FileOutputStream(toFile);
            int bytesRead;
            // 4K buffer
            byte[] buf = new byte[4 * 1024];
            while ((bytesRead = fis.read(buf)) != -1) {
                fos.write(buf, 0, bytesRead);
            }
            fos.flush();
            fos.close();
            fos = null;
            fis.close();
        } catch (IOException e) {
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {

                }

            }
            if (fis != null) {
                try {
                    fis.close();
                    fis = null;
                } catch (IOException e) {

                }
            }

        }
        return true;

    }

    public boolean copyFileByfis(FileInputStream fis, String to) {

        File fromFile, toFile;
        //fromFile = new File(from);
        toFile = new File(to);
        //FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            toFile.createNewFile();
            // fis = new FileInputStream(fromFile);
            fos = new FileOutputStream(toFile);
            int bytesRead;
            // 4K buffer
            byte[] buf = new byte[8 * 1024];
            while ((bytesRead = fis.read(buf)) != -1) {
                fos.write(buf, 0, bytesRead);
            }
            fos.flush();
            fos.close();
            fos = null;

        } catch (IOException e) {
            // System.out.println(e);
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {

                }

            }

        }
        return true;

    }

}
