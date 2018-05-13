package webtools.Lucene.query;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import webtools.Lucene.utils.GsonMapMgr;
import webtools.Lucene.utils.IKAnalyzer5x;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 查询管理
 * */
public class LuceneQuery {
	public String yw_type="res_info";
	private final GsonMapMgr pGsonMapMgr=new GsonMapMgr();
	private int convertInt(String str){
		try {
			if (str==null || "".equals(str)){
				str="0";
			}
			return Integer.parseInt(str);
		} catch (Exception e) {
			
		}
		return 0;
	}
	public int totalRS=0;//全部查询记录
	/** 
    * 根据页码和分页大小获取上一次的最后一个scoredocs 
    * @param pageIndex 
    * @param pageSize 
    * @param query 
    * @param searcher 
    * @return 
    * @throws IOException 
    */  
   private ScoreDoc getLastScoreDoc(final int pageIndex,final int pageSize,final Query query,final IndexSearcher searcher,final Sort sort) throws IOException {  
       if(pageIndex==1)return null;//如果是第一页就返回空  
       int num = pageSize*(pageIndex-1);//获取上一页的最后数量  
       //Sort sort = new Sort();
       //TopDocs tds = searcher.search(query, num,sort);
    
       TopDocs tds = searcher.search(query, num,sort);
       
       this.totalRS=tds.totalHits;
       if(this.totalRS<num){
    	   return null;
       }
      // System.out.println(tds.totalHits);
       return tds.scoreDocs[num-1];  
   } 
   public void searchPageByAfter(final IKAnalyzer5x analyzer, final String query, final int pageIndex, final int pageSize, final String indexDir) {
	    try {  
	        Directory directory = FSDirectory.open( Paths.get( indexDir ) );
	    	IndexReader indexReader = DirectoryReader.open( directory );
	        IndexSearcher searcher =  new IndexSearcher( indexReader );
	        QueryParser parser = new QueryParser("contents", analyzer );  
	        Query q = parser.parse(query);  
	        
	        //获取上一页的最后一个元素  
	        ScoreDoc lastSd = getLastScoreDoc(pageIndex, pageSize, q, searcher,null);  
	        //通过最后一个元素去搜索下一页的元素  
	        TopDocs tds = searcher.searchAfter(lastSd,q, pageSize);  
	       
	        for(ScoreDoc sd:tds.scoreDocs) {  
	            Document doc = searcher.doc(sd.doc);
	           // doc.getFields()
	            System.out.println(sd.doc+":"+doc.get("id")+"-->"+doc.get("name"));  
	        }  
	          
	    }  catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	} 
   /**
    * 多条件查询
    * @param List<String> listRsField=需要返回的结果字段列表
    * */
   public List<Map<String,Object>> getSearchRes(List<String> listRsField,String indexDir,String query,int pageIndex,int pageSize){
	   List<Map<String,Object>> listRtn=new ArrayList<Map<String,Object>>();
	   Directory 		directory	=null;
	   IndexReader 		indexReader	=null;
	   IndexSearcher 	searcher	=null;
	   IKAnalyzer5x 	analyzer 	=null;
	   try {
		   	directory = FSDirectory.open( Paths.get( indexDir ) );
	    	indexReader = DirectoryReader.open( directory );
	        searcher =  new IndexSearcher( indexReader );
	        analyzer = new IKAnalyzer5x();
	        
	      //构建两个SortField
	        Sort sort = new Sort();
	        
	        //按照publishdate排序，降序
	        SortField sf1 = new SortField("publishdate", SortField.Type.STRING, true);
	        //按bookNumber排序，升序
	        SortField sf2 = new SortField("bookname", SortField.Type.DOC, false);

	        //先按照publishdate排，在按照bookNumber排
	        sort.setSort(new SortField[]{sf1, sf2});
	      //先按照publishdate排，在按照bookNumber排
	        sort.setSort(new SortField[]{sf1, sf2});
	        //要查找的字符串数组
	        String [] stringQuery={"可以","标题1"};
	        //待查找字符串对应的字段
	        String[] fields={"content","title"};
	        //Occur.MUST表示对应字段必须有查询值， Occur.MUST_NOT 表示对应字段必须没有查询值，Occur.SHOULD表示对应字段应该存在查询值（但不是必须）
	        Occur[] occ={Occur.SHOULD,Occur.SHOULD};
	        Query query1 = MultiFieldQueryParser.parse(stringQuery, fields, occ, analyzer);
	        /*
	        lucene中BooleanQuery 实现与或的复合搜索 .
	        BooleanClause用于表示布尔查询子句关系的类，包 括：BooleanClause.Occur.MUST，BooleanClause.Occur.MUST_NOT，BooleanClause.Occur.SHOULD。
	         	必须包含,不能包含,可以包含三种.有以下6种组合： 
	         
	        1．MUST和MUST：取得连个查询子句的交集。 
	        2．MUST和MUST_NOT：表示查询结果中不能包含MUST_NOT所对应得查询子句的检索结果。 
	        3．SHOULD与MUST_NOT：连用时，功能同MUST和MUST_NOT。
	        4．SHOULD与MUST连用时，结果为MUST子句的检索结果,但是SHOULD可影响排序。
	        5．SHOULD与SHOULD：表示“或”关系，最终检索结果为所有检索子句的并集。
	        6．MUST_NOT和MUST_NOT：无意义，检索无结果。
	        */
	        
	        //组合条件：
	        //年龄(或)：10、20、30、40
	        //名字(与): 四
	        //城市(非): 莆田
	        TermQuery ageQuery10=new TermQuery(new Term("ages", "10"));
	        TermQuery ageQuery20=new TermQuery(new Term("ages", "20"));
	        TermQuery ageQuery30=new TermQuery(new Term("ages", "30"));
	        TermQuery ageQuery40=new TermQuery(new Term("ages", "40"));
	        
	        TermQuery nameQuery=new TermQuery(new Term("name", "四"));
	        
	        TermQuery cityQuery=new TermQuery(new Term("city", "莆田"));
	        
	       
	        
	        
	        QueryParser parser = new QueryParser("contents", analyzer );  
	        
	        Query q = parser.parse(query);  
	        
	        //获取上一页的最后一个元素  
	        ScoreDoc lastSd = getLastScoreDoc(pageIndex, pageSize, q, searcher,null);  
	        //通过最后一个元素去搜索下一页的元素  
	        TopDocs tds = searcher.searchAfter(lastSd,q, pageSize);  
	        //==结果集合处理
	        for(ScoreDoc sd:tds.scoreDocs) {  
	            Document doc = searcher.doc(sd.doc);
	            for(int i=0;i<listRsField.size();i++){
	            	String fieldName=listRsField.get(i);
	            	Map<String,Object> mapData=new ConcurrentHashMap<String, Object>();
	            	String val=doc.get(fieldName);
	            	if(val==null)val="";
	            	mapData.put(fieldName, val);
	            	listRtn.add(mapData);
	            }
	          
	        }  
	          
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			try {
				if(analyzer!=null){
					analyzer.close();
					analyzer=null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				if(indexReader!=null){
					indexReader.close();
					indexReader=null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				if(directory!=null){
					directory.close();
					directory=null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
				
		}
	   
	   return listRtn;
   }
   /**
    * 设置查询字段与条件
    * */
   public void doSetTerm(List<Map<String,Object>> listTerm,IKAnalyzer5x analyzer ){
	   try {
		 for(int i=0;i<listTerm.size();i++){
			 Map<String,Object> mapData=listTerm.get(i);
			 String fieldname	=this.pGsonMapMgr.getString(mapData, "fieldname");//条件字段名称
			 String fieldvalue	=this.pGsonMapMgr.getString(mapData, "fieldvalue");//条件字段对应的数据
			 
			 Term t1 = new Term("title", "张");//全匹配方式（单字分次方式）
		        TermQuery q1 = new TermQuery(t1);
		        Term t2 = new Term("body", "打");
		        TermQuery q2 = new TermQuery(t2);

		      
		        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

		        booleanQuery.add(new TermQuery(new Term("name", "iphone")), Occur.MUST);

		        booleanQuery.add(new TermQuery(new Term("description", "grey")), Occur.MUST);
		        //拆词方式
		        QueryParser parser = new QueryParser( "fileName", analyzer );
		        Query query1 = parser.parse( "" );
		        // contents必须含有条件一
		        booleanQuery.add( query1, Occur.MUST );

		        Query query = booleanQuery.build();
		 }
	} catch (Exception e) {
		// TODO: handle exception
	}
   }
   /**
    * 查询 分词器进行智能切分
    * */
   public List<Map<String,Object>> getQueryList_useSmart(final List<String> listRsField,final List<Map<String,Object>> listTerm,final  Sort sort,final String indexDir,final int pageIndex,final int pageSize){
	   return this.getQueryList(listRsField, listTerm, sort, indexDir, pageIndex, pageSize,true);
   }
   /**
    * 查询()
    * */
   public List<Map<String,Object>> getQueryList(final List<String> listRsField,final List<Map<String,Object>> listTerm,final  Sort sort,final String indexDir,final int pageIndex,final int pageSize){
	   return this.getQueryList(listRsField, listTerm, sort, indexDir, pageIndex, pageSize,false);
   }
   /**
    * 查询
    * */
   private List<Map<String,Object>> getQueryList(final List<String> listRsField,final List<Map<String,Object>> listTerm,final  Sort sort,final String indexDir,final int pageIndex,final int pageSize,boolean useSmart){
	   List<Map<String,Object>> listRtn=new ArrayList<Map<String,Object>>();
	   Directory 		directory	=null;
	   IndexReader 		indexReader	=null;
	   IndexSearcher 	searcher	=null;
	   IKAnalyzer5x 	analyzer 	=null;
	   try {
		   	directory = FSDirectory.open( Paths.get( indexDir ) );
	    	indexReader = DirectoryReader.open( directory );
	        searcher =  new IndexSearcher( indexReader );
	        analyzer = new IKAnalyzer5x(useSmart);
	        // 多条件必备神器
	        BooleanQuery.Builder builder = new BooleanQuery.Builder();
	        
	        //条件一
	        final QueryParser parser = new QueryParser( "yw_type", analyzer );
	        Query query = parser.parse( this.yw_type );
	        builder.add( query, Occur.MUST );
	        // 条件二
	        for(int i=0;i<listTerm.size();i++){
	        	Map<String,Object> mapData=listTerm.get(i);
	        	String field_id=this.pGsonMapMgr.getString(mapData, "field_id");
	        	String field_val=this.pGsonMapMgr.getString(mapData, "field_val");
	        	String or_term=this.pGsonMapMgr.getString(mapData, "or_term");
	        	if("".equals(or_term)){
	        		this.doSetBooleanQuery(builder, analyzer, field_id, field_val);
	        	}else{
	        		if("NumericRangeQuery".equalsIgnoreCase(or_term)){
	        			String n1=(this.pGsonMapMgr.getString(mapData, "field_va1l"));
	        			String n2=(this.pGsonMapMgr.getString(mapData, "field_va12"));
	        			this.doSetNumericRangeQueryByInt(builder, analyzer, field_id, n1, n2);
	        		}else if("NotRangeQuery".equalsIgnoreCase(or_term)){
	        			String n1=(this.pGsonMapMgr.getString(mapData, "field_va1l"));
	        			String n2=(this.pGsonMapMgr.getString(mapData, "field_va12"));
	        			this.doSetNotRangeQuery(builder, analyzer, field_id, n1, n2);
	        		}else if("Fuzzy".equalsIgnoreCase(or_term)){
	        			this.doSetFuzzyQueryQuery(builder, field_id, field_val);//模糊查询
	        		}else if("TermQuery".equalsIgnoreCase(or_term)){
	        			this.doSetTermQuery(builder, field_id, field_val);
	        		}else if("INTRangeQuery".equalsIgnoreCase(or_term)){
	        			int n1=this.convertInt((this.pGsonMapMgr.getString(mapData, "field_va1l")));
	        			int n2=this.convertInt((this.pGsonMapMgr.getString(mapData, "field_va12")));
	        			this.doSetIntRangeQuery(builder, analyzer, field_id, n1, n2);
	        		}else if("useSmart".equalsIgnoreCase(or_term)){
	        			this.doSetBooleanQueryuseSmart(builder, field_id, field_val);
	        		}else{
	        			this.doSetBooleanQueryOR(builder, analyzer, field_id, field_val);
	        		}
	        	}
	        }
	       // Query query2 =NumericRangeQuery.newIntRange("is_adm_pub", 0, 0, true,true);  
	        //TermRangeQuery query2=new TermRangeQuery("is_adm_pub", new BytesRef("0".getBytes()), new BytesRef("0".getBytes()), true, true); 
	       // builder.add( query2, Occur.MUST );
	        //==范围查找
	        //TermRangeQuery query2=new TermRangeQuery("create_time", new BytesRef("2017-03-21 9:44:27".getBytes()), new BytesRef("2017-04-27 9:44:27".getBytes()), true, true); 
	        //builder.add( query2, Occur.MUST );
	        //==范围查找
	        //==IN命令=begin
	       // String queryString = "(res_cat_id:zx0001) (res_cat_id:003)"; 
	       // QueryParser parser1 = new QueryParser( "res_cat_id", analyzer );
		    //   Query query1 = parser1.parse( queryString );
		    //   builder.add( query1, Occur.MUST );
	        //===IN命令==end 
	       // sort=null;
	        //SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
            //Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(builder.build()));
	        //获取上一页的最后一个元素 
	        ScoreDoc lastSd = getLastScoreDoc(pageIndex, pageSize, builder.build(), searcher,sort);
	        if(lastSd==null){
	        	if(pageIndex>1){
	        		return listRtn;
	        	}
	        }
	        //通过最后一个元素去搜索下一页的元素  
	        TopDocs tds;
	        if(sort==null){
	        	tds = searcher.searchAfter(lastSd,builder.build(), pageSize);
	        }else{
	        	tds = searcher.searchAfter(lastSd,builder.build(), pageSize,sort);
	        }
	        this.totalRS=tds.totalHits;
	        //==结果集合处理
	        for(ScoreDoc sd:tds.scoreDocs) {  
	        	final Document doc = searcher.doc(sd.doc);
	        	final  Map<String,Object> mapData=new ConcurrentHashMap<String, Object>();
	            for(int i=0;i<listRsField.size();i++){
	            	final String fieldName=listRsField.get(i);
	            	if("*".equals(fieldName)){
	            		List<IndexableField> IndexableFields=doc.getFields();
	            		if(IndexableFields!=null){
		            		for(int k=0;k<IndexableFields.size();k++){
		            			String myName=IndexableFields.get(k).name();
		            			if(myName==null)continue;
		            			if("".equals(myName))continue;
		            			String val=doc.get(myName);
		            			if(val==null)val="";
		            			mapData.put(myName, val);
		            			
		            		}
		            		// listRtn.add(mapData);
	            		}
	            		break;
	            		
	            	}
	            	String val=doc.get(fieldName);
	            	//System.out.println(fieldName+"==="+val);
	            	if(val==null)val="";
	            	if(!"".equals(val)){
		            	//TokenStream tokenStream = analyzer.tokenStream(fieldName, new StringReader(doc.get(fieldName)));
		                //String valHighlighter = highlighter.getBestFragment(tokenStream, doc.get(fieldName));
		                //if(valHighlighter!=null){
		                //	val=valHighlighter;
		                //}
	            	}
	            	mapData.put(fieldName, val);
	            	
	            }
	            listRtn.add(mapData);
	        }  
	          
		} catch (Exception e) {
			System.out.println(e.toString());
		}finally{
			try {
				if(analyzer!=null){
					analyzer.close();
					analyzer=null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				if(indexReader!=null){
					indexReader.close();
					indexReader=null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				if(directory!=null){
					directory.close();
					directory=null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
				
		}
	   
	   return listRtn;
   }
   /**
    * 组合条件
    * */
   private void doSetBooleanQuery(final BooleanQuery.Builder builder,final IKAnalyzer5x analyzer,final String key,final String val){
	   // 条件二
	   try {
		   QueryParser parser1 = new QueryParser( key, analyzer );
	       Query query1 = parser1.parse( val );
	       builder.add( query1, Occur.MUST );
		} catch (Exception e) {
			// TODO: handle exception
		}
      
   }
   private void doSetBooleanQueryuseSmart(final BooleanQuery.Builder builder,final String key,final String val){
	   // 条件二
	   IKAnalyzer5x 	analyzer=null;
	   try {
		   	analyzer =new IKAnalyzer5x(true);
		   QueryParser parser1 = new QueryParser( key, analyzer );
	       Query query1 = parser1.parse( val );
	       builder.add( query1, Occur.MUST );
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			try {
				if(analyzer!=null){
					analyzer.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
      
   }
   private void doSetBooleanQueryOR(BooleanQuery.Builder builder,IKAnalyzer5x analyzer,String key,String val){
	   // 条件二
	   try {
		   QueryParser parser1 = new QueryParser( key, analyzer );
	       Query query1 = parser1.parse( val );
	       builder.add( query1, Occur.SHOULD );
	       
		} catch (Exception e) {
			// TODO: handle exception
		}
      
   }
   private void doSetTermQuery(final BooleanQuery.Builder builder,final String field,final String name){
	   // 精准条件
	   try {
		   Query query1 =new TermQuery(new Term(field,name));

	       builder.add( query1, Occur.MUST );
		} catch (Exception e) {
			// TODO: handle exception
		}
      
   }
   /**
    * 模糊查询
    * */
   private void doSetFuzzyQueryQuery(final BooleanQuery.Builder builder,final String field, String name){
	   // 精准条件
	   IKAnalyzer5x 	analyzer =null;
	   try {
		   	analyzer =new IKAnalyzer5x(true);
		   name=name.trim();
		   QueryParser parser1 = new QueryParser( field, analyzer );
		   parser1.setAllowLeadingWildcard(true);
	       Query query1 = parser1.parse("*"+name+"*" );
	       
	       builder.add( query1, Occur.MUST);
		   
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			try {
				if(analyzer!=null){
					analyzer.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
      
   }
   /**
    * 数字范围的查找
    * */
   private void doSetNumericRangeQueryByInt(BooleanQuery.Builder builder,IKAnalyzer5x analyzer,String key,String v1,String v2){
	   // 条件二
	   try {
		   //Query query2 =NumericRangeQuery.newIntRange(key, 0, 0, true,true);
		   Query query2 = new TermRangeQuery(key, new BytesRef(v1.getBytes()), new BytesRef(v2.getBytes()), true, true); 
	        //TermRangeQuery query2=new TermRangeQuery("is_adm_pub", new BytesRef("0".getBytes()), new BytesRef("0".getBytes()), true, true); 
	        builder.add( query2, Occur.MUST );
	       
		} catch (Exception e) {
			// TODO: handle exception
		}
      
   }
   /**
    * 不包含的条件
    * */
   private void doSetNotRangeQuery(BooleanQuery.Builder builder,IKAnalyzer5x analyzer,String key,String v1,String v2){
	   // 条件二
	   try {
		   //Query query2 =NumericRangeQuery.newIntRange(key, 0, 0, true,true);
		   Query query2 = new TermRangeQuery(key, new BytesRef(v1.getBytes()), new BytesRef(v2.getBytes()), true, true); 
	        //TermRangeQuery query2=new TermRangeQuery("is_adm_pub", new BytesRef("0".getBytes()), new BytesRef("0".getBytes()), true, true); 
	        builder.add( query2, Occur.MUST_NOT );
	       
		} catch (Exception e) {
			// TODO: handle exception
		}
      
   }
   private void doSetIntRangeQuery(BooleanQuery.Builder builder,IKAnalyzer5x analyzer,String key,int v1,int v2){
	   // 条件二
	   try {
		   Query query2 =NumericRangeQuery.newIntRange(key, v1, v2, true,true);
		  // Query query2 = new TermRangeQuery(key, new BytesRef(v1.getBytes()), new BytesRef(v2.getBytes()), true, true); 
	        //TermRangeQuery query2=new TermRangeQuery("is_adm_pub", new BytesRef("0".getBytes()), new BytesRef("0".getBytes()), true, true); 
	        builder.add( query2, Occur.MUST );
	       
		} catch (Exception e) {
			// TODO: handle exception
		}
      
   }
   /**
    * IN 命令
    * */
   private void doSetInTerm(BooleanQuery.Builder builder,IKAnalyzer5x analyzer,String key,String v1,String v2){
	   try {
		   //==IN命令=begin
	        String queryString = "(res_cat_id:zx0001) (res_cat_id:003)"; 
	        QueryParser parser1 = new QueryParser( "res_cat_id", analyzer );
		      Query query1 = parser1.parse( queryString );
		      builder.add( query1, Occur.SHOULD );
	        //===IN命令==end 
	} catch (Exception e) {
		// TODO: handle exception
	}
   }
   /**
    * 根据主键得到信息
    * */
   public  Map<String,Object> getMap_byId(final  String indexDir,final  String idVal,final List<Map<String,Object>> listFields ){
	   Map<String,Object> mapRtn=new ConcurrentHashMap<String, Object>();
	   Directory 		directory	=null;
	   IndexReader 		indexReader	=null;
	   IndexSearcher 	searcher	=null;
	   IKAnalyzer5x 	analyzer 	=null;
	   try {
		   if("".equals(idVal))return mapRtn;
		   	directory = FSDirectory.open( Paths.get( indexDir ) );
	    	indexReader = DirectoryReader.open( directory );
	        searcher =  new IndexSearcher( indexReader );
	        analyzer = new IKAnalyzer5x();
	        // 多条件必备神器
	        BooleanQuery.Builder builder = new BooleanQuery.Builder();
	        
	        //条件一
	        final  QueryParser parser = new QueryParser( "id", analyzer );
	        Query query = parser.parse( idVal );
	        builder.add( query, Occur.MUST );
	        //获取上一页的最后一个元素  
	        final ScoreDoc lastSd = getLastScoreDoc(1, 1, builder.build(), searcher,null);  
	        //通过最后一个元素去搜索下一页的元素  
	        TopDocs tds = searcher.searchAfter(lastSd,builder.build(), 1);  
	        //==结果集合处理
	        for(ScoreDoc sd:tds.scoreDocs) {  
	            Document doc = searcher.doc(sd.doc);
	            for(int i=0;i<listFields.size();i++){
	            	Map<String,Object> mapData=listFields.get(i);
	            	String field_id=this.pGsonMapMgr.getString(mapData, "field_id");
	            	
	            	String val=doc.get(field_id);
	            	if(val==null)val="";
	            	mapRtn.put(field_id, val);
	            }
	        }  
	          
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			try {
				if(analyzer!=null){
					analyzer.close();
					analyzer=null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				if(indexReader!=null){
					indexReader.close();
					indexReader=null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				if(directory!=null){
					directory.close();
					directory=null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
				
		}
		return mapRtn;
    
   }
   public Map<String,Object> createmap(String field_id,String field_val){
		Map<String,Object> map=new ConcurrentHashMap<String, Object>();
		map.put("field_id", field_id);
		map.put("field_val", field_val);
		return map;
	}
   public Map<String,Object> createmapUseSmart(String field_id,String field_val){
		Map<String,Object> map=new ConcurrentHashMap<String, Object>();
		map.put("field_id", field_id);
		map.put("field_val", field_val);
		map.put("or_term", "useSmart");
		return map;
	}
	public Map<String,Object> createmapOr(String field_id,String field_val){
		Map<String,Object> map=new ConcurrentHashMap<String, Object>();
		map.put("field_id", field_id);
		map.put("field_val", field_val);
		map.put("or_term", "or");
		return map;
	}
	public Map<String,Object> createmapNumericRangeQuery(String field_id,String field_val1,String field_val2){
		Map<String,Object> map=new ConcurrentHashMap<String, Object>();
		map.put("field_id", field_id);
		map.put("field_va1l", field_val1);
		map.put("field_va12", field_val2);
		map.put("or_term", "NumericRangeQuery");
		return map;
	}
	public Map<String,Object> createmapINTRangeQuery(String field_id,String field_val1,String field_val2){
		Map<String,Object> map=new ConcurrentHashMap<String, Object>();
		map.put("field_id", field_id);
		map.put("field_va1l", field_val1);
		map.put("field_va12", field_val2);
		map.put("or_term", "INTRangeQuery");
		return map;
	}
	/**
	 * 模糊查询
	 * */
	public Map<String,Object> createmapFuzzy(String field_id,String field_val){
		Map<String,Object> map=new ConcurrentHashMap<String, Object>();
		map.put("field_id", field_id);
		map.put("field_val", field_val);
		map.put("or_term", "Fuzzy");
		return map;
	}
	/**
	 * 精准条件
	 * */
	public Map<String,Object> createTermQuery(String field_id,String field_val){
		Map<String,Object> map=new ConcurrentHashMap<String, Object>();
		map.put("field_id", field_id);
		map.put("field_val", field_val);
		map.put("or_term", "TermQuery");
		return map;
	}
	public Map<String,Object> createmapNotRangeQuery(String field_id,String field_val1,String field_val2){
		Map<String,Object> map=new ConcurrentHashMap<String, Object>();
		map.put("field_id", field_id);
		map.put("field_va1l", field_val1);
		map.put("field_va12", field_val2);
		map.put("or_term", "NotRangeQuery");
		return map;
	}
	  public String getStrAnalysis(String keyWord){
		   String strRtn="";
		  
		   IKAnalyzer5x analyzer =null;
		   TokenStream tokenStream=null;
		  try {
			  if(keyWord.length()<=3)return keyWord;
			  analyzer = new IKAnalyzer5x(true);
			  tokenStream = analyzer.tokenStream("content",  
			          new StringReader(keyWord));
			  
			  tokenStream.reset();
			  tokenStream.addAttribute(CharTermAttribute.class);  
			  Map<String,String> mapTemp=new ConcurrentHashMap<String, String>();
			  while (tokenStream.incrementToken()) {  
			      CharTermAttribute charTermAttribute = tokenStream  
			              .getAttribute(CharTermAttribute.class);  
			      String str=charTermAttribute.toString();
			      if(mapTemp.get(charTermAttribute.toString())==null){
			    	  if(str.length()==1)continue;
			    	  mapTemp.put(str, str);
			    	  strRtn+=str+" ";
			      }
			    //  System.out.println(charTermAttribute.toString());  
			
			  } 
			  tokenStream.end();
			  strRtn=strRtn.trim();
			
			  
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(analyzer!=null){
				analyzer.close();
			}
			if(tokenStream!=null){
				try {
					tokenStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return strRtn;
		
	}  
	  public String[] getStringsByAnalysisResult(String keyWord,boolean useSmart)throws Exception {
		  String[] strs=null;
		   String strRtn="";
		   IKAnalyzer5x analyzer =null;
		   TokenStream tokenStream=null;
		  try {
			  analyzer = new IKAnalyzer5x(useSmart);
			  tokenStream = analyzer.tokenStream("content",  
			          new StringReader(keyWord));
			  
			  tokenStream.reset();
			  tokenStream.addAttribute(CharTermAttribute.class);  
			  while (tokenStream.incrementToken()) {  
			      CharTermAttribute charTermAttribute = tokenStream  
			              .getAttribute(CharTermAttribute.class);  
			      strRtn+=charTermAttribute.toString()+" ";
			    //  System.out.println(charTermAttribute.toString());  
			
			  } 
			  tokenStream.end();
			  strs=strRtn.split(" ");
			  
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(analyzer!=null){
				analyzer.close();
			}
			if(tokenStream!=null){
				tokenStream.close();
			}
		}
		
		return strs;
		
	}  
}
