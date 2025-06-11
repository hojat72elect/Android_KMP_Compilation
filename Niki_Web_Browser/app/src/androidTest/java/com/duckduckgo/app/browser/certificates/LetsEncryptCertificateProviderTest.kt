

package com.duckduckgo.app.browser.certificates

import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.browser.certificates.rootstore.IsrgRootX1
import com.duckduckgo.app.browser.certificates.rootstore.IsrgRootX2
import com.duckduckgo.app.browser.certificates.rootstore.LetsEncryptE1
import com.duckduckgo.app.browser.certificates.rootstore.LetsEncryptR3
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LetsEncryptCertificateProviderTest {

    private val certificateProvider =
        LetsEncryptCertificateProviderImpl(
            setOf(
                IsrgRootX1(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext),
                IsrgRootX2(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext),
                LetsEncryptR3(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext),
                LetsEncryptE1(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext),
            ),
        )

    @Test
    fun whenFindByCnameAndCnameHitThenReturnCertificate() {
        val certificate = certificateProvider.findByCname("R3")
        assertTrue(certificate is LetsEncryptR3)
    }

    @Test
    fun whenFindByCnameAndCnameMissThenReturnCertificate() {
        val certificate = certificateProvider.findByCname("no_match")
        assertNull(certificate)
    }
}
