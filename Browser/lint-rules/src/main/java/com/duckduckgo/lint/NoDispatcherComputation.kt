

package com.duckduckgo.lint



import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UExpression
import java.util.EnumSet



class NoDispatcherComputation : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> {
        return listOf("computation")
    }

    override fun visitMethodCall(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        // Get the containing class of the method
        val containingClass = method.containingClass ?: return

        // Check if the containing class implements DispatcherProvider
        if (implementsDispatcherProvider(containingClass)) {
            context.report(
                ISSUE_AVOID_COMPUTATION,
                node,
                context.getLocation(node),
                ISSUE_DESCRIPTION,
            )
        }
    }

    private fun implementsDispatcherProvider(clazz: PsiClass): Boolean {
        return clazz.allMethods.any { it.name == "computation" && it.containingClass?.qualifiedName == "com.duckduckgo.common.utils.DispatcherProvider" }
    }

    companion object {
        const val ISSUE_DESCRIPTION = "Avoid using computation() if this is not a valid usecase."
        val ISSUE_AVOID_COMPUTATION = Issue.create(
            id = "AvoidComputationUsage",
            briefDescription = ISSUE_DESCRIPTION,
            explanation = "Usages of computation() should be carefully considered as they can lead to performance issues.",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = Implementation(
                NoDispatcherComputation::class.java,
                EnumSet.of(Scope.JAVA_FILE, Scope.TEST_SOURCES)
            )
        )
    }
}
