

package com.duckduckgo.networkprotection.impl.configuration

import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

/**
 * A [SSLSocketFactory] that delegates calls. Sockets can be configured after creation by
 * overriding [.configureSocket].
 */
abstract class DelegatingSSLSocketFactory(
    private val delegate: SSLSocketFactory,
) : SSLSocketFactory() {
    @Throws(IOException::class)
    override fun createSocket(): SSLSocket {
        val sslSocket = delegate.createSocket() as SSLSocket
        return configureSocket(sslSocket)
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int): SSLSocket {
        val sslSocket = delegate.createSocket(host, port) as SSLSocket
        return configureSocket(sslSocket)
    }

    @Throws(IOException::class)
    override fun createSocket(
        host: String,
        port: Int,
        localAddress: InetAddress,
        localPort: Int,
    ): SSLSocket {
        val sslSocket = delegate.createSocket(host, port, localAddress, localPort) as SSLSocket
        return configureSocket(sslSocket)
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): SSLSocket {
        val sslSocket = delegate.createSocket(host, port) as SSLSocket
        return configureSocket(sslSocket)
    }

    @Throws(IOException::class)
    override fun createSocket(
        host: InetAddress,
        port: Int,
        localAddress: InetAddress,
        localPort: Int,
    ): SSLSocket {
        val sslSocket = delegate.createSocket(host, port, localAddress, localPort) as SSLSocket
        return configureSocket(sslSocket)
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return delegate.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return delegate.supportedCipherSuites
    }

    @Throws(IOException::class)
    override fun createSocket(
        socket: Socket,
        host: String,
        port: Int,
        autoClose: Boolean,
    ): SSLSocket {
        val sslSocket = delegate.createSocket(socket, host, port, autoClose) as SSLSocket
        return configureSocket(sslSocket)
    }

    @Throws(IOException::class)
    abstract fun configureSocket(sslSocket: SSLSocket): SSLSocket
}
