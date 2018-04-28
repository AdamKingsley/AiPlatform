package cn.edu.nju.software;

import cn.edu.nju.software.common.initial.BindingInitializer;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.support.WebBindingInitializer;
import tk.mybatis.spring.annotation.MapperScan;


import javax.sql.DataSource;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan("cn.edu.nju.software")
@MapperScan("cn.edu.nju.software.mapper")
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean(name="datasource")
    @ConfigurationProperties("spring.datasource")
    public DataSource getDataSource() {
        System.out.println("-------------------- primaryDataSource init with druid ---------------------");
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name="txManager")
    public DataSourceTransactionManager getDataSourceTransactionManager(@Qualifier("datasource") DataSource datasource) {
        System.out.println("-------------------- init txManager -----------------------");
        DataSourceTransactionManager dsm = new DataSourceTransactionManager();
        dsm.setDataSource(datasource);
        return dsm;
    }

    @Bean(name = "webBindingInitializer")
    public WebBindingInitializer getWebBindingInitializer(){
        return new BindingInitializer();
    }
}
