package info.tmpz84.app.kassis.fileprocessor.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
data class FileAdapter(val key:String,
                       val filename:String,
                       val content_type:String,
                       val checksum:String)
