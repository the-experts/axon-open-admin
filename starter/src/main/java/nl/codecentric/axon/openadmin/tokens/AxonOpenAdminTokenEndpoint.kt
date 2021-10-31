package nl.codecentric.axon.openadmin.tokens

import nl.codecentric.axon.openadmin.metrics.TokenStatusService
import nl.codecentric.axon.openadmin.model.TokenInformationDTO
import nl.codecentric.axon.openadmin.processors.ProcessorStatusService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${axon.admin.base-url:axon-admin}")
class AxonOpenAdminTokenEndpoint(
        private val tokenStatusService: TokenStatusService,
        private val processorStatusService: ProcessorStatusService,
) {
    @GetMapping("/tokens")
    fun getTokens(): TokenInformationDTO {
        return tokenStatusService.getTokenInformation()
    }

    @GetMapping("/processors")
    fun getProcessors() = processorStatusService.getStatus()
}
