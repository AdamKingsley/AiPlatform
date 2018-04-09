package cn.edu.nju.software.common.druid;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by mengf on 2018/4/6 0006.
 */

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.datasource.druid.web-stat-filter")
public class DruidWebStatFilter {
    private boolean enabled;
    private String urlPattern;
    private String exclusions;
    private Boolean sessionStatEnable;
    private Integer sessionStatMaxCount;
//    private Boolean principalSessionName;
//    private String principalCookieName;
//    private Boolean profileEnable;


    @Override
    public String toString() {
        return "DruidWebStatFilter{" +
                "enabled=" + enabled +
                ", urlPattern='" + urlPattern + '\'' +
                ", exclusions='" + exclusions + '\'' +
                ", sessionStatEnable=" + sessionStatEnable +
                ", sessionStatMaxCount=" + sessionStatMaxCount +
                '}';
    }
}
