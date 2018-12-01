package info.tmpz84.app.kassis.fileprocessor.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.seasar.doma.Domain
import java.sql.Timestamp

@Domain(valueType = String::class)
@JsonIgnoreProperties(ignoreUnknown=true)
data class MessageAdapter(var id:Int,
                          var msgid:String,
                          var title:String?,
                          var status:String,
                          var state:String,
                          var message_type:String?,
                          var created_by:Int,
                          var created_at:Timestamp?,
                          var updated_at:Timestamp?)

