package commons.baidu.translate;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 百度翻译工具类
 */
public class Baidu {
    private static Logger LOG = Logger.getLogger(Baidu.class);

    public static Properties configProperties;
    public static int wordcount = 10;

    private static void init() {
        String configFilePath = "/baidu_trans.properties";
        InputStream inputStream = null;
        try {
            inputStream = Baidu.class.getResourceAsStream(configFilePath);
            if (null == inputStream) {
                throw new IOException("读取百度翻译配置文件失败");
            }
            configProperties = new Properties();
            configProperties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    //private static final String TRANS_API_HOST = "http://fanyi-api.baidu.com/api/trans/vip/translate";
    //private static final String APP_ID = "20170807000071070";
    //private static final String SECURITY_KEY = "yTthDvT5YBm2mSii3c9i";

    public static String translate_html(String html, String srclanguage, String deslanguage) {
        init();
        TransApi api = new TransApi(configProperties.getProperty("baidu.trans.app_id"), configProperties.getProperty("baidu.trans.security_key"));
        html = html.replaceAll(">\\s+<", "><");
        Document doc = Jsoup.parse(html);
        Elements ele = doc.getAllElements();
        for (Element e : ele) {
            if (!e.textNodes().isEmpty() && !StringUtils.isEmpty(e.text())) {
                String res = getTransResult(api, e.text(), srclanguage, deslanguage);
                /*JSONObject json = JSONObject.parseObject(res);
        		JSONArray array = json.getJSONArray("trans_result");
        		for (int i = 0; i < array.size(); i++) {
        		    JSONObject jo = array.getJSONObject(i);
					if(jo.containsKey("dst")) {
						e.text((String) jo.get("dst"));
					}
        		}*/
                e.text((String) res);
            }
        }
        Elements e = doc.getElementsByTag("body");
        return e.html();
    }

    public static String translate_text(String text, String srclanguage, String deslanguage) {
        init();
        TransApi api = new TransApi(configProperties.getProperty("baidu.trans.app_id"), configProperties.getProperty("baidu.trans.security_key"));
        StringBuffer texts = new StringBuffer();
        String res = getTransResult(api, text, srclanguage, deslanguage);
        return res.toString();
    }


    // 在字符串末尾查找符合条件分隔符位置等于-1说明未找到 0说明在第一位
    public static int getPosition(String inputString) {

        List<String> mylist = new ArrayList();

        Pattern p = Pattern.compile("[，。？：；?,]");

        String[] a = p.split(inputString);

        Matcher m = p.matcher(inputString);
        int pos = -1;
        while (m.find()) {
            pos = m.end();
        }

        return pos;
    }

    //按固定长度拆分字符串
    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    public static List<String> getStrList(String inputString, int length,
                                          int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }


    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }

    //重新加工拆分列表
    public static List<String> getResultList(String inputString) {
        List<String> list = new ArrayList<String>();
        List<Integer> initPos = new ArrayList();
        List<Integer> afterPos = new ArrayList();
        if (inputString.length() <= wordcount) {
            list.add(inputString);
            return list;
        } else {
            List<String> mylist = getStrList(inputString, wordcount);//按照初始固定大小拆分
            Integer pos = 0;
            for (String str1 : mylist) {
                pos = getPosition(str1);
                if (pos >= 0) {//在拆分后的字符串里找分隔符，找到最后一个分隔符，添加到initPos列表里
                    initPos.add(pos);
                } else//如果没找到,需要占位，所以添加为0
                {
                    initPos.add(0);
                }

            }
        }

        //循环位置列表，微调新的位置列表
        for (int i = 0; i < initPos.size(); i++) {
            if (initPos.get(i) != 0) {//分隔符不在第一位
                afterPos.add(wordcount * i + initPos.get(i));
            }

        }
        if (afterPos.size() == 0) {
            list.add(inputString);
        } else if (afterPos.size() == 1) {
            list.add(inputString);
        } else {
            for (int i = 0; i < afterPos.size(); i++) {
                if (i == 0) {
                    list.add(inputString.substring(0, afterPos.get(i)));

                } else {
                    list.add(inputString.substring(afterPos.get(i - 1), afterPos.get(i)));
                }
            }
            //最后一个位置到结尾，也需要处理
            if (afterPos.get(afterPos.size() - 1) != inputString.length()) {
                list.add(inputString.substring(afterPos.get(afterPos.size() - 1), inputString.length()));
            }


        }

        return list;
    }


    public static String getTransResult(TransApi api, String text, String srclanguage, String deslanguage) {
        String transResult1 = "";
        StringBuffer endResult = new StringBuffer();
        List<String> transList = getResultList(text);
        for (String mystr : transList) {
            transResult1 = api.getTransResult(configProperties.getProperty("baidu.trans.trans_api_host"), mystr, srclanguage, deslanguage);
            JSONObject json = JSONObject.parseObject(transResult1);
            JSONArray array = json.getJSONArray("trans_result");
            if (null != array) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject jo = array.getJSONObject(i);
                    if (jo.containsKey("dst")) {
                        endResult.append((String) jo.get("dst"));
                    }
                }
            } else {
                LOG.info("翻译结果：" + transResult1);
            }
        }
        return endResult.toString();
    }


    public static void main(String[] str) {
        String ss = "【libor欧元】隔夜libor报-0.424717天libor报-0.40643%,%。";
        //ss="\\n\\n 来源：中国驻澳大利亚使馆 2017/11/16\\n\\n 　　，中国国家旅游局在悉尼成功举办中澳旅游年系列活动之一“美丽中国之夜”。国家旅游局副局长李世宏、成竞业大使夫妇、顾小杰总领事、澳大利亚旅游局局长欧素文以及来自两国旅游界、航空公司和新闻媒体代表约300余人出席。 　　李世宏副局长在致辞中回顾了2017中澳旅游年启动以来双>方开展的一系列活动和取得的成果，指出1至8月中国访澳游客达95万人次，1至9月澳访华游客达54万人次，增幅均超过10%。欧素文代表澳贸易旅游投资部长乔博致辞称，每8名澳入境游客就有1人来自中国，中国游客占来澳境外游客消费总额的四分之一。 　　成大使积极评价旅游对促进中澳双边关系发挥的有益作用，强调不久前举行的中共十九大为>中澳旅游合作提供了新的前景。他表示乐见越来越多的中国民众来澳旅游，同时也欢迎更多的澳大利亚人前往美丽中国旅游。 \\n \\n\\n 查看原文 （机器人发稿测试中）";
        //ss="\\n\\n 来源：中国:驻澳大利亚使馆,中国国家旅游局在悉尼成功举办中澳旅游年系列活动之一,2017/11/16\\n\\n";
        //ss="\\n\\n 来源驻澳大利亚使馆来源驻澳大利亚使馆来源驻澳大利亚使馆";
        //ss="市场难以再出现盈利拉动或估值拉动的行情，关注景气度较好的科技与制造细分龙头，如二级行业中的半导体、电子设备、新能源设备、工程机械、航运港口、建筑装修、发电及电网等。\\r\\n    本期话题：哪些细分领域景气向上？今年市场的大蓝筹股风格，本质是盈利驱动。全A股（非金融）市值前3%公司贡献全市场46%利润，今年平均涨幅超50%。另外，对指数涨幅进行盈利和估值拆分，2017年以来上证综指的涨幅全部由盈利改善所贡献。那么，推动企业盈利改善的因素后续怎么变化？有哪些细分领域景气是向上的？\\r\\n    首先，A股ROE提升主要来自销售净利率提升；净利率的提升主要来自期间费用率的改善。全A（非金融）营收增速下滑（-0.1pcts），净利增速却大幅提升（+1.8pcts），原因在于盈利能力上升。盈利提升主要来自销售净利率提升（由5.48%升到6.06%），资产周转率贡献次之（由0.43%升到0.47%）；净利率提升主要来自期间费用率改善。消费行业销售费用率降幅大、周期行业财务费用率降幅大、管理费用率普降。\\r\\n    其次，怎么看盈利增速的趋势以及盈利质量？明年盈利增速大概率下滑，但盈利质量的持续改善具有不确定性。宏观层面，增速回升的驱动因素在于：供给侧改革和环保限产带来的价格上涨；地产投资高增速带动上下游；出口复苏；消费升级带动高端需求上升。往后看，随供需格局趋稳，PPI将进入下行通道；地产投资在10月份已开始走弱；出口有较大不确定性且贡献相对有限；而消费升级是长期趋势，10月社消零售下滑，也受宏观周期因素影响。因此，经济周期向下，A股增速明年大概率将下滑，且企业盈利质量的持续改善具有不确定性。\\r\\n    最后，目前市场较难出现盈利拉动或估值拉动的整体大幅上涨，关注景气度较好的细分领域机会。经济周期向下，由盈利拉动的整体上涨较难持续。而目前沪深300估值14.4倍，历史55%分位，上证50估值11.9倍，历史49%分位，基本合理，市场难有拉估值带动的整体上涨。从景气度与相对估值分析，可看出：一是部分成长行业增速较高（连续两季度增速回升），如半导体、电子设备、新能源设备等；二是部分中游制造增速回升且有持续性，如工程机械、航运港口、建筑装修、发电及电网等；三是消费股PEG大多超过1，估值优势渐消弱，相反，成长估值优势渐突出。因此，建议关注新兴科技与中游制造细分龙头。\\r\\n    市场判断：A股逐渐防范风险，配置前期滞涨且估值合理的石油石化、以及大金融等防御属性的板块。经济周期下行，且市场估值不便宜，目前较难出现盈利拉动或估值拉动的整体大幅上涨。对于A股市场，我们延续10月最后一周以及11月月报的观点，认为是收获的季节，逐渐防范风险，行业配置上，重点关注石油石化以及大金融等板块。\\r\\n    行业配置：维持组合不变，11月调出计算机、传媒、环保，调入石油石化、医药，最终配置为：石油石化(20%)、医药(10%)，电子(10%)、餐饮旅游(10%)、通信(20%)、证券(20%)，农业(10%)。个股见正文。";
        //ss="报告摘要：\\r\\n    布林带择时观点：调整不改上升趋势。本周沪深300指数收盘价为4104.20，下跌0.40%，本周大盘蓝筹继续护盘，全周呈现倒“V”型，微微收跌，沪深300的周线上涨趋势已经形成，自6月以来指数一直沿着布林上轨1上行，沪深300指数已进入超涨模式，从过去一年半的历史来看，向上超越布林上轨2的之后都会有一段时期的回调，在这里我们也提示沪深300指数短期回调风险。因此，短期我们震荡看空，中期仍然具备上升潜力。本周中证500收盘价为6278.03点，下跌1.32%，本周成交量4791亿，较上周5641亿小幅小跌，中证500处于震荡行情，目前正处于下跌行情之中，从短期趋势而言，下穿20周均线尚未找到支撑，中期可以静待底部信号出现。因此，短期给予继续震荡看空判断，继续磨底。本周创业板指收盘价为1782.66点，下跌2.79%，成交量3332亿，较上周4157亿成交量略有下降，创业板目前仍处于下跌趋势之中，本周下穿20周均线继续下探，短期宜规避风险。总体来看我们认为创业板仍然维持在1689~1961点之间震荡的判断。因此，给予创业板短期震荡看空判断。\\r\\n    市值风格配置：大盘相对抗跌，小盘继续磨底。本周市场继续分化，大盘指数上涨0.29%，最小市值下跌2.21%。如果我们仔细观察中证1800-指数的走势，我们会发现在今年6月初和7月中旬的两次小市值反弹都是跌到20%的位置，小市值的第三次“黄金坑”可能已经出现。从风格上，我们认为趋向平衡，一方面从保收益角度配置大盘抗风险，另一方面重视对小市值的布局，适当配置小盘布局成长。建议配置50%大盘，25%中盘，25%小盘。\\r\\n    新股：本周五证监会核发了5家IPO批文，和上周持平，募集资金不超过32亿。11月开板的新股开板收益率平均为292%，较10月252%上升40个百分点；11月连续涨停板数平均为9.8，较10月9.3上升5.4%。截至2017年11月24日，2017年证监会一共核发了381家IPO批文，平均每月37家；我们监控的A类账户打新收益率1.2个亿规模年化收益率最高12.8%，较上月上行10BP；B类账户打新收益率1.2个亿规模年化收益率最高为11.35%，较上月上行1BP；C类非受限账户打新收益率0.6个亿全沪市底仓年化收益率最高为5.03%，较上月上行6BP，1.2个亿年化收益率为3.84%，和上月持平。\\r\\n    行业关注：建议关注银行、交通运输、纺织服装；";
        //ss="资讯信息\\r\\n    1）美国农业部公布的出口销售报告显示，截至11月18日当周，美国2017/18年度大豆出口销售净增86.91万吨，低于市场预估的100-140万吨区间下沿，较上周减少21%，较前四周均值减少45%，并创市场年度新低。一方面，巴西旧作大豆加紧销售步伐，另一方面明年新豆价格具有竞争力也对远期美豆销售预期产生一定影响。\\r\\n    2）近期巴西天气适宜，大豆播种进度加快，播种率达到84%，超过往年均值，本周巴西天气依旧有利于大豆生长。同时，阿根廷部分产区将迎来降雨，天气状况的改善将令CBOT大豆期价承压。\\r\\n    后市展望\\r\\n    今日豆类油脂市场调整为主。豆粕盘中延续上周减仓的态势，今日盘中再度减仓6.6万手，菜粕盘中减仓1.6万手；豆油盘中减仓1.6万手，棕榈油盘中减仓1.4万手，豆类油脂市场资金继续小幅流出。虽然短期国内豆粕市场受到GMO证书审查趋紧的支撑，豆粕供应持续紧张，但目前已经是最为紧张的阶段，下游市场后续采购谨慎制约了豆粕现货市场成交放量。未来将随着大豆卸货节奏的加快，豆粕市场供应最紧张的阶段将会过去，下游市场的集中采购需求是否被激发仍需观察。短期来看，在市场利多影响集中释放之后，在美豆期价陷入盘整的背景下，叠加近期市场资金的持续流出，前期豆粕期价累积的涨幅或将出现回吐。";
        //String xx = Baidu.translate_html(ss,"zh","en");
        //System.out.println("success"+ss.length());
        //System.out.println(xx);
        ss = "美国农业部公布的出口销售报告显示美国农业部公布的出口销售报告显示";
        String xy = Baidu.translate_text(ss, "zh", "en");
        System.out.println(xy);

    }
}
