package com.burhanrashid52.photoediting.filters

import com.burhanrashid52.photoediting.core.PhotoFilter

interface FilterListener {
    fun onFilterSelected(photoFilter: PhotoFilter)
}