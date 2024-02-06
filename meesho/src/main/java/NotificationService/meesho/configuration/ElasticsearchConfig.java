package NotificationService.meesho.configuration;
import NotificationService.meesho.constants.ElasticSearchSmsRequestsConstants;
import org.apache.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

@Configuration
@EnableElasticsearchRepositories(basePackages = ElasticSearchSmsRequestsConstants.BASE_PACKAGES)
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    private String hostname = ElasticSearchSmsRequestsConstants.HOST_NAME;
    private int port = ElasticSearchSmsRequestsConstants.port;

    @Override
    @Bean(name = ElasticSearchSmsRequestsConstants.ELASTIC_SEARCH_CLIENT)
    public RestHighLevelClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(hostname, port));
        return new RestHighLevelClient(builder);
    }
}
