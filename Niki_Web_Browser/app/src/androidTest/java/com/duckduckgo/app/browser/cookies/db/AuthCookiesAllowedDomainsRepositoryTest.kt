

package com.duckduckgo.app.browser.cookies.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.common.test.CoroutineTestRule
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthCookiesAllowedDomainsRepositoryTest {
    @get:Rule
    @Suppress("unused")
    val coroutineRule = CoroutineTestRule()

    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var authCookiesAllowedDomainsDao: AuthCookiesAllowedDomainsDao
    private lateinit var authCookiesAllowedDomainsRepository: AuthCookiesAllowedDomainsRepository

    @Before
    fun before() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        authCookiesAllowedDomainsDao = db.authCookiesAllowedDomainsDao()
        authCookiesAllowedDomainsRepository = AuthCookiesAllowedDomainsRepository(authCookiesAllowedDomainsDao, coroutineRule.testDispatcherProvider)
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun whenAddDomainIfIsEmptyThenReturnNull() = runTest {
        assertNull(authCookiesAllowedDomainsRepository.addDomain(""))
    }

    @Test
    fun whenAddDomainAndDomainNotValidThenReturnNull() = runTest {
        assertNull(authCookiesAllowedDomainsRepository.addDomain("https://example.com"))
    }

    @Test
    fun whenAddValidDomainThenReturnNonNull() = runTest {
        assertNotNull(authCookiesAllowedDomainsRepository.addDomain("example.com"))
    }

    @Test
    fun whenGetDomainIfDomainExistsThenReturnAllowedDomainEntity() = runTest {
        givenAuthCookieAllowedDomain("example.com")

        val authCookieAllowedDomainEntity = authCookiesAllowedDomainsRepository.getDomain("example.com")
        assertEquals("example.com", authCookieAllowedDomainEntity?.domain)
    }

    @Test
    fun whenGetDomainIfDomainDoesNotExistThenReturnNull() = runTest {
        val authCookieAllowedDomainEntity = authCookiesAllowedDomainsRepository.getDomain("example.com")
        assertNull(authCookieAllowedDomainEntity)
    }

    @Test
    fun whenRemoveDomainThenDomainDeletedFromDatabase() = runTest {
        givenAuthCookieAllowedDomain("example.com")
        val authCookieAllowedDomainEntity = authCookiesAllowedDomainsRepository.getDomain("example.com")

        authCookiesAllowedDomainsRepository.removeDomain(authCookieAllowedDomainEntity!!)

        val deletedEntity = authCookiesAllowedDomainsRepository.getDomain("example.com")
        assertNull(deletedEntity)
    }

    @Test
    fun whenDeleteAllThenAllDomainsDeletedExceptFromTheExceptionList() = runTest {
        givenAuthCookieAllowedDomain("example.com", "example2.com")

        authCookiesAllowedDomainsRepository.deleteAll(listOf("example.com"))

        assertNull(authCookiesAllowedDomainsRepository.getDomain("example2.com"))
        assertNotNull(authCookiesAllowedDomainsRepository.getDomain("example.com"))
    }

    private fun givenAuthCookieAllowedDomain(vararg allowedDomain: String) {
        allowedDomain.forEach {
            authCookiesAllowedDomainsDao.insert(AuthCookieAllowedDomainEntity(domain = it))
        }
    }
}
