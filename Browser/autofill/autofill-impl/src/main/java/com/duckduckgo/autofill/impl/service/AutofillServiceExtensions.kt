

package com.duckduckgo.autofill.impl.service

import com.duckduckgo.autofill.impl.service.AutofillFieldType.UNKNOWN

fun List<AutofillRootNode>.findBestFillableNode(): AutofillRootNode? {
    return this.firstNotNullOfOrNull { rootNode ->
        val focusedDetectedField = rootNode.parsedAutofillFields
            .firstOrNull { field ->
                field.originalNode.isFocused && field.type != UNKNOWN
            }
        if (focusedDetectedField != null) {
            return@firstNotNullOfOrNull rootNode
        }

        val firstDetectedField = rootNode.parsedAutofillFields.firstOrNull { field -> field.type != UNKNOWN }
        if (firstDetectedField != null) {
            return@firstNotNullOfOrNull rootNode
        }
        return@firstNotNullOfOrNull null
    }
}
