package ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder

import ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder.whereclause.WhereClauseBuilderFactory

object ApicalypseQueryBuilderFactory {

    fun create(): ApicalypseQueryBuilder {
        return ApicalypseQueryBuilderImpl(
            whereClauseBuilderFactory = WhereClauseBuilderFactory
        )
    }
}
