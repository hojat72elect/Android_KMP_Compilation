package ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder.whereclause.conditions

import ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder.whereclause.Condition

 interface ConditionBuilder {
    fun condition(condition: Condition): ConditionBuilder
    fun build(): String
}
