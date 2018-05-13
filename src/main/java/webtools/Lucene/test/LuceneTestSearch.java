package webtools.Lucene.test;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import webtools.Lucene.utils.IKAnalyzer5x;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class LuceneTestSearch {
    private IKAnalyzer5x pIKAnalyzer5x;

    private IKAnalyzer5x getAnalyzer() {
        if (pIKAnalyzer5x == null) {
            return new IKAnalyzer5x();
        } else {
            return pIKAnalyzer5x;
        }
    }

    /**
     * 单条件查询
     *
     * @param indexDir
     * @param q
     * @throws Exception
     */
    public void search(String indexDir, String q) throws Exception {
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        IKAnalyzer5x analyzer = new IKAnalyzer5x(true);
        TopScoreDocCollector f = TopScoreDocCollector.create(10);

        // 搜索目标是 contents
        QueryParser parser = new QueryParser("contents", analyzer);
        // 传入关键字，进行分析
        Query query = parser.parse(q);

        Sort sort = new Sort();
//      TopFieldCollector c = TopFieldCollector.create(sort, 100, false, false, false );
//      indexSearcher.search(query, c);
//      ScoreDoc[] hits = c.topDocs(101, 102).scoreDocs;

        indexSearcher.search(query, f);
        // TopDocs topDocs = f.topDocs(10, 20);
        // 分页，这里取前十个
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("总数据量=" + topDocs.totalHits);


        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            // 获取搜索结果
            Document document = indexSearcher.doc(scoreDoc.doc);
            List<IndexableField> list = document.getFields();
            System.out.println(document.get("id") + document.get("name"));
        }

        indexReader.close();
        directory.close();
    }

    /**
     * 根据页码和分页大小获取上一次的最后一个scoredocs
     *
     * @param pageIndex
     * @param pageSize
     * @param query
     * @param searcher
     * @return
     * @throws IOException
     */
    private ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher searcher) throws IOException {
        if (pageIndex == 1) return null;//如果是第一页就返回空
        int num = pageSize * (pageIndex - 1);//获取上一页的最后数量
        TopDocs tds = searcher.search(query, num);
        System.out.println(tds.totalHits);
        return tds.scoreDocs[num - 1];
    }

    public void searchPageByAfter(String query, int pageIndex, int pageSize, String indexDir) {
        try {
            Directory directory = FSDirectory.open(Paths.get(indexDir));
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            IKAnalyzer5x analyzer = new IKAnalyzer5x(true);
            QueryParser parser = new QueryParser("contents", analyzer);
            Query q = parser.parse(query);

            //获取上一页的最后一个元素
            ScoreDoc lastSd = getLastScoreDoc(pageIndex, pageSize, q, searcher);
            //通过最后一个元素去搜索下一页的元素
            TopDocs tds = searcher.searchAfter(lastSd, q, pageSize);

            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println(sd.doc + ":" + doc.get("id") + "-->" + doc.get("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 多条件查询
     *
     * @param indexDir
     * @param q
     * @throws Exception
     */
    public void searchBooleanQuery(String indexDir, String q) throws Exception {
        long l1 = System.currentTimeMillis();
        Directory directory = FSDirectory.open(Paths.get(indexDir));

        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        IKAnalyzer5x analyzer = new IKAnalyzer5x();
        // 多条件必备神器
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        // 实际使用中一般是多目标搜索（根据 姓名、性别、年龄、学校等等），
        // QueryParser parser = new MultiFieldQueryParser( new String[]{"contents","fullPath"}, analyzer );
        // 条件一
        QueryParser parser = new QueryParser("fileName", analyzer);
        Query query = parser.parse(q);
        // contents必须含有条件一
        builder.add(query, Occur.MUST);
        // 条件二
        QueryParser parser1 = new QueryParser("fileName", analyzer);
        Query query1 = parser1.parse("b c");
        // fileName必须不能是 b 和 c
        builder.add(query1, Occur.MUST_NOT);

        TopDocs topDocs = indexSearcher.search(builder.build(), 10);
        System.out.print(topDocs.totalHits);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(document.get("fullPath"));
        }

        indexReader.close();
        directory.close();
        long l2 = System.currentTimeMillis();
        System.out.println(l2 - l1);
    }

    public static void main(String[] args) {

        try {
            LuceneTestSearch pLuceneTestSearch = new LuceneTestSearch();
            String indexDir = "D:/apache-tomcat-7.0.69/lucene_idx";
            String q = "2";
            long l1 = System.currentTimeMillis();
            // 搜索contents含有1的文件信息
            System.out.println("单条件查询：");
            //  pLuceneTestSearch.searchPageByAfter(q,1,10,indexDir);
            pLuceneTestSearch.search(indexDir, q);
            // 搜索contents含有1 但是除开文件b 和  c
            //  System.out.println( "多条件查询：" );
            //  pLuceneTestSearch.searchBooleanQuery( indexDir, q );
            long l2 = System.currentTimeMillis();
            System.out.println(l2 - l1);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}