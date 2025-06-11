

package com.duckduckgo.anvil.compiler

import com.squareup.anvil.annotations.ExperimentalAnvilApi
import com.squareup.anvil.compiler.internal.reference.AnnotatedReference
import com.squareup.anvil.compiler.internal.reference.AnnotationReference
import com.squareup.anvil.compiler.internal.reference.ClassReference
import com.squareup.anvil.compiler.internal.reference.argumentAt
import org.jetbrains.kotlin.name.FqName

@OptIn(ExperimentalAnvilApi::class)
internal fun AnnotationReference.bindingKeyOrNull(): ClassReference? = argumentAt("bindingKey", 1)?.value()

@OptIn(ExperimentalAnvilApi::class)
internal fun AnnotatedReference.isAnnotatedWith(fqName: List<FqName>): Boolean {
    return annotations.any { it.fqName in fqName }
}

@OptIn(ExperimentalAnvilApi::class)
internal fun AnnotatedReference.fqNameIntersect(fqName: List<FqName>): List<FqName> {
    annotations.map { it.fqName }.intersect(fqName.toSet()).let {
        return it.toList()
    }
}

@OptIn(ExperimentalAnvilApi::class)
internal fun AnnotatedReference.filterQualifierAnnotations(): List<AnnotationReference> {
    return annotations.filter { it.isQualifier() }
}
