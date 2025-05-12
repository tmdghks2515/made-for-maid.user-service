package io.madeformaid.user.global.event.publisher

import event.ImageEvent.ImageUnusingEvent
import event.ImageEvent.ImageUsingEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ImageEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {
    fun publishImageUsing(event: ImageUsingEvent) {
        kafkaTemplate.send("image-using", "123", "event")
//        kafkaTemplate.send("image.using", event.imageId, event.toByteArray())
    }

    fun publishImageUnusing(event: ImageUnusingEvent) {
        kafkaTemplate.send("image-unusing", "123", "event")
//        kafkaTemplate.send("image.unusing", event.imageId, event.toByteArray())
    }
}
