/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import java.util.List;
import java.util.Properties;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
@Configuration
public class NacosConfig {

    @Value("${nacos.address}")
    private String address;

    @Value("${nacos.namespace}")
    private String namespace;

    @Value("${nacos.clusterName}")
    private String clusterName;

    @Value("${nacos.username}")
    private String username;

    @Value("${nacos.password}")
    private String password;

    @Bean
    public Converter<List<FlowRuleEntity>, String> flowRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<List<DegradeRuleEntity>, String> degradeRuleEntityEncoder() {
        return JSON::toJSONString;
    }
    @Bean
    public Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, FlowRuleEntity.class);
    }

    @Bean
    public Converter<String, List<DegradeRuleEntity>> degradeRuleEntityDecoder() {
        return s -> JSON.parseArray(s, DegradeRuleEntity.class);
    }

    @Bean
    public ConfigService nacosConfigService() throws Exception {
        System.out.println("server:" + this.address +
                "   namespace:" + this.namespace +
                "   clusterName:" + this.clusterName +
                "   username:" + this.username +
                "   password:" + this.password);
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, this.address);
        properties.put(PropertyKeyConst.NAMESPACE, this.namespace);
        properties.put(PropertyKeyConst.CLUSTER_NAME, this.clusterName);
        properties.put(PropertyKeyConst.USERNAME, this.username);
        properties.put(PropertyKeyConst.PASSWORD, this.password);
        return ConfigFactory.createConfigService(properties);
        //return ConfigFactory.createConfigService("localhost");
    }
}
