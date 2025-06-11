package ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder.whereclause

import ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder.whereclause.conditions.ConditionBuilderFactory

object WhereClauseBuilderFactory {

    fun newBuilder(): WhereClauseBuilder {
        return WhereClauseBuilderImpl(
            conditionBuilderFactory = createConditionBuilderFactory()
        )
    }

    private fun createConditionBuilderFactory(): ConditionBuilderFactory {
        return ConditionBuilderFactory
    }
}
