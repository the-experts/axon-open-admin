package nl.codecentric.axon.openadmin.command.handlers

import nl.codecentric.axon.openadmin.command.AxonAdminCommand
import nl.codecentric.axon.openadmin.command.AxonAdminCommandHandler
import nl.codecentric.axon.openadmin.command.AxonAdminCommmandType
import nl.codecentric.axon.openadmin.metrics.TokenStatusService
import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.config.EventProcessingModule
import org.axonframework.eventhandling.StreamingEventProcessor
import org.springframework.stereotype.Component

@Component
class ReleaseSegmentAxonAdminCommandHandler(
        eventProcessingModule: EventProcessingConfiguration,
        tokenStatusService: TokenStatusService
) : AxonAdminCommandHandler(eventProcessingModule, tokenStatusService) {
    override fun executeCommand(command: AxonAdminCommand, eventProcessor: StreamingEventProcessor) {
        val segment = command.segment ?: throw IllegalArgumentException("No segment supplied in command")
        eventProcessor.processingStatus()[segment]
                ?: throw IllegalStateException("No segment ${segment} present on this node")
        eventProcessor.releaseSegment(segment)
    }

    override val commandType = AxonAdminCommmandType.RELEASE_SEGMENT
}
