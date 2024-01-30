package NotificationService.meesho.Configuration;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

@Configuration
@EnableElasticsearchRepositories(basePackages = "NotificationService.meesho.repositories")
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    private String hostname = "127.0.0.1";
    private int port = 9200;

    @Override
    @Bean(name = "elastic-search-client")
    public RestHighLevelClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(hostname, port));
        return new RestHighLevelClient(builder);
    }
}
