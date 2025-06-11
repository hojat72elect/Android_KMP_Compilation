package ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder.whereclause.conditions

import ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder.whereclause.Condition
import ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder.whereclause.WhereClauseBuilderFactory

class ConditionBuilderImpl(
    private val conditionType: ConditionType,
    private val whereClauseBuilderFactory: WhereClauseBuilderFactory
) : ConditionBuilder {

    private val conditionBuilder = StringBuilder()

    override fun condition(condition: Condition) = apply {
        conditionBuilder
            .append(conditionType.separator)
            .append(condition.buildCondition())
    }

    private fun Condition.buildCondition(): String {
        return whereClauseBuilderFactory.newBuilder().apply(this).build()
    }

    override fun build(): String {
        return conditionBuilder.toString()
    }
}
