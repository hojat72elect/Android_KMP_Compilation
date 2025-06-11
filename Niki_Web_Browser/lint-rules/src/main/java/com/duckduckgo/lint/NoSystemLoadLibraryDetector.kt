

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
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.tryResolveNamed
import java.util.*

@Suppress("UnstableApiUsage")
class NoSystemLoadLibraryDetector : Detector(), SourceCodeScanner {
    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler = NoInternalImportHandler(context)

    internal class NoInternalImportHandler(private val context: JavaContext) : UElementHandler() {
        override fun visitCallExpression(node: UCallExpression) {
            if (node.methodName == "loadLibrary" && node.receiver?.tryResolveNamed()?.name == "System") {
                context.report(NO_SYSTEM_LOAD_LIBRARY, node, context.getNameLocation(node), "System.loadLibrary() should not be used.")
            }
        }
    }

    companion object {
        val NO_SYSTEM_LOAD_LIBRARY = Issue.create("NoSystemLoadLibrary",
            "Do not use System.loadLibrary().",
            """
                System.loadLibrary() should not be used.
                Use LibraryLoader.loadLibrary() instead.
            """.trimIndent(),
            Category.CORRECTNESS, 10, ERROR,
            Implementation(NoSystemLoadLibraryDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES))
        )
    }
}
