package nl.codecentric.axon.openadmin.command.handlers

import nl.codecentric.axon.openadmin.command.AxonAdminCommand
import nl.codecentric.axon.openadmin.command.AxonAdminCommandHandler
import nl.codecentric.axon.openadmin.command.AxonAdminCommmandType
import nl.codecentric.axon.openadmin.metrics.TokenStatusService
import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.config.EventProcessingModule
import org.axonframework.eventhandling.StreamingEventProcessor
import org.springframework.stereotype.Component

@Component
class StopProcessorAxonAdminCommandHandler(
        eventProcessingModule: EventProcessingConfiguration,
        tokenStatusService: TokenStatusService
) : AxonAdminCommandHandler(eventProcessingModule, tokenStatusService) {
    override fun executeCommand(command: AxonAdminCommand, eventProcessor: StreamingEventProcessor) {
        eventProcessor.shutDown()
    }

    override val commandType = AxonAdminCommmandType.STOP_PROCESSOR
}
