package de.difuture.ekut.pht.lib.train.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import de.difuture.ekut.pht.lib.data.TrainTag
import de.difuture.ekut.pht.lib.internal.RebaseStrategyDeserializer

@JsonDeserialize(using = RebaseStrategyDeserializer::class)
sealed class RebaseStrategy : Typed

data class DockerRebaseStrategy(

    @JsonProperty("export_files")
    val export_files: Set<String>,

    @JsonProperty("next_train_tag")
    val next_train_tag: TrainTag,

    @JsonProperty("from")
    val from: String
) : RebaseStrategy() {

    override val type = "docker"
    override val display = type
}
