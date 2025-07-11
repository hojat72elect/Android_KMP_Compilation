

package com.duckduckgo.networkprotection.impl.connectionclass

import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.exp
import kotlin.math.ln

class ExponentialGeometricAverage @Inject constructor() {
    private val decayConstant = DEFAULT_DECAY_CONSTANT
    private val cutover = ceil(1 / decayConstant)

    private var count = 0
    private var value: Double = -1.0

    internal val average: Double
        get() = value

    internal fun addMeasurement(measurement: Double) {
        val keepConstant = 1 - decayConstant

        value = if (count > cutover) {
            exp(keepConstant * ln(value) + decayConstant * ln(measurement))
        } else if (count > 0) {
            val retained: Double = keepConstant * count / (count + 1.0)
            val newcomer = 1.0 - retained
            exp(retained * ln(value) + newcomer * ln(measurement))
        } else {
            measurement
        }
        count++
    }

    internal fun reset() {
        value = -1.0
        count = 0
    }
}

/**
 * The factor used to calculate the moving average depending upon the previous calculated value.
 * The smaller this value is, the less responsive to new samples the moving average becomes.
 */
private const val DEFAULT_DECAY_CONSTANT = 0.1
