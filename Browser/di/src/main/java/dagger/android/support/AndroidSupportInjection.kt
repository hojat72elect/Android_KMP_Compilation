

package dagger.android.support

import android.view.View
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector

class AndroidSupportInjection {
    companion object {
        inline fun <reified T : Fragment> inject(instance: T) {
            AndroidInjector.inject(AndroidInjection.findHasDaggerInjectorForFragment(instance), instance)
        }

        inline fun <reified T : View> inject(instance: T) {
            AndroidInjector.inject(AndroidInjection.findHasDaggerInjectorForView(instance), instance)
        }
    }
}
