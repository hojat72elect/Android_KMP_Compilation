package com.simplemobiletools.smsmessenger.models.contacts

import com.simplemobiletools.smsmessenger.helpers.SMT_PRIVATE

data class ContactSource(var name: String, var type: String, var publicName: String, var count: Int = 0) {
    fun getFullIdentifier(): String {
        return if (type == SMT_PRIVATE) {
            type
        } else {
            "$name:$type"
        }
    }
}
