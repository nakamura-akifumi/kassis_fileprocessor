package info.tmpz84.app.kassis.fileprocessor.domain.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.seasar.doma.Domain

@Domain(valueType = String::class)
@JsonIgnoreProperties(ignoreUnknown=true)
data class MessageAdapter(val key:String,
                          val filename:String,
                          val content_type:String,
                          val checksum:String)
