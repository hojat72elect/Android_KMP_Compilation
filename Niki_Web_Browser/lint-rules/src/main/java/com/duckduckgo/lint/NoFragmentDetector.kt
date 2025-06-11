

package com.duckduckgo.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.ERROR
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UClass
import java.util.*

@Suppress("UnstableApiUsage")
class NoFragmentDetector : Detector(), SourceCodeScanner {
    override fun getApplicableUastTypes() = listOf(UClass::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler = NoInternalImportHandler(context)

    internal class NoInternalImportHandler(private val context: JavaContext) : UElementHandler() {
        override fun visitClass(node: UClass) {
            if (node.extendsListTypes.any { it.className == "Fragment" }) {
                context.report(NO_FRAGMENT_ISSUE, node, context.getNameLocation(node), "Fragment should not be directly extended")
            }
        }
    }

    companion object {
        val NO_FRAGMENT_ISSUE = Issue.create("NoFragment",
            "The Fragment type should not be extended. Use DuckDuckGoFragment instead.",
            """
                The Fragment should not be used.
                Use DuckDuckGoFragment instead.
            """.trimIndent(),
            Category.CORRECTNESS, 10, ERROR,
            Implementation(NoFragmentDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES))
        )
    }
}
