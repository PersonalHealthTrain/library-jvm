package de.difuture.ekut.pht.lib.train.api

sealed class Property : Typed

sealed class EnvironmentVariableProperty(open val name: String) : Property() {

    override val type = "http://www.wikidata.org/entity/Q400857"
    override val display = "environmentVariable"
    abstract val target: String
}

data class UrlEnvironmentVariableProperty(
    override val name: String
) : EnvironmentVariableProperty(name) {
    override val target = "http://www.wikidata.org/entity/Q400857"
}
