package webtools.Lucene.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;


/**
 * http://blog.csdn.net/sd0902/article/details/8466608
 * http://www.javacui.com/framework/455.html
 * http://blog.csdn.net/fun913510024/article/details/46289007
 * http://www.cnblogs.com/sephy/p/3303711.html
 * http://blog.sina.com.cn/s/blog_95532bcb0102vl7b.html
 * http://blog.csdn.net/cuibruce/article/details/53462497
 * 例子中是先搜索title或content字段中的结果再与上type字段中的结果，即  ((title||content)&&type);
 * http://blog.csdn.net/rick_123/article/details/6637121
 * <p>
 * Title: HelloWorld.java
 * Package com.ccy.lucene
 * </p>
 * <p>
 * Description: lucene helloworld demo
 * <p>
 */
public class HelloWorld {
    //源文件
    String filePath = "D:\\newWork\\lucene5.3\\luceneSourceFile\\IndexWriter addDocument's a javadoc .txt";
    //索引文件
    String indexPath = "D:\\newWork\\lucene5.3\\luceneIndex";
    //分词器
    private Analyzer analyzer = new StandardAnalyzer();


    public void createIndex() throws Exception {
        //1.将文本转化为doc对象
        Document doc = file2Document(filePath);
        //2、打开索引库
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        Directory fsDir = FSDirectory.open(Paths.get(indexPath));
        //3、写入索引库
        IndexWriter indexWiter = new IndexWriter(fsDir, config);
        indexWiter.addDocument(doc);
        //4.关闭
        indexWiter.close();
    }


    public void search() throws Exception {
        //0、搜索关键字
        String queryString = "document";

        //1、打开索引库
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher searcher = new IndexSearcher(reader);

        //2、搜索解析器
        QueryParser parser = new QueryParser("content", analyzer);
        Query query = parser.parse(queryString);

        //3、根据关键词进行搜索
        TopDocs docs = searcher.search(query, 100);
        ;
        System.out.println("总共有【" + docs.totalHits + "】条匹配结果");

        //4、遍历结果并处理  
        for (ScoreDoc scoreDoc : docs.scoreDocs) {
            int docSn = scoreDoc.doc; // 文档内部编号
            Document doc = searcher.doc(docSn); // 根据编号取出相应的文档
            // 打印出文档信息
            System.out.println("------------------------------");
            System.out.println("name     = " + doc.get("name"));
            System.out.println("content  = " + doc.get("content"));
            System.out.println("size     = " + doc.get("size"));
            System.out.println("path     = " + doc.get("path"));
        }

        reader.close();
    }


    /**
     * //1.将文本转化为doc对象
     *
     * @param path
     * @return
     */
    public Document file2Document(String path) {
        File file = new File(path);
        Document doc = new Document();
        doc.add(new Field("name", file.getName(), StringField.TYPE_STORED));
        doc.add(new Field("content", readFileContent(file), TextField.TYPE_STORED));
        doc.add(new LongField("size", file.length(), LongField.TYPE_STORED));
        doc.add(new Field("path", file.getAbsolutePath(), StringField.TYPE_STORED));
        return doc;
    }

    /**
     * 读取文件内容
     */
    public static String readFileContent(File file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuffer content = new StringBuffer();

            for (String line = null; (line = reader.readLine()) != null; ) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


