

package com.duckduckgo.lint

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.ULambdaExpression
import java.util.EnumSet



class NonCancellableDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String>? {
        return listOf("launch", "launchIn", "launchWhen", "async", "produce")
    }

    override fun visitMethodCall(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        val methodName = method.name
        if (methodName == "launch" || methodName == "launchIn" || methodName == "launchWhen" || methodName == "async" || methodName == "produce") {
            val arguments = node.valueArguments.takeUnless { it.isEmpty() } ?: return
            arguments.forEach { argument ->
                if (isNonCancellable(argument)) {
                    context.report(
                        ISSUE_NON_CANCELLABLE,
                        node,
                        context.getLocation(node),
                        "Avoid using NonCancellable when launching a coroutine."
                    )
                }
            }
        }
    }

    private fun isNonCancellable(argument: UExpression): Boolean {
        if(argument is ULambdaExpression) return false
        // Check if the argument is NonCancellable
        return argument.asSourceString().contains("NonCancellable")
    }

    companion object {
        val ISSUE_NON_CANCELLABLE = Issue.create(
            id = "NonCancellableUsage",
            briefDescription = "Avoid using NonCancellable when launching a coroutine.",
            explanation = "NonCancellable should not be used when launching coroutines. Use AppCoroutineScope instead.",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = Implementation(
                NonCancellableDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE, Scope.TEST_SOURCES)
            )
        )
    }
}
