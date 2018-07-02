package io.dmt.moneyxchange.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Properties specific to Moneyxchange.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    public static class Exchange{
        private int timeout = 10;

        public Exchange(int timeout) {
            this.timeout = timeout;
        }

        public Exchange() {
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }
    }
    private final ApplicationProperties.Exchange exchange = new ApplicationProperties.Exchange();

    public ApplicationProperties.Exchange getExchange() {
        return exchange;
    }
}
