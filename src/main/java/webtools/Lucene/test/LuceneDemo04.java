package webtools.Lucene.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import webtools.Lucene.utils.IKAnalyzer5x;

import java.io.IOException;
import java.io.StringReader;

/**
 * 中文分词，IKAnalayzer,对索引结果实现高亮显示
 *
 * @author wugaoshang
 */
public class LuceneDemo04 {
    private static final Version version = Version.LUCENE_5_5_4;
    private Directory directory = null;
    private DirectoryReader ireader = null;
    private IndexWriter iwriter = null;
    //private IKAnalyzer analyzer;
    private IKAnalyzer5x pIKAnalyzer5x;

    //测试数据
    private String[] content = {
            "你好，我是中共人",
            "中华人民共和国",
            "中国人民从此站起来了",
            "Lucene是一个不错的全文检索的工具",
            "全文检索中文分词"
    };

    /**
     * 构造方法
     */
    public LuceneDemo04() {
        directory = new RAMDirectory();
    }

    private IKAnalyzer5x getAnalyzer() {
        if (pIKAnalyzer5x == null) {
            return new IKAnalyzer5x();
        } else {
            return pIKAnalyzer5x;
        }
    }


    /**
     * 创建索引
     */
    public void createIndex() {
        Document doc = null;
        try {
            IndexWriterConfig iwConfig = new IndexWriterConfig(getAnalyzer());
            iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
            iwriter = new IndexWriter(directory, iwConfig);
            for (String text : content) {
                doc = new Document();
                doc.add(new TextField("content", text, Field.Store.YES));
                iwriter.addDocument(doc);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (iwriter != null)
                    iwriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public IndexSearcher getSearcher() {
        try {
            if (ireader == null) {
                ireader = DirectoryReader.open(directory);
            } else {
                DirectoryReader tr = DirectoryReader.openIfChanged(ireader);
                if (tr != null) {
                    ireader.close();
                    ireader = tr;
                }
            }
            return new IndexSearcher(ireader);
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void searchByTerm(String field, String keyword, int num) throws InvalidTokenOffsetsException {
        IndexSearcher isearcher = getSearcher();
        Analyzer analyzer = getAnalyzer();
        //使用QueryParser查询分析器构造Query对象
        QueryParser qp = new QueryParser(field, analyzer);
        //这句所起效果？
        // qp.setDefaultOperator(QueryParser.OR_OPERATOR);
        try {
            Query query = qp.parse(keyword);
            ScoreDoc[] hits;

            //注意searcher的几个方法
            hits = isearcher.search(query, null, num).scoreDocs;

            // 关键字高亮显示的html标签，需要导入lucene-highlighter-xxx.jar
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

            for (int i = 0; i < hits.length; i++) {
                Document doc = isearcher.doc(hits[i].doc);
                // 内容增加高亮显示
                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(doc.get("content")));
                String content = highlighter.getBestFragment(tokenStream, doc.get("content"));
                System.out.println(content);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用过滤器查询
     *
     * @param field
     * @param keyword
     * @param num
     * @throws InvalidTokenOffsetsException
     */
    public void searchByTermFilter(String field, String keyword, int num) throws InvalidTokenOffsetsException {
        IndexSearcher isearcher = getSearcher();
        Analyzer analyzer = getAnalyzer();
        //使用QueryParser查询分析器构造Query对象
        QueryParser qp = new QueryParser(field, analyzer);
        //这句所起效果？
        qp.setDefaultOperator(QueryParser.OR_OPERATOR);
        try {
            Query query = qp.parse(keyword);
            Query q2 = qp.parse("全文检索");
            ScoreDoc[] hits;

            QueryWrapperFilter filter = new QueryWrapperFilter(q2);
            //注意searcher的几个方法
            hits = isearcher.search(query, filter, num).scoreDocs;

            // 关键字高亮显示的html标签，需要导入lucene-highlighter-xxx.jar
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

            for (int i = 0; i < hits.length; i++) {
                Document doc = isearcher.doc(hits[i].doc);
                // 内容增加高亮显示
                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(doc.get("content")));
                String content = highlighter.getBestFragment(tokenStream, doc.get("content"));
                System.out.println(content);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InvalidTokenOffsetsException {
        System.out.println("start");
        LuceneDemo04 ld = new LuceneDemo04();
        ld.createIndex();
        long start = System.currentTimeMillis();
        ld.searchByTerm("content", "国", 500);
        System.out.println("end search use " + (System.currentTimeMillis() - start) + "ms");
    }


}