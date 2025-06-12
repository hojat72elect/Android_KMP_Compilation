package com.amaze.fileutilities.image_viewer.editor.filters

import ja.burhanrashid52.photoeditor.PhotoFilter

interface FilterListener {
    fun onFilterSelected(photoFilter: PhotoFilter?)
}
