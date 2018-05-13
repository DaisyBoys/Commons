package webtools.Lucene.idx_mgr;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import webtools.Lucene.query.LuceneQuery;
import webtools.Lucene.utils.BeanLockWriterIdx;
import webtools.Lucene.utils.GsonMapMgr;
import webtools.Lucene.utils.IKAnalyzer5x;
import webtools.Lucene.utils.LuceneConfig;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 索引管理
 */
public class MgrLuceneIdx {
    private IndexWriter indexWriter = null;
    private Directory directory = null;
    //private IKAnalyzer5x analyzer = new IKAnalyzer5x();
    private final GsonMapMgr pGsonMapMgr = new GsonMapMgr();
    private final LuceneQuery pLuceneQuery = new LuceneQuery();
    private BeanLockWriterIdx pBeanLockWriterIdx = null;

    public String yw_type = "res_info";//业务类型

    private int convertInt(String str) {
        try {
            if (str == null || "".equals(str)) {
                str = "0";
            }
            return Integer.parseInt(str);
        } catch (Exception e) {

        }
        return 0;
    }

    public long convertLong(final String str) {
        try {
            long l = (long) (convertDouble(str));
            return l;
        } catch (Exception e) {

        }
        return 0;
    }

    public double convertDouble(String str) {
        try {
            if (str == null || "".equals(str)) {
                str = "0";
            }
            double dd = (double) Math.round(Double.parseDouble(str) * 100) / 100;
            return getDoubbleFrm2(dd);
        } catch (Exception e) {

        }
        return 0;

    }

    public double getDoubbleFrm2(double dd) {
        dd = (double) Math.round(dd * 100) / 100;
        return dd;

    }

    /**
     * 初始化索引信息
     */
    private void init(String index_path) {
        try {
            IKAnalyzer5x analyzer = new IKAnalyzer5x();
            directory = FSDirectory.open(Paths.get(index_path));
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, indexWriterConfig);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 关闭
     */
    private void close() {
        try {
            if (indexWriter != null) {
                //indexWriter.forceMerge(10);
                indexWriter.commit();
                indexWriter.close();
                //indexWriter=null;
            }
            if (directory != null) {
                directory.close();
                //directory=null;
            }
        } catch (Exception e) {
        } finally {
            if (pBeanLockWriterIdx != null) {
                pBeanLockWriterIdx.lock.writeLock().unlock();
            }
        }
    }

    /**
     * 创建索引
     */
    public void doCreateOneDocIdx(String index_path, String id, final List<Map<String, Object>> list) {
        try {
//			List<Map<String,Object>> listFields =new ArrayList<Map<String,Object>>();
//			Map<String,Object> mapFiled=new ConcurrentHashMap<String, Object>();
//			mapFiled.put("field_id", "id");
//			listFields.add(mapFiled );
//			Map<String,Object> mapId=pLuceneQuery.getMap_byId(index_path, id, listFields);
//			String oldId=this.pGsonMapMgr.getString(mapId, "id");

            //==上锁==begin
            pBeanLockWriterIdx = LuceneConfig.getLock(index_path);
            if (pBeanLockWriterIdx != null) {
                pBeanLockWriterIdx.lock.writeLock().lock();
            }
            //==上锁==end
            if (indexWriter == null) {
                init(index_path);
            }
            //System.out.println("==开始===");
            Document oldDoc = getDoc_byId(id);
            Map<String, Object> mapDocField = this.getMap(oldDoc);

            //deleteDocById(id);
            Document doc = new Document();


            if (oldDoc != null) {
                oldDoc.removeFields("dysmyfield");//移除字段类型定义
                List<IndexableField> listDoc = oldDoc.getFields();
                for (int i = 0; i < listDoc.size(); i++) {
                    IndexableField idxF = listDoc.get(i);
                    //String OldFieldType=this.pGsonMapMgr.getString(mapDocField, idxF.name());
                    String FieldType = this.pGsonMapMgr.getString(mapDocField, idxF.name());
                    //System.out.println(idxF.name()+"=="+FieldType+"==="+idxF.stringValue());
                    if ("n_follow_cnt".equals(idxF.name()) || "n_visited_cnt".equals(idxF.name())) {
                        continue;
                    }
                    if ("StringField".equalsIgnoreCase(FieldType)) {
                        doc.add(new StringField(idxF.name(), idxF.stringValue(), Field.Store.YES));
                    } else if ("TextField".equalsIgnoreCase(FieldType)) {
                        doc.add(new TextField(idxF.name(), idxF.stringValue(), Field.Store.YES));
                    } else if ("IntField".equalsIgnoreCase(FieldType)) {
                        doc.add(new IntField(idxF.name(), this.convertInt(idxF.stringValue()), Field.Store.YES));//整数
                        doc.add(new NumericDocValuesField(idxF.name(), this.convertInt(idxF.stringValue())));//排序用，忒重要
                    } else {
                        doc.add(new StringField(idxF.name(), idxF.stringValue(), Field.Store.YES));
                    }
                    if (!"IntField".equalsIgnoreCase(FieldType)) {
                        if (idxF.fieldType().omitNorms()) {
                            int nL = idxF.stringValue().length();
                            if (nL < 200) {
                                doc.add(new SortedDocValuesField(idxF.name(), new BytesRef(idxF.stringValue())));//排序用，忒重要
                            }
                            //doc.add(new SortedDocValuesField(idxF.name(), new BytesRef(idxF.stringValue())));//排序用，忒重要
                        }
                    }
                }

            }

            doc.removeFields("id");
            doc.removeFields("yw_type");
            doc.add(new StringField("id", id, Field.Store.YES));////主键唯一编号 索引不分词
            doc.add(new StringField("yw_type", this.yw_type, Field.Store.YES));//业务类型
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> mapData = list.get(i);
                String field_id = pGsonMapMgr.getString(mapData, "field_id");
                try {
                    doc.removeFields(field_id);
                    doc.removeField(field_id);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                String queryType = pGsonMapMgr.getString(mapData, "queryType");
                String field_val = pGsonMapMgr.getString(mapData, "field_val");//内容

                mapDocField.put(field_id, queryType);//设置字段查询类型并保持

                if ("StringField".equalsIgnoreCase(queryType)) {
                    //System.out.println(field_id);
                    //org.apache.lucene.document.BinaryDocValuesField
                    doc.add(new StringField(field_id, field_val, Field.Store.YES));//索引和分词
                    doc.add(new SortedDocValuesField(field_id, new BytesRef(field_val)));//排序用，忒重要
                } else if ("IntField".equalsIgnoreCase(queryType)) {

                    doc.add(new IntField(field_id, this.convertInt(field_val), Field.Store.YES));//整数
                    doc.add(new NumericDocValuesField(field_id, this.convertInt(field_val)));//排序用，忒重要
                    //System.out.println(field_id+"=="+queryType+"==="+field_val);

                } else if ("TextField".equalsIgnoreCase(queryType)) {
                    doc.add(new TextField(field_id, field_val, Field.Store.YES));//索引和分词
                    int nL = field_val.length();
//					int nDiv=nL/32766;
//					int nMod=nL%32766;
//					int nPos=0;

                    if (nL < 200) {
                        doc.add(new SortedDocValuesField(field_id, new BytesRef(field_val)));//排序用，忒重要
                    }
                } else {
                    //org.apache.lucene.document.BinaryDocValuesField
                    //doc.add( new BinaryDocValuesField(field_id, new BytesRef(field_val.getBytes())));//索引和分词
                }

            }
            this.doSetFieldDocType(doc, mapDocField);
            //==处理填报的字段类型
            if (oldDoc == null) {
                indexWriter.addDocument(doc);//追加
            } else {
                //==更新
                indexWriter.updateDocument(new Term("id", id), doc);
            }
            //System.out.println("==结束===");
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            this.close();
        }
    }

    /**
     * 删除索引编号
     */
    public void deleteDocById(String idVal) {
        try {
            // 多条件必备神器
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            IKAnalyzer5x analyzer = new IKAnalyzer5x();
            // 条件一
            QueryParser parser = new QueryParser("id", analyzer);
            Query query;
            try {
                query = parser.parse(idVal);
                builder.add(query, Occur.MUST);
                // 条件二
                QueryParser parser1 = new QueryParser("yw_type", analyzer);
                Query query1 = parser1.parse(this.yw_type);
                builder.add(query1, Occur.MUST);
                indexWriter.deleteDocuments(builder.build());
                indexWriter.commit();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // contents必须含有条件一


            //indexWriter.deleteDocuments(query);


        } catch (IOException e) {

        } finally {

        }
    }

    public void doDeleteIdxs(String index_path, String[] ids) {
        try {
            //==上锁==begin
            pBeanLockWriterIdx = LuceneConfig.getLock(index_path);
            if (pBeanLockWriterIdx != null) {
                pBeanLockWriterIdx.lock.writeLock().lock();
            }
            //==上锁==end
            if (indexWriter == null) {
                init(index_path);
            }
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    String id = ids[i];
                    if (id == null) continue;
                    if ("".equals(id.trim())) continue;
                    deleteDocById(id);
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            this.close();
        }
    }

    /**
     * 按条件进行删除
     */
    public void doDeleteByTerm(String index_path, String field_id, String[] vals) {
        try {
            //==上锁==begin
            pBeanLockWriterIdx = LuceneConfig.getLock(index_path);
            if (pBeanLockWriterIdx != null) {
                pBeanLockWriterIdx.lock.writeLock().lock();
            }
            //==上锁==end
            if (indexWriter == null) {
                init(index_path);
            }
            if (vals != null) {
                for (int i = 0; i < vals.length; i++) {
                    String id = vals[i];
                    if (id == null) continue;
                    if ("".equals(id.trim())) continue;
                    deleteDocById(field_id, id);
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            this.close();
        }
    }

    private void deleteDocById(String name, String idVal) {
        try {
            // 多条件必备神器
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            IKAnalyzer5x analyzer = new IKAnalyzer5x();
            // 条件一
            QueryParser parser = new QueryParser(name, analyzer);
            Query query;
            try {
                query = parser.parse(idVal);
                builder.add(query, Occur.MUST);
                // 条件二
                QueryParser parser1 = new QueryParser("yw_type", analyzer);
                Query query1 = parser1.parse(this.yw_type);
                builder.add(query1, Occur.MUST);
                indexWriter.deleteDocuments(builder.build());
                indexWriter.commit();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // contents必须含有条件一


            //indexWriter.deleteDocuments(query);


        } catch (IOException e) {

        } finally {

        }
    }


    /**
     * 拆词
     */
    public String getAnalysisResult(String keyWord) throws Exception {
        String strRtn = "";
        IKAnalyzer5x analyzer = null;
        TokenStream tokenStream = null;
        try {
            analyzer = new IKAnalyzer5x(false);
            tokenStream = analyzer.tokenStream("content",
                    new StringReader(keyWord));

            tokenStream.reset();
            tokenStream.addAttribute(CharTermAttribute.class);
            while (tokenStream.incrementToken()) {
                CharTermAttribute charTermAttribute = tokenStream
                        .getAttribute(CharTermAttribute.class);
                strRtn += charTermAttribute.toString() + " ";
                //  System.out.println(charTermAttribute.toString());

            }
            tokenStream.end();


        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (analyzer != null) {
                analyzer.close();
            }
            if (tokenStream != null) {
                tokenStream.close();
            }
        }
        return strRtn;

    }

    /**
     * 分词器进行智能切分
     */
    public String getAnalysisResult_useSmart(String keyWord) throws Exception {
        String strRtn = "";
        IKAnalyzer5x analyzer = null;
        TokenStream tokenStream = null;
        try {
            analyzer = new IKAnalyzer5x(true);
            tokenStream = analyzer.tokenStream("content",
                    new StringReader(keyWord));

            tokenStream.reset();
            tokenStream.addAttribute(CharTermAttribute.class);
            while (tokenStream.incrementToken()) {
                CharTermAttribute charTermAttribute = tokenStream
                        .getAttribute(CharTermAttribute.class);
                strRtn += charTermAttribute.toString() + " ";
                //  System.out.println(charTermAttribute.toString());

            }
            tokenStream.end();


        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (analyzer != null) {
                analyzer.close();
            }
            if (tokenStream != null) {
                tokenStream.close();
            }
        }
        return strRtn;

    }

    /**
     * 正则替换用字符串
     */
    public List<String> getAnalysisResultReplace(String keyWord) throws Exception {
        List<String> listRtn = new ArrayList<String>();
        IKAnalyzer5x analyzer = null;
        TokenStream tokenStream = null;
        try {
            analyzer = new IKAnalyzer5x(false);
            tokenStream = analyzer.tokenStream("content",
                    new StringReader(keyWord));

            tokenStream.reset();
            tokenStream.addAttribute(CharTermAttribute.class);
            Map<String, String> mapTemp = new ConcurrentHashMap<String, String>();
            while (tokenStream.incrementToken()) {
                CharTermAttribute charTermAttribute = tokenStream
                        .getAttribute(CharTermAttribute.class);
                String str = charTermAttribute.toString();
                if (mapTemp.get(charTermAttribute.toString()) == null) {
                    listRtn.add(str);
                    mapTemp.put(str, str);
                }
                //  System.out.println(charTermAttribute.toString());

            }
            tokenStream.end();


        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (analyzer != null) {
                analyzer.close();
            }
            if (tokenStream != null) {
                tokenStream.close();
            }
        }
        return listRtn;

    }

    public Document getDoc_byId(final String idVal) {
        IKAnalyzer5x analyzer = null;
        IndexSearcher searcher = null;
        IndexReader indexReader = null;
        try {
            indexReader = DirectoryReader.open(directory);
            searcher = new IndexSearcher(indexReader);
            analyzer = new IKAnalyzer5x();
            // 多条件必备神器
            BooleanQuery.Builder builder = new BooleanQuery.Builder();

            //条件一
            final QueryParser parser = new QueryParser("id", analyzer);
            Query query = parser.parse(idVal);
            builder.add(query, Occur.MUST);
            //获取上一页的最后一个元素
            final ScoreDoc lastSd = getLastScoreDoc(1, 1, builder.build(), searcher, null);
            //通过最后一个元素去搜索下一页的元素
            TopDocs tds = searcher.searchAfter(lastSd, builder.build(), 1);
            //==结果集合处理
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);

                return doc;
            }

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            try {
                if (analyzer != null) {
                    analyzer.close();
                    analyzer = null;
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                if (indexReader != null) {
                    indexReader.close();
                    indexReader = null;
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }


        }
        return null;

    }

    private ScoreDoc getLastScoreDoc(final int pageIndex, final int pageSize, final Query query, final IndexSearcher searcher, final Sort sort) throws IOException {
        if (pageIndex == 1) return null;//如果是第一页就返回空
        int num = pageSize * (pageIndex - 1);//获取上一页的最后数量
        //Sort sort = new Sort();
        //TopDocs tds = searcher.search(query, num,sort);

        TopDocs tds = searcher.search(query, num, sort);

        // System.out.println(tds.totalHits);
        return tds.scoreDocs[num - 1];
    }

    /**
     * 得到JSON
     */
    private String getJson(Map<String, Object> map) {
        try {
            String jsonStr = this.pGsonMapMgr.fomatJson(map);
            return jsonStr;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }

    /**
     * 根据文档得到对应的MAP
     */
    private Map<String, Object> getMap(Document doc) {
        Map<String, Object> mapRtn = new ConcurrentHashMap<String, Object>();
        try {
            if (doc == null) return mapRtn;
            String str = doc.get("dysmyfield");
            if (str == null || str == null) {
                return mapRtn;
            }
            Map<String, Object> map = this.pGsonMapMgr.gsonFormatMapObj(str);
            if (map == null) return mapRtn;
            mapRtn = map;
        } catch (Exception e) {

        }
        return mapRtn;
    }

    /**
     * 设置搜索的引擎的输入字段类型
     */
    private void doSetFieldDocType(Document doc, Map<String, Object> map) {
        try {
            String jsonStr = this.getJson(map);
            //  System.out.println(jsonStr);
            doc.add(new TextField("dysmyfield", jsonStr, Field.Store.YES));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void main(String[] args) {
        try {
            MgrLuceneIdx pMgrLuceneIdx = new MgrLuceneIdx();
            String sss = pMgrLuceneIdx.getAnalysisResult("张龙");
            String a = pMgrLuceneIdx.getAnalysisResult_useSmart("张浩");
            System.out.println(sss);
            System.out.println(a);
        } catch (Exception arg2) {
            ;
        }
    }
}
