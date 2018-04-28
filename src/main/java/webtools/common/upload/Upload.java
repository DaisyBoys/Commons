package webtools.common.upload;
import java.io.*;
import java.net.URL;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class Upload {
	private String saveDir = "."; 						//Ҫ�����ļ���·��
	private String charset = null; 						//�ַ�
	private List<String> tmpFileName = new ArrayList<String>(); 	    //��ʱ����ļ������ݽṹ
	private Hashtable<String,String> parameter = new Hashtable<String,String>(); 		//��Ų������ֵ����ݽṹ
	private HttpServletRequest request; 				//���ڴ�����������ʵ��
	private String boundary = ""; 						//�ڴ���ݵķָ���
	protected int len = 0; 								//ÿ�δ�������ʵ�ʶ������ֽڳ���
	private String queryString;
	private int count; 									//���ص��ļ�����
	private long maxFileSize = 0;//1024 * 1024 * 10; 		//����ļ������ֽ�;
	private String tagFileName = "";

	//���ý��ѡ�
	private Process proc = null;

	public void init(HttpServletRequest request) throws ServletException {
		this.request = request;
		boundary = request.getContentType().substring(30); //�õ��ڴ�����ݷֽ��
		queryString = request.getQueryString();
	}
	
	public void init(HttpServletRequest request,boolean isplan) throws ServletException {
		this.request = request;
		boundary = request.getContentType().substring(30); //�õ��ڴ�����ݷֽ��
		queryString = request.getQueryString();
		if(isplan)
		{
			Process tmpproc = new Process();
			tmpproc.setContentsize(request.getContentLength());
			request.getSession().setAttribute("upproc", tmpproc);
			proc = (Process)request.getSession().getAttribute("upproc");
		}
		
	}

	public String getParameter(String s) { //���ڵõ�ָ���ֶεĲ���ֵ,��дrequest.getParameter(String s)
		if (parameter.isEmpty()) {
			return null;
		}
		return (String) parameter.get(s);
	}

	public String[] getParameterValues(String s) { //���ڵõ�ָ��ͬ���ֶεĲ�������,��дrequest.getParameterValues(String s)
		List<String> al = new ArrayList<String>();
		if (parameter.isEmpty()){
			return null;
		}
		Enumeration<String> e = parameter.keys();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			if ( -1 != key.indexOf(s + "||||||||||") || key.equals(s)) {
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
/*
	public String[] getFileName() {
		return fileName;
	}
*/
	public List<String> getFileName(){
		return tmpFileName;
	}
	public void setMaxFileSize(long size) {
		maxFileSize = size;
	}

	public void setTagFileName(String filename) {
		tagFileName = filename;
	}

	public void setSaveDir(String saveDir) { //���������ļ�Ҫ�����·��
		this.saveDir = saveDir;
		File testdir = new File(saveDir); //Ϊ�˱�֤Ŀ¼����,���û�����½���Ŀ¼
		if (!testdir.exists()) {
			testdir.mkdirs();
		}
	}

	public void setCharset(String charset) { //�����ַ�
		this.charset = charset;
	}

	public boolean uploadFile() throws ServletException, IOException { //�û����õ����ط���
		if(charset == null){
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
				if (i >= 0) { // ���һ�ηֽ���ڵ���������filename=,˵�����ļ��ı�������
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
					try{
						FileOutputStream dos = new FileOutputStream(saveDir + "/" + fName);
						long size = 0l;
						while ((line = readLine(buffer, servletinputstream, null)) != null) {
							if (line.indexOf(boundary) != -1) {
								break;
							}
							size += len;
							if (size > maxFileSize && maxFileSize > 0) {
								throw new IOException("�ļ�����" + maxFileSize + "�ֽ�!");
							}
							dos.write(buffer, 0, len);
						}
						dos.close();
					}
					catch(Exception ex){
						System.out.println(ex.getMessage());
					}
				} else { // �������ֶα��������
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
			fileName[k] = (String) tmpFileName.get(k); //��ArrayList����ʱ�ļ���������й��û�����
		}
		if (fileName.length == 0) {
			return false; //���fileName���Ϊ��˵��û�������κ��ļ�
		}
		*/
		return true;
	}

	private void put(String key, String value, Hashtable<String,String> ht) {
		if (!ht.containsKey(key)) {
			ht.put(key, value);
		} else { // ����Ѿ�����ͬ���KEY,��Ҫ�ѵ�ǰ��key����,ͬʱҪע�ⲻ�ܹ��ɺ�KEYͬ��
			try {
				Thread.currentThread().sleep(1); // Ϊ�˲���ͬһms�в���������ͬ��key
			} catch (Exception e) {
			}
			key += "||||||||||" + System.currentTimeMillis();
			ht.put(key, value);
		}
	}

	/*
	 * ����ServletInputstream.readLine(byte[] b,int
	 * offset,length)����,�÷����Ǵ�ServletInputstream���ж�һ��
	 * ��ָ����byte����,Ϊ�˱�֤�ܹ�����һ��,��byte[]b��Ӧ��С��256,��д��readLine��,������һ����Ա����lenΪ
	 * ʵ�ʶ������ֽ���(�е��в���256),�����ļ�����д��ʱӦ�ô�byte������д�����len���ȵ��ֽڶ������byte[]
	 * �ĳ���,����д������������ص���String�Ա����ʵ������,���ܷ���len,���԰�len��Ϊ��Ա����,��ÿ�ζ�����ʱ ��ʵ�ʳ��ȸ�����.
	 * Ҳ����˵�ڴ��?�ļ�������ʱ��ݼ�Ҫ��String��ʽ�����Ա������ʼ�ͽ�����,��Ҫͬʱ��byte[]����ʽд���ļ� �������.
	 */
	private String readLine(byte[] Linebyte,ServletInputStream servletinputstream, String charset) {
		try {
			len = servletinputstream.readLine(Linebyte, 0, Linebyte.length);
			if (len == -1) {
				return null;
			}
			if(proc != null)proc.setProcess(len);
			if (charset == null) {
				return new String(Linebyte, 0, len);
			} else {
				return new String(Linebyte, 0, len, charset);
			}

		} catch (Exception _ex) {
			return null;
		}

	}

	protected String getFileName(String line) { // �������ַ��з�����ļ���
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

	private String getKey(String line) { // �������ַ��з�����ֶ���
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
	
	public Enumeration getParameterNames()
	{
		return parameter.keys();
	}
	
	public void downLoad(String filePath, HttpServletResponse response,	boolean isOnLine) throws Exception {
		File f = new File(filePath);
		if (!f.exists()) {
			response.sendError(404, "File not found!");
			return;
		}
		BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
		byte[] buf = new byte[1024];
		int len = 0;

		response.reset(); // �ǳ���Ҫ
		if (isOnLine) { // ���ߴ򿪷�ʽ
			URL u = new URL("file:///" + filePath);
			response.setContentType(u.openConnection().getContentType());
			response.setHeader("Content-Disposition", "inline; filename="
					+ f.getName());
			// �ļ���Ӧ�ñ����UTF-8
		} else { // �����ط�ʽ
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ f.getName());
		}
		OutputStream out = response.getOutputStream();
		while ((len = br.read(buf)) > 0)
			out.write(buf, 0, len);
		br.close();
		out.close();
	} 
}
