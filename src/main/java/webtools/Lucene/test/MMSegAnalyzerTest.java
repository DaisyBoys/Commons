package webtools.Lucene.test;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
import com.chenlb.mmseg4j.analysis.MaxWordAnalyzer;
import com.chenlb.mmseg4j.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;

/**
 * MMSegAnalyzer分词器测试
 *
 * @author Lanxiaowei
 */
public class MMSegAnalyzerTest {

    String txt = "";


    public void before() throws Exception {
        txt = "京华时报２００９年1月23日报道 昨天，受一股来自中西伯利亚的强冷空气影响，本市出现大风降温天气，白天最高气温只有零下7摄氏度，同时伴有6到7级的偏北风。";
        txt = "２００９年ゥスぁま是中 ＡＢｃｃ国абвгαβγδ首次,我的ⅠⅡⅢ在chenёlbēū全国ㄦ范围ㄚㄞㄢ内①ē②㈠㈩⒈⒑发行地方政府债券，";
        txt = "大S小3U盘浙BU盘T恤T台A股牛B";
    }


    //@Ignore  
    public void testSimple() throws IOException {
        Analyzer analyzer = new SimpleAnalyzer();
        displayTokens(analyzer, txt);
    }

    public void testComplex() throws IOException {
        //txt = "1999年12345日报道了一条新闻,2000年中法国足球比赛";  
        /*txt = "第一卷 云天落日圆 第一节 偷欢不成倒大霉"; 
        txt = "中国人民银行"; 
        txt = "我们"; 
        txt = "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作";*/
        //ComplexSeg.setShowChunk(true);  
        Analyzer analyzer = new ComplexAnalyzer();
        displayTokens(analyzer, txt);
    }


    public void testMaxWord() throws IOException {
        //txt = "1999年12345日报道了一条新闻,2000年中法国足球比赛";  
        //txt = "第一卷 云天落日圆 第一节 偷欢不成倒大霉";  
        //txt = "中国人民银行";  
        //txt = "下一个 为什么";  
        //txt = "我们家门前的大水沟很难过";  
        //ComplexSeg.setShowChunk(true);  
        Analyzer analyzer = new MaxWordAnalyzer();
        displayTokens(analyzer, txt);
    }  
  
    /*@Test 
    public void testCutLeeterDigitFilter() { 
        String myTxt = "mb991ch cq40-519tx mmseg4j "; 
        List<String> words = toWords(myTxt, new MMSegAnalyzer("") { 
 
            @Override 
            protected TokenStreamComponents createComponents(String text) { 
                Reader reader = new BufferedReader(new StringReader(text)); 
                Tokenizer t = new MMSegTokenizer(newSeg(), reader); 
                return new TokenStreamComponents(t, new CutLetterDigitFilter(t)); 
            } 
 
             
        }); 
 
        //Assert.assertArrayEquals("CutLeeterDigitFilter fail", words.toArray(new String[words.size()]), "mb 991 ch cq 40 519 tx mmseg 4 j".split(" ")); 
        for(String word : words) { 
            System.out.println(word); 
        } 
    }*/

    public static void displayTokens(Analyzer analyzer, String text) throws IOException {
        TokenStream tokenStream = analyzer.tokenStream("text", text);
        displayTokens(tokenStream);
    }

    public static void displayTokens(TokenStream tokenStream) throws IOException {
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = tokenStream.addAttribute(PositionIncrementAttribute.class);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        TypeAttribute typeAttribute = tokenStream.addAttribute(TypeAttribute.class);

        tokenStream.reset();
        int position = 0;
        while (tokenStream.incrementToken()) {
            int increment = positionIncrementAttribute.getPositionIncrement();
            if (increment > 0) {
                position = position + increment;
                System.out.print(position + ":");
            }
            int startOffset = offsetAttribute.startOffset();
            int endOffset = offsetAttribute.endOffset();
            String term = charTermAttribute.toString();
            System.out.println("[" + term + "]" + ":(" + startOffset + "-->" + endOffset + "):" + typeAttribute.type());
        }
    }

    /**
     * 断言分词结果
     *
     * @param analyzer
     * @param text      源字符串
     * @param expecteds 期望分词后结果
     * @throws IOException
     */
    public static void assertAnalyzerTo(Analyzer analyzer, String text, String[] expecteds) throws IOException {
        TokenStream tokenStream = analyzer.tokenStream("text", text);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        for (String expected : expecteds) {
            // Assert.assertTrue(tokenStream.incrementToken());
            // Assert.assertEquals(expected, charTermAttribute.toString());
        }
        //Assert.assertFalse(tokenStream.incrementToken());  
        tokenStream.close();
    }
}  