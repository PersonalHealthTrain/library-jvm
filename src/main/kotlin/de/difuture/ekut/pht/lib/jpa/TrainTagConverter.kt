package de.difuture.ekut.pht.lib.jpa

import de.difuture.ekut.pht.lib.data.TrainTag
import de.difuture.ekut.pht.lib.data.toTrainTag
import javax.persistence.AttributeConverter

class TrainTagConverter : AttributeConverter<TrainTag, String> {

    override fun convertToDatabaseColumn(attribute: TrainTag) = attribute.repr
    override fun convertToEntityAttribute(dbData: String) = dbData.toTrainTag()
}
