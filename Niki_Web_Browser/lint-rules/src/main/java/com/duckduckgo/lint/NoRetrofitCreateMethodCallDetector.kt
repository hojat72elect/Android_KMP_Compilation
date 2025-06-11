

package com.duckduckgo.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Severity.ERROR
import com.intellij.psi.impl.compiled.ClsClassImpl
import org.jetbrains.uast.UCallExpression
import java.util.*

@Suppress("UnstableApiUsage")
class NoRetrofitCreateMethodCallDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler = NoInternalImportHandler(context)

    internal class NoInternalImportHandler(private val context: JavaContext) : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            if ((node.resolve()?.parent as? ClsClassImpl)?.stub?.qualifiedName == "retrofit2.Retrofit" && node.methodName == "create") {
                context.report(
                    issue = NO_RETROFIT_CREATE_CALL,
                    location = context.getNameLocation(node),
                    message = NO_RETROFIT_CREATE_CALL.getExplanation(TextFormat.RAW)
                )
            }
        }
    }

    companion object {
        val NO_RETROFIT_CREATE_CALL = Issue.create(
            id = "NoRetrofitCreateMethodCallDetector",
            briefDescription = "Do not use retrofit.create().",
            explanation = """
                retrofit.create() should not be used directly.
                Use @ContributesServiceApi annotation instead.
            """.trimIndent(),
            moreInfo = "https://app.asana.com/0/0/1203425544997317/f",
            category = Category.CUSTOM_LINT_CHECKS,
            priority = 10,
            severity = ERROR,
            implementation = Implementation(NoRetrofitCreateMethodCallDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }
}
