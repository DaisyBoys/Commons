package webtools.Lucene.utils;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Lucene 索引处理
 */
public class LuceneIdx {
    private IndexWriter indexWriter = null;
    private Directory directory = null;
    IKAnalyzer5x analyzer = new IKAnalyzer5x();

    /**
     * 初始化索引
     */
    public void createIdxWriter() {
        // IKAnalyzer 有独特之处，同时支持多种语言的分词
        try {
            directory = FSDirectory.open(Paths.get(LuceneConfig.INDEX_PATH));
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, indexWriterConfig);

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void createIndex(LuceneData pLuceneData) {
        try {
            Document doc = new Document();
            doc.add(new StringField("id", pLuceneData.getId(), Field.Store.YES));
            doc.add(new StringField("name", pLuceneData.getName(), Field.Store.YES));
            doc.add(new TextField("content", pLuceneData.getContent(), Field.Store.YES));
            if (indexWriter != null) {
                indexWriter.addDocument(doc);
                indexWriter.commit();
                indexWriter.close();
                directory.close();

            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {

        }
    }

    /**
     * 删除索引
     *
     * @throws IOException
     */

    public void deleteDoc(String field, String key) {
        IKAnalyzer5x analyzer = new IKAnalyzer5x();
        IndexWriterConfig icw = new IndexWriterConfig(analyzer);
        Path indexPath = Paths.get(LuceneConfig.INDEX_PATH);
        Directory directory;
        try {
            directory = FSDirectory.open(indexPath);

            IndexWriter indexWriter = new IndexWriter(directory, icw);
            indexWriter.deleteDocuments(new Term(field, key));
            indexWriter.commit();
            indexWriter.close();
            System.out.println("删除完成!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LuceneIdx pLuceneIdx = new LuceneIdx();
        pLuceneIdx.createIdxWriter();
        try {
            // pLuceneIdx.deleteDoc("id", "13");
            LuceneData pLuceneData = new LuceneData();
            pLuceneData.setId("13888");
            pLuceneData.setName("问题");
            pLuceneData.setContent("");
            pLuceneIdx.createIndex(pLuceneData);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
