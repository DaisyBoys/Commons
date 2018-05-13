package webtools.common.upload;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.*;


public class Upload {
    private String saveDir = ".";
    private String charset = null;
    private List<String> tmpFileName = new ArrayList<String>();
    private Hashtable<String, String> parameter = new Hashtable<String, String>();
    private HttpServletRequest request;
    private String boundary = "";
    protected int len = 0;
    private String queryString;
    private int count;
    private long maxFileSize = 0;
    private String tagFileName = "";


    private Process proc = null;

    public void init(HttpServletRequest request) throws ServletException {
        this.request = request;
        boundary = request.getContentType().substring(30);
        queryString = request.getQueryString();
    }

    public void init(HttpServletRequest request, boolean isplan) throws ServletException {
        this.request = request;
        boundary = request.getContentType().substring(30);
        queryString = request.getQueryString();
        if (isplan) {
            Process tmpproc = new Process();
            tmpproc.setContentsize(request.getContentLength());
            request.getSession().setAttribute("upproc", tmpproc);
            proc = (Process) request.getSession().getAttribute("upproc");
        }

    }

    public String getParameter(String s) {
        if (parameter.isEmpty()) {
            return null;
        }
        return (String) parameter.get(s);
    }

    public String[] getParameterValues(String s) {
        List<String> al = new ArrayList<String>();
        if (parameter.isEmpty()) {
            return null;
        }
        Enumeration<String> e = parameter.keys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            if (-1 != key.indexOf(s + "||||||||||") || key.equals(s)) {
                al.add(parameter.get(key));
            }
        }
        if (al.size() == 0) {
            return null;
        }
        String[] value = new String[al.size()];
        for (int i = 0; i < value.length; i++) {
            value[i] = (String) al.get(i);
        }
        return value;
    }

    public String getQueryString() {
        return queryString;
    }

    public int getCount() {
        return count;
    }


    public List<String> getFileName() {
        return tmpFileName;
    }

    public void setMaxFileSize(long size) {
        maxFileSize = size;
    }

    public void setTagFileName(String filename) {
        tagFileName = filename;
    }

    public void setSaveDir(String saveDir) {
        this.saveDir = saveDir;
        File testdir = new File(saveDir);
        if (!testdir.exists()) {
            testdir.mkdirs();
        }
    }

    public void setCharset(String charset) { //�����ַ�
        this.charset = charset;
    }

    public boolean uploadFile() throws ServletException, IOException {
        if (charset == null) {
            setCharset(request.getCharacterEncoding());
        }
        return uploadFile(request.getInputStream());
    }

    private boolean uploadFile(ServletInputStream servletinputstream) throws ServletException, IOException //ȡ�������ݵ�������
    {
        String line = null;
        byte[] buffer = new byte[256];
        while ((line = readLine(buffer, servletinputstream, charset)) != null) {
            if (line.startsWith("Content-Disposition: form-data;")) {
                int i = line.indexOf("filename=");
                if (i >= 0) {
                    String fName = getFileName(line);
                    fName = fName.substring(1, fName.length() - 1);
                    if (fName.equals("")) {
                        continue;
                    }
                    if (count == 0 && tagFileName.length() != 0) {
                        String ext = fName
                                .substring((fName.lastIndexOf(".") + 1));
                        fName = tagFileName + "." + ext;
                    }
                    tmpFileName.add(fName);
                    count++;
                    while ((line = readLine(buffer, servletinputstream, charset)) != null) {
                        if (line.length() <= 2) {
                            break;
                        }
                    }
                    //File f = new File(saveDir, fName);
                    //FileOutputStream dos = new FileOutputStream(f);
                    try {
                        FileOutputStream dos = new FileOutputStream(saveDir + "/" + fName);
                        long size = 0l;
                        while ((line = readLine(buffer, servletinputstream, null)) != null) {
                            if (line.indexOf(boundary) != -1) {
                                break;
                            }
                            size += len;
                            if (size > maxFileSize && maxFileSize > 0) {
                                throw new IOException("" + maxFileSize + "!");
                            }
                            dos.write(buffer, 0, len);
                        }
                        dos.close();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } else {
                    String key = getKey(line);
                    String value = "";
                    while ((line = readLine(buffer, servletinputstream, charset)) != null) {
                        if (line.length() <= 2) {
                            break;
                        }
                    }
                    while ((line = readLine(buffer, servletinputstream, charset)) != null) {

                        if (line.indexOf(boundary) != -1) {
                            break;
                        }
                        value += line;
                    }
                    put(key, value.trim(), parameter);
                }
            }
        }
        if (queryString != null) {
            String[] each = split(queryString, "&");
            for (int k = 0; k < each.length; k++) {
                String[] nv = split(each[k], "=");
                if (nv.length == 2) {
                    put(nv[0], nv[1], parameter);
                }
            }
        }
        /*
		fileName = new String[tmpFileName.size()];
		for (int k = 0; k < fileName.length; k++) {
			fileName[k] = (String) tmpFileName.get(k);
		}
		if (fileName.length == 0) {
			return false;
		}
		*/
        return true;
    }

    private void put(String key, String value, Hashtable<String, String> ht) {
        if (!ht.containsKey(key)) {
            ht.put(key, value);
        } else {
            try {
                Thread.currentThread().sleep(1);
            } catch (Exception e) {
            }
            key += "||||||||||" + System.currentTimeMillis();
            ht.put(key, value);
        }
    }

    private String readLine(byte[] Linebyte, ServletInputStream servletinputstream, String charset) {
        try {
            len = servletinputstream.readLine(Linebyte, 0, Linebyte.length);
            if (len == -1) {
                return null;
            }
            if (proc != null) proc.setProcess(len);
            if (charset == null) {
                return new String(Linebyte, 0, len);
            } else {
                return new String(Linebyte, 0, len, charset);
            }

        } catch (Exception _ex) {
            return null;
        }

    }

    protected String getFileName(String line) {
        if (line == null) {
            return "";
        }
        int i = line.indexOf("filename=");
        line = line.substring(i + 9).trim();
        i = line.lastIndexOf("");
        if (i < 0 || i >= line.length() - 1) {
            i = line.lastIndexOf("/");
            if (line.equals("")) {
                return "";
            }
            if (i < 0 || i >= line.length() - 1) {
                return line;
            }
        }
        return line.substring(i + 1, line.length() - 1);
    }

    private String getKey(String line) {
        if (line == null) {
            return "";
        }
        int i = line.indexOf("name=");
        line = line.substring(i + 5).trim();
        return line.substring(1, line.length() - 1);
    }

    private String[] split(String strOb, String mark) {
        if (strOb == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(strOb, mark);
        List<String> tmp = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            tmp.add(st.nextToken());
        }
        String[] strArr = new String[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            strArr[i] = (String) tmp.get(i);
        }
        return strArr;
    }

    public Enumeration getParameterNames() {
        return parameter.keys();
    }

    public void downLoad(String filePath, HttpServletResponse response, boolean isOnLine) throws Exception {
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;

        response.reset();
        if (isOnLine) {
            URL u = new URL("file:///" + filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename="
                    + f.getName());

        } else {
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + f.getName());
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        br.close();
        out.close();
    }
}
