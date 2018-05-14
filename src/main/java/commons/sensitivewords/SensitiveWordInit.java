package commons.sensitivewords;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author wugaoshang
 * @Description: 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 */
public class SensitiveWordInit {

    private static final String UTF8 = "UTF-8";
    @SuppressWarnings("rawtypes")
    public HashMap sensitiveWordMap;

    public SensitiveWordInit() {
        super();
    }

    public Map initKeyWord() {
        try {
            // 读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile();
            // 将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
            // spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sensitiveWordMap;
    }


    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        // 初始化敏感词容器，减少扩容操作
        sensitiveWordMap = new HashMap(keyWordSet.size());
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        // 迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            // 关键字
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                // 转换成char型
                char keyChar = key.charAt(i);
                // 获取
                Object wordMap = nowMap.get(keyChar);
                 // 如果存在该key，直接赋值
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                // 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                } else {
                    newWorMap = new HashMap<String, String>();
                    // 不是最后一个
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
            }
            // 最后一个
            nowMap.put("isEnd", "1");
        }
    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     *
     * @throws Exception
     */
    @SuppressWarnings("resource")
    private Set<String> readSensitiveWordFile() throws Exception {
        Set<String> set = null;
        Resource resource = new ClassPathResource("SensitiveWords.txt");
        File file = resource.getFile();
        // 文件流是否存在
        if (file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), UTF8);
            set = new HashSet<>();
            BufferedReader bufferedReader = new BufferedReader(read);
            try {
                String txt = null;
                // 读取文件，将文件内容放入到set中
                while ((txt = bufferedReader.readLine()) != null) {
                    set.add(txt.trim());
                }
            } catch (Exception e) {
                throw e;
            } finally {
                bufferedReader.close();
            }
        } else { // 不存在抛出异常信息
            throw new Exception("敏感词库文件不存在");
        }
        return set;
    }
}
