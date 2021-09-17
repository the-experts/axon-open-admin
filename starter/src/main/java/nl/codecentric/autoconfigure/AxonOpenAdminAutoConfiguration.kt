package nl.codecentric.autoconfigure

import nl.codecentric.axon.openadmin.AxonAdminConfiguration
import org.axonframework.springboot.autoconfig.AxonAutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration(proxyBeanMethods = false)
@EnableScheduling
@Import(AxonAdminConfiguration::class)
@AutoConfigureAfter(AxonAutoConfiguration::class)
class AxonOpenAdminAutoConfiguration
