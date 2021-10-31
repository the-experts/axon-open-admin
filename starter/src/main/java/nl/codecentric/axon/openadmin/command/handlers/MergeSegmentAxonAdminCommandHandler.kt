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
class MergeSegmentAxonAdminCommandHandler(
        eventProcessingModule: EventProcessingConfiguration,
        tokenStatusService: TokenStatusService
) : AxonAdminCommandHandler(eventProcessingModule, tokenStatusService) {
    override fun executeCommand(command: AxonAdminCommand, eventProcessor: StreamingEventProcessor) {
        val segment = command.segment ?: throw IllegalArgumentException("No segment supplied in command")
        val status = eventProcessor.processingStatus()[segment]
        if (status != null) {
            status.segment.mergeableSegmentId()
            eventProcessor.mergeSegment(segment).get()
        } else {
            throw IllegalStateException("Will not split segment since it is not running on this node")
        }
    }

    override val commandType = AxonAdminCommmandType.MERGE_SEGMENT
}
