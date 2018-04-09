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
@ConfigurationProperties(prefix = "spring.datasource.druid.stat-view-servlet")
public class DruidStatViewServlet {
    private Boolean enabled;
    private String urlPattern;
    private Boolean resetEnable;
    private String loginUsername;
    private String loginPassword;
    private String allow;
    private String deny;
    private String aopPatterns;

    @Override
    public String toString() {
        return "DruidStatViewServlet{" +
                "enabled=" + enabled +
                ", urlPattern='" + urlPattern + '\'' +
                ", resetEnable='" + resetEnable + '\'' +
                ", loginUsername='" + loginUsername + '\'' +
                ", loginPassword='" + loginPassword + '\'' +
                ", allow='" + allow + '\'' +
                ", deny='" + deny + '\'' +
                ", aopPatterns='" + aopPatterns + '\'' +
                '}';
    }
}
