

package com.duckduckgo.subscriptions.impl.model

data class Entitlement(
    /**
     * Name of the entitlement.
     */
    val name: String,

    /**
     * Name of the product represented by this entitlement.
     */
    val product: String,
)
