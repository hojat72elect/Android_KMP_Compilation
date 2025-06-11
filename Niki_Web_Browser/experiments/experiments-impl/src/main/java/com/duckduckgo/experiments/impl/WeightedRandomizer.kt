

package com.duckduckgo.experiments.impl

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution

interface IndexRandomizer {
    fun random(items: List<Probabilistic>): Int
}

interface Probabilistic {
    val weight: Double
}

@ContributesBinding(AppScope::class)
class WeightedRandomizer @Inject constructor() : IndexRandomizer {

    override fun random(items: List<Probabilistic>): Int {
        val indexArray = arrayPopulatedWithIndexes(items)
        val probabilitiesArray = arrayPopulatedWithProbabilities(items)

        val intDistribution = EnumeratedIntegerDistribution(indexArray, probabilitiesArray)
        return intDistribution.sample()
    }

    private fun arrayPopulatedWithIndexes(items: List<Probabilistic>): IntArray {
        return IntArray(items.size) { i -> i }
    }

    private fun arrayPopulatedWithProbabilities(items: List<Probabilistic>): DoubleArray {
        return items.map { it.weight }.toDoubleArray()
    }
}
