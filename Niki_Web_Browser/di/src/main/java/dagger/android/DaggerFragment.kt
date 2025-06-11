

package dagger.android

import android.annotation.SuppressLint
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.duckduckgo.di.DaggerMap
import javax.inject.Inject

@SuppressLint("NoFragment") // this is base fragment class to be used instead of Fragment
abstract class DaggerFragment(@LayoutRes contentLayoutId: Int = 0) : Fragment(contentLayoutId), HasDaggerInjector {
    @Inject
    lateinit var injectorFactoryMap: DaggerMap<Class<*>, AndroidInjector.Factory<*, *>>

    override fun daggerFactoryFor(key: Class<*>): AndroidInjector.Factory<*, *> {
        return injectorFactoryMap[key]
            ?: throw RuntimeException(
                """
                Could not find the dagger component for ${key.simpleName}.
                You probably forgot to annotate your class with @InjectWith(Scope::class).
                """.trimIndent(),
            )
    }
}
