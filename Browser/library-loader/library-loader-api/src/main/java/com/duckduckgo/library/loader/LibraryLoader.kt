

package com.duckduckgo.library.loader

import android.content.Context
import com.getkeepsafe.relinker.ReLinker
import com.getkeepsafe.relinker.ReLinker.LoadListener

class LibraryLoader {
    companion object {
        fun loadLibrary(context: Context, name: String) {
            ReLinker.loadLibrary(context, name)
        }

        fun loadLibrary(context: Context, name: String, listener: LibraryLoaderListener) {
            ReLinker.loadLibrary(context, name, listener)
        }
    }

    interface LibraryLoaderListener : LoadListener
}
