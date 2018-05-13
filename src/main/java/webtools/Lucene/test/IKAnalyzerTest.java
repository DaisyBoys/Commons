package webtools.Lucene.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.StringReader;

public class IKAnalyzerTest {
    public static void main(String[] args) {
        String keyWord = "IKAnalyzer的分词效果到底怎么样呢，我们来看一下吧,京华时报２００９年1月23日报道 昨天，受一股来自中西伯利亚的强冷空气影响，本市出现大风降温天气，白天最高气温只有零下7摄氏度，同时伴有6到7级的偏北风。";
        keyWord += "２００９年ゥスぁま是中 ＡＢｃｃ国абвгαβγδ首次,我的ⅠⅡⅢ在chenёlbēū全国ㄦ范围ㄚㄞㄢ内①ē②㈠㈩⒈⒑发行地方政府债券，";
        //创建IKAnalyzer中文分词对象  
        IKAnalyzer analyzer = new IKAnalyzer();
        // 使用智能分词  
        analyzer.setUseSmart(true);

        // 打印分词结果  
        try {
            printAnalysisResult(analyzer, keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印出给定分词器的分词结果
     *
     * @param analyzer 分词器
     * @param keyWord  关键词
     * @throws Exception
     */
    private static void printAnalysisResult(Analyzer analyzer, String keyWord)
            throws Exception {
        System.out.println("[" + keyWord + "]分词效果如下");
        TokenStream tokenStream = analyzer.tokenStream("content",
                new StringReader(keyWord));
        tokenStream.reset();
        tokenStream.addAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()) {
            CharTermAttribute charTermAttribute = tokenStream
                    .getAttribute(CharTermAttribute.class);
            System.out.println(charTermAttribute.toString());

        }
        tokenStream.end();
        tokenStream.close();
    }
}  