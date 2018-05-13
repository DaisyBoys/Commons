package webtools.Lucene.idx_mgr;


import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import webtools.Lucene.utils.BeanLockWriterIdx;
import webtools.Lucene.utils.GsonMapMgr;
import webtools.Lucene.utils.IKAnalyzer5x;
import webtools.Lucene.utils.LuceneConfig;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 信息发布索引处理
 * */
public class WriterIdx {
	private IndexWriter indexWriter = null;
	private Directory directory=null;
	//private IKAnalyzer5x analyzer = new IKAnalyzer5x();
	private final GsonMapMgr pGsonMapMgr=new GsonMapMgr();
	private BeanLockWriterIdx pBeanLockWriterIdx=null;
	/**
	 * 初始化索引信息
	 * */
	private void init(){
		try {
			  IKAnalyzer5x analyzer = new IKAnalyzer5x();
		     directory = FSDirectory.open( Paths.get( LuceneConfig.INDEX_PATH ) );
		     IndexWriterConfig indexWriterConfig = new IndexWriterConfig( analyzer );
		     indexWriter = new IndexWriter( directory, indexWriterConfig );
		      
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 * 关闭
	 * */
	private void close(){
		try {
			if(indexWriter!=null){
				indexWriter.commit();
				indexWriter.close();
				//indexWriter=null;
			}
			if(directory!=null){
				directory.close();
				//directory=null;
			}
		} catch (Exception e) {
		}finally{
			if(pBeanLockWriterIdx!=null){
				pBeanLockWriterIdx.lock.writeLock().unlock();
			}
		}
	}
	/**
	 * 创建信息索引
	 * */
	public void doCreateIdx(final List<Map<String,Object>> list){
		try {
			//==上锁==begin
			pBeanLockWriterIdx=LuceneConfig.getLock(LuceneConfig.INDEX_PATH);
			if(pBeanLockWriterIdx!=null){
				pBeanLockWriterIdx.lock.writeLock().lock();
			}
			//==上锁==end
			if(indexWriter==null){
				init();
			}
			for(int i=0;i<list.size();i++){
				Map<String,Object> mapData=list.get(i);
				Document doc = new Document();
				String id=pGsonMapMgr.getString(mapData, "id");//主键唯一编号
				String name=pGsonMapMgr.getString(mapData, "name");//检索的标题信息
				String contents=pGsonMapMgr.getString(mapData, "contents");//主体内容简介
				//==文件处理
				doc.add( new StringField( "id", id, Field.Store.YES ) );//索引不分词
				doc.add( new TextField("name", name, Field.Store.YES ) );//索引和分词
				doc.add( new TextField( "contents", contents, Field.Store.YES ) );
				
				doc.add( new StringField("AllFieldFlag", "★", Field.Store.YES)); //全部索引 
				indexWriter.addDocument( doc );//追加
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			this.close();
		}
	}
	/**
	 * 删除所有
	 * */
	 public void deleteDoc(String field, String key) {
	    try {
	    	//==上锁==begin
			pBeanLockWriterIdx=LuceneConfig.getLock(LuceneConfig.INDEX_PATH);
			if(pBeanLockWriterIdx!=null){
				pBeanLockWriterIdx.lock.writeLock().lock();
			}
			//==上锁==end
			
	    	if(indexWriter==null){
				init();
			}
	        indexWriter.deleteDocuments(new Term(field, key));
	        indexWriter.commit();
	       
	    } catch (IOException e) {
	        e.printStackTrace();
	    }finally{
			this.close();
		}
	}
	 public static void main(String[] args) {
		 WriterIdx pWriterIdx=new WriterIdx();
		
		 try {
		    List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			for(int i=0;i<100000;i++){
				Map<String,Object> map=new ConcurrentHashMap<String, Object>();
				map.put("id", 0+"");
				map.put("name", "你问我测试对吗");
				map.put("contents", "中文测试"+i);
				list.add(map);
			}
			pWriterIdx.doCreateIdx(list);
			 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	   
	
}
