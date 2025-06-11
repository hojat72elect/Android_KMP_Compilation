

package dagger.android

import androidx.appcompat.app.AppCompatActivity
import com.duckduckgo.di.DaggerMap
import javax.inject.Inject

abstract class DaggerActivity : AppCompatActivity(), HasDaggerInjector {
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
