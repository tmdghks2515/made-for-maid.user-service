package io.madeformaid.user.global.event.publisher

import event.ImageEvent.ImageUnusingEvent
import event.ImageEvent.ImageUsingEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ImageEventPublisher(
    private val imageUsingTemplate: KafkaTemplate<String, ImageUsingEvent>,
    private val imageUnusingTemplate: KafkaTemplate<String, ImageUnusingEvent>
) {
    fun publishImageUsing(event: ImageUsingEvent) {
        imageUsingTemplate.send("image.using", event.imageId, event)
    }

    fun publishImageUnusing(event: ImageUnusingEvent) {
        imageUnusingTemplate.send("image.unusing", event.imageId, event)
    }
}