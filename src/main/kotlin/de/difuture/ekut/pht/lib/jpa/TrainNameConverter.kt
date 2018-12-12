package de.difuture.ekut.pht.lib.jpa

import de.difuture.ekut.pht.lib.data.TrainName
import de.difuture.ekut.pht.lib.data.toTrainName
import javax.persistence.AttributeConverter

class TrainNameConverter : AttributeConverter<TrainName, String> {

    override fun convertToDatabaseColumn(attribute: TrainName) = attribute.repr
    override fun convertToEntityAttribute(dbData: String) = dbData.toTrainName()
}
