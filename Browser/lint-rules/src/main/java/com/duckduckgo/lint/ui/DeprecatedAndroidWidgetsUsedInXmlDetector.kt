

package com.duckduckgo.lint.ui

import com.android.tools.lint.detector.api.Category.Companion.CUSTOM_LINT_CHECKS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.TextFormat
import com.android.tools.lint.detector.api.XmlContext
import com.duckduckgo.lint.ui.DesignSystemLintExtensions.Companion.DEPRECATED_WIDGETS
import org.w3c.dom.Element

@Suppress("UnstableApiUsage")
class DeprecatedAndroidWidgetsUsedInXmlDetector : LayoutDetector() {

    override fun getApplicableElements() = DEPRECATED_WIDGETS

    override fun visitElement(
        context: XmlContext,
        element: Element
    ) {
        reportUsage(context, element)
    }

    private fun reportUsage(
        context: XmlContext,
        element: Element
    ) {
        context.report(
            issue = DEPRECATED_WIDGET_IN_XML,
            location = context.getNameLocation(element),
            message = DEPRECATED_WIDGET_IN_XML.getExplanation(TextFormat.RAW),
        )
    }

    companion object {

        val DEPRECATED_WIDGET_IN_XML = Issue
            .create(
                id = "DeprecatedWidgetInXml",
                briefDescription = "Default Android Widget used instead of Design System Component",
                explanation = "Always favor the use of the Design System Component",
                moreInfo = "https://app.asana.com/0/1202857801505092/list",
                category = CUSTOM_LINT_CHECKS,
                priority = 10,
                severity = Severity.ERROR,
                androidSpecific = true,
                implementation = Implementation(
                    DeprecatedAndroidWidgetsUsedInXmlDetector::class.java,
                    Scope.RESOURCE_FILE_SCOPE,
                ),
            )
    }
}
