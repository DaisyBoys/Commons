package com.iooicc.GlobalNews.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by wugaoshang on 2018/2/7.
 */


@Configuration
@ConfigurationProperties(prefix="GlobalNews")
@PropertySource("classpath:/global_news.properties")
public class GlobalNewsSettings {

    private String imageDir;

    private String accessPath;

    public String getImageDir() {
        return imageDir;
    }

    public void setImageDir(String imageDir) {
        this.imageDir = imageDir;
    }

    public String getAccessPath() {
        return accessPath;
    }

    public void setAccessPath(String accessPath) {
        this.accessPath = accessPath;
    }
}
