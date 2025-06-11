

package com.duckduckgo.autofill.api

import com.duckduckgo.feature.toggles.api.FeatureToggles
import com.duckduckgo.feature.toggles.api.Toggle

class FakeAutofillFeature private constructor() {

    companion object {
        fun create(): AutofillFeature {
            return FeatureToggles.Builder()
                .store(
                    object : Toggle.Store {
                        private val map = mutableMapOf<String, Toggle.State>()

                        override fun set(key: String, state: Toggle.State) {
                            map[key] = state
                        }

                        override fun get(key: String): Toggle.State? {
                            return map[key]
                        }
                    },
                )
                .featureName("fakeAutofill")
                .build()
                .create(AutofillFeature::class.java)
        }
    }
}
