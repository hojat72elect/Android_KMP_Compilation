package ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder.whereclause.conditions

import ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder.whereclause.WhereClauseBuilderFactory

object ConditionBuilderFactory {

    fun newBuilder(conditionType: ConditionType): ConditionBuilder {
        return ConditionBuilderImpl(
            conditionType = conditionType,
            whereClauseBuilderFactory = WhereClauseBuilderFactory
        )
    }
}
