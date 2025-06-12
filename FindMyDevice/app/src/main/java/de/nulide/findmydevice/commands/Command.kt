package de.nulide.findmydevice.commands

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import de.nulide.findmydevice.R
import de.nulide.findmydevice.data.SettingsRepository
import de.nulide.findmydevice.permissions.Permission
import de.nulide.findmydevice.services.FmdJobService
import de.nulide.findmydevice.transports.Transport
import de.nulide.findmydevice.utils.log
import kotlinx.coroutines.CoroutineScope


abstract class Command(val context: Context) {
    companion object {
        private val TAG = Command::class.simpleName
    }

    val settings = SettingsRepository.getInstance(context)

    abstract val keyword: String
    abstract val usage: String

    @get:DrawableRes
    abstract val icon: Int

    @get:StringRes
    abstract val shortDescription: Int

    @get:StringRes
    abstract val longDescription: Int?

    abstract val requiredPermissions: List<Permission>
    open val optionalPermissions: List<Permission> = emptyList()

    fun missingRequiredPermissions(): List<Permission> {
        return requiredPermissions.filter { p -> !p.isGranted(context) }
    }

    fun <T> execute(
        args: List<String>,
        transport: Transport<T>,
        coroutineScope: CoroutineScope,
        job: FmdJobService?,
    ) {
        val missing = missingRequiredPermissions()
        if (missing.isNotEmpty()) {
            val msg = context.getString(
                R.string.cmd_missing_permissions,
                args.joinToString(" "),
                missing.joinToString(", ") { it.toString(context) }
            )
            context.log().w(TAG, msg)
            transport.send(context, msg)
            job?.jobFinished()
            return
        }
        // Continue executing command.
        // The concrete classes should implement executeInternal.
        executeInternal(args, transport, coroutineScope, job)
    }

    internal abstract fun <T> executeInternal(
        args: List<String>,
        transport: Transport<T>,
        coroutineScope: CoroutineScope,
        job: FmdJobService?,
    )
}
