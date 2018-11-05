package info.tmpz84.app.kassis.fileprocessor

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.Naming
import org.seasar.doma.jdbc.dialect.PostgresDialect
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource
import org.seasar.doma.jdbc.tx.LocalTransactionManager

object ConfigAdapter : Config {

    private val dialect = PostgresDialect()

    private val dataSource = LocalTransactionDataSource(
            "jdbc:postgresql://localhost:5432/kassis_soda_development", null, null)

    private val transactionManager = LocalTransactionManager(
            dataSource.getLocalTransaction(jdbcLogger))

    override fun getDialect() = dialect

    override fun getDataSource() = dataSource

    override fun getTransactionManager() = transactionManager

    override fun getNaming() = Naming.SNAKE_LOWER_CASE
}