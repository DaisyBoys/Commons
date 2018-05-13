package webtools.Lucene.test;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import webtools.Lucene.utils.IKAnalyzer5x;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

public class LuceneCreateIndex {
    private IndexWriter indexWriter = null;

    public LuceneCreateIndex(String indexDir) throws Exception {
        // IKAnalyzer 有独特之处，同时支持多种语言的分词
        IKAnalyzer5x analyzer = new IKAnalyzer5x();
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(directory, indexWriterConfig);

    }

    /**
     * 将测试数据文件写入索引
     *
     * @param dataDir
     * @throws Exception
     */
    public void createIndex(String dataDir) throws Exception {
        File[] files = new File(dataDir).listFiles();
        for (File file : files) {
            Document document = getDocument(file);
            indexWriter.addDocument(document);

            System.out.println("已建立索引文件:" + file.getCanonicalPath());
        }
        //indexWriter.forceMerge(maxNumSegments)
        indexWriter.forceMerge(1000);
        indexWriter.commit();
        indexWriter.close();
    }

    public Document getDocument(File file) throws Exception {
        Document document = new Document();
        document.add(new TextField("contents", new FileReader(file)));
        document.add(new TextField("fileName", file.getName(), Field.Store.YES));
        //  document.add(new Field("title", "lucene introduction", Field.Store.YES, Field.Index.ANALYZED));
        document.add(new TextField("fullPath", file.getCanonicalPath(), Field.Store.YES));

        return document;
    }

    /**
     * 索引生成,优化,合并索引文件,减少生成索引文件数
     *
     * @throws Exception
     */
    public void testDirectoryOptimize(String indexPath) throws Exception {
        //创建文件系统的索引库
        // Directory fsDir = FSDirectory.getDirectory(indexPath);
        //构造索引器
        //IndexWriter fsIndexWriter = new IndexWriter(fsDir,analyzer,MaxFieldLength.LIMITED);
        //使用索引器,优化的生成索引
        //fsIndexWriter.optimize();
        //fsIndexWriter.close();
    }

    public static void main(String[] args) {
        String indexDir = "D:/apache-tomcat-7.0.69/lucene_idx";// 索引文件生成目录
        String dataDir = "E:\\dys\\docment\\2017\\03\\电子保单";// 测试数据目录（测试数据见下图）
        try {
            new LuceneCreateIndex(indexDir).createIndex(dataDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}