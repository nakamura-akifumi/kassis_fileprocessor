package info.tmpz84.app.kassis.fileprocessor

import info.tmpz84.app.kassis.fileprocessor.domain.model.User
import java.util.*
import javax.naming.AuthenticationException
import javax.naming.Context
import javax.naming.directory.InitialDirContext
import javax.naming.directory.BasicAttributes
import javax.naming.directory.BasicAttribute
import javax.naming.directory.DirContext


class LdapService {

    lateinit var ctx: DirContext

    init {
        println("init.")
    }

    fun disconnect() {
        try {
            ctx.close()
        } catch (e: Exception) {
            println("error misc")
            println(e)

            //その他のエラー
        }
    }
    fun connect() {
        println("connect ldap.")
        val env: Hashtable<String, String> = Hashtable()
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory")

        env.put(Context.PROVIDER_URL , "ldap://localhost/ou=users,dc=example,dc=com" )

        env.put("java.naming.ldap.version", "3")
        env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=example,dc=com")
        env.put(Context.SECURITY_CREDENTIALS, "kassispassword")

        try {
            ctx = InitialDirContext(env)
        } catch (ae: AuthenticationException) {
            println("認証エラー")
            //認証エラー
            println(ae)
        } catch (e: Exception) {
            println("error misc")
            println(e)

            //その他のエラー
        }
    }

    fun create(user: User) {

        try {
            //TODO: KEYの名前
            val attrs = BasicAttributes()
            val dn = "uid=${user.username}"

            val attrUid = BasicAttribute("uid")
            attrUid.add(0, user.username)
            attrs.put(attrUid)

            val attrSn = BasicAttribute("sn")
            attrSn.add(0, user.username)
            attrs.put(attrSn)

            val attrCn = BasicAttribute("cn")
            attrCn.add(0, user.username)
            attrs.put(attrCn)

            val attrUserPassword = BasicAttribute("userPassword")
            attrUserPassword.add(0, user.password)
            attrs.put(attrUserPassword)

            val attrObjClass = BasicAttribute("objectClass")
            attrObjClass.add(0, "inetOrgPerson")
            attrs.put(attrObjClass)


            ctx.createSubcontext(dn, attrs);
            //println("success")
        } catch (ae: AuthenticationException) {
            println("認証エラー")
            //認証エラー
            println(ae)
        } catch (e: Exception) {
            println("error misc")
            println(e)

            //その他のエラー
        }


    }

}