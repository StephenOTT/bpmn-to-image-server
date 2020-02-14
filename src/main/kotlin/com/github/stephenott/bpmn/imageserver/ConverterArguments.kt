package com.github.stephenott.bpmn.imageserver

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected

@Introspected
data class ConverterArguments(
        @JsonProperty("min_width") val minWidth: Int? = null,
        @JsonProperty("min_height") val minHeight: Int? = null,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("no_title") val noTitle: Boolean? = null,
        @JsonProperty("no_footer") val noFooter: Boolean? = null,
        @JsonProperty("scale") val scale: Int = 1
)