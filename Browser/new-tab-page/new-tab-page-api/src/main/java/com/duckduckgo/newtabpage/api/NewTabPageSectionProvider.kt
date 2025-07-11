

package com.duckduckgo.newtabpage.api

import kotlinx.coroutines.flow.Flow

interface NewTabPageSectionProvider {

    fun provideSections(): Flow<List<NewTabPageSectionPlugin>>
}
