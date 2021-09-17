package nl.codecentric.axon.openadmin

import nl.codecentric.axon.openadmin.events.EventTailingService
import nl.codecentric.axon.openadmin.metrics.TokenStatusService
import nl.codecentric.axon.openadmin.model.TokenInformationDTO
import nl.codecentric.axon.openadmin.processors.ProcessorStatusService
import org.axonframework.config.EventProcessingModule
import org.axonframework.eventhandling.StreamingEventProcessor
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${axon.admin.base-url:axon-admin}")
class AxonOpenAdminEndpoint(
        private val tokenStatusService: TokenStatusService,
        private val processorStatusService: ProcessorStatusService,
        private val eventProcessingModule: EventProcessingModule,
        private val eventTailingService: EventTailingService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/tokens")
    fun getTokens(): TokenInformationDTO {
        return tokenStatusService.getTokenInformation()
    }

    @GetMapping("/processors")
    fun getProcessors() = processorStatusService.getStatus()

    @GetMapping("/events")
    fun getEvents() = eventTailingService.getEvents()

    @GetMapping("/events/{aggregateId}")
    fun getEventsForAggregate(@PathVariable aggregateId: String) = eventTailingService.getEvents(aggregateId)

    @PostMapping("/processor/{processorName}/split/{segmentId}")
    fun split(@PathVariable processorName: String, @PathVariable segmentId: Int): ResponseEntity<Unit> {
        return runOnProcessorWithResponse(processorName) {
            val status = it.processingStatus()[segmentId]
            val replayAfter = status?.isReplaying == true // When splitting, axon reverts to a normal token for some reason
            if (!it.isRunning || status == null) {
                logger.error("Will not split segment since it is not running on this node")
                return@runOnProcessorWithResponse false
            }
            it.splitSegment(segmentId).get()
            if (replayAfter) {
                if (it.isRunning) {
                    it.shutDown()
                }
                it.resetTokens()
                it.start()
            }
            true
        }
    }

    @PostMapping("/processor/{processorName}/merge/{segmentId}")
    fun merge(@PathVariable processorName: String, @PathVariable segmentId: Int): ResponseEntity<Unit> {
        return runOnProcessorWithResponse(processorName) {
            logger.warn("Starting merge")
            if (!it.isRunning) {
                logger.error("Will not merge segment since it is not running on this node")
                return@runOnProcessorWithResponse false
            }
            logger.warn("Attempting merge!")
            val status = it.processingStatus()[segmentId]
            if (status != null) {
                status.segment.mergeableSegmentId()
                it.mergeSegment(segmentId).get()
                logger.warn("Merge successful")
            } else {
                logger.warn("Merge failed")
                return@runOnProcessorWithResponse false
            }
            true
        }
    }

    @PostMapping("/processor/{processorName}/release/{segmentId}")
    fun release(@PathVariable processorName: String, @PathVariable segmentId: Int): ResponseEntity<Unit> {
        return runOnProcessorWithResponse(processorName) {
            logger.warn("Starting release")
            it.processingStatus()[segmentId] ?: return@runOnProcessorWithResponse false
            it.releaseSegment(segmentId)
            true
        }
    }

    @PostMapping("/processor/{processorName}/stop")
    fun stop(@PathVariable processorName: String): ResponseEntity<Unit> {
        return runOnProcessorWithResponse(processorName) {
            it.shutDown()
            true
        }
    }

    @PostMapping("/processor/{processorName}/start")
    fun start(@PathVariable processorName: String): ResponseEntity<Unit> {
        return runOnProcessorWithResponse(processorName) {
            it.start()
            true
        }
    }

    @PostMapping("/processor/{processorName}/reset")
    fun reset(@PathVariable processorName: String): ResponseEntity<Unit> {
        return runOnProcessorWithResponse(processorName) {
            if (it.isRunning) {
                it.shutDown()
            }
            it.resetTokens()
            it.start()
            true
        }
    }

    private fun runOnProcessorWithResponse(processorName: String, block: (StreamingEventProcessor) -> Boolean): ResponseEntity<Unit> {
        val eventProcessor = eventProcessingModule.eventProcessor(processorName, StreamingEventProcessor::class.java)
        if(!eventProcessor.isPresent) {
            return ResponseEntity.status(409).build()
        }
        val result = eventProcessor
            .map(block)
            .orElse(false)
        tokenStatusService.updateCachedInformation()

        return if (result) ResponseEntity.ok().build() else ResponseEntity.status(500).build()
    }
}
