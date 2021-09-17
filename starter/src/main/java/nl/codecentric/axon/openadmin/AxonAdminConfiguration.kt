package nl.codecentric.axon.openadmin

import nl.codecentric.axon.openadmin.tokens.DummyTokenProvider
import nl.codecentric.axon.openadmin.tokens.JdbcTokenProvider
import nl.codecentric.axon.openadmin.tokens.JpaTokenProvider
import nl.codecentric.axon.openadmin.tokens.TokenProvider
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.sql.DataSource

@Configuration(proxyBeanMethods = false)
@ComponentScan("nl.codecentric.axon.openadmin")
class AxonAdminConfiguration (
    @Value("\${server.servlet.context-path:}")
    val contextPath: String,
    @Value("\${axon.admin.base-url:axon-admin}")
    val axonAdminPath: String,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    fun logInitialization() {
        logger.info("Thanks for using AxonOpenAdmin in your application. To get started, navigate to $contextPath/$axonAdminPath")
    }

    @Bean
    fun tokenProvider(tokenStore: TokenStore, dataSource: DataSource?): TokenProvider {
        if (tokenStore is JpaTokenStore) {
            return JpaTokenProvider(tokenStore)
        } else if (tokenStore is JdbcTokenStore) {
            return JdbcTokenProvider(tokenStore, dataSource!!)
        }
        throw IllegalArgumentException("No matching store!")
    }

    @Bean
    @ConditionalOnMissingBean(TokenStore::class)
    fun tokenProvider() = DummyTokenProvider()
}

