package NotificationService.meesho.configuration;

import NotificationService.meesho.constants.ElasticSearchSmsRequestsConstants;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = ElasticSearchSmsRequestsConstants.BASE_PACKAGES)
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
    @Override
    @Bean(name = ElasticSearchSmsRequestsConstants.ELASTIC_SEARCH_CLIENT)
    public RestHighLevelClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(ElasticSearchSmsRequestsConstants.HOST_NAME,
                ElasticSearchSmsRequestsConstants.PORT));
        return new RestHighLevelClient(builder);
    }
}
