package de.nulide.findmydevice.commands

import android.content.Context
import de.nulide.findmydevice.R
import de.nulide.findmydevice.data.EncryptedSettingsRepository
import de.nulide.findmydevice.data.Settings
import de.nulide.findmydevice.data.SettingsRepository
import de.nulide.findmydevice.services.FmdJobService
import de.nulide.findmydevice.transports.Transport
import de.nulide.findmydevice.utils.Notifications
import de.nulide.findmydevice.utils.log
import kotlinx.coroutines.CoroutineScope


// Order matters for the home screen
fun availableCommands(context: Context): List<Command> {
    val commands = mutableListOf(
        BluetoothCommand(context),
        CameraCommand(context),
        DeleteCommand(context),
        GpsCommand(context),
        // HelpCommand(context),
        LocateCommand(context),
        LockCommand(context),
        NoDisturbCommand(context),
        RingCommand(context),
        StatsCommand(context),
    )
    // FIXME: The HelpCommand does not know about itself
    commands.add(HelpCommand(commands, context))
    return commands
}

/**
 * CommandHandler is the entry point for taking a string,
 * mapping it to a Command, and executing the command.
 *
 * Access control is done internally, after parsing the command.
 *
 * @param job
 * An optional FmdJobService that is running this command, and its JobParameters.
 * If this is non-null, the Command should call job.jobFinished() when it is done.
 * (This is like a callback.)
 */
class CommandHandler<T>
@JvmOverloads constructor(
    private val transport: Transport<T>,
    private val coroutineScope: CoroutineScope,
    private val job: FmdJobService?,
    private val showUsageNotification: Boolean = true,
) {

    /**
     * Parses and executes a command of the form "triggerWord command options", e.g. "fmd locate cell"
     */
    @JvmOverloads
    fun execute(
        context: Context,
        rawCommand: String,
        onHandlingStarted: () -> Unit = {},
    ) {
        context.log().d(
            TAG,
            "Handling command '$rawCommand' from source '${transport.getDestinationString()}'"
        )

        val settings = SettingsRepository.getInstance(context)
        val fmdTriggerWord = settings.get(Settings.SET_FMD_COMMAND) as String

        val encSettings = EncryptedSettingsRepository.getInstance(context)
        val expectedPin = encSettings.getFmdPin()

        val cmds = availableCommands(context)
        val parser =
            CommandParser(fmdTriggerWord, expectedPin, HelpCommand(cmds, context), cmds)
        val parsed = parser.parse(rawCommand)

        when (parsed) {
            is ParserResult.Success -> {
                context.log().d(TAG, "Executing command: ${parsed.command.keyword}")
                if (!transport.isAllowed(parsed)) {
                    context.log().e(TAG, "Aborting, the transport denied the access.")
                    return
                }
                if (showUsageNotification) {
                    showUsageNotification(context, rawCommand)
                }
                // Only call this if we are actually handling the command, and not aborting.
                onHandlingStarted()
                parsed.command.execute(parsed.args, transport, coroutineScope, job)
            }

            is ParserResult.Empty -> {
                context.log().w(TAG, "Cannot handle: args is empty.")
            }

            is ParserResult.TriggerWordMismatch -> {
                context.log().w(
                    TAG,
                    "Not handling: '${parsed.actual}' does not match trigger word '${parsed.expected}'"
                )
            }

            is ParserResult.UnknownCommand -> {
                context.log().w(TAG, "No command found that matches '${parsed.commandKeyword}'")
            }
        }
    }

    private fun showUsageNotification(context: Context, rawCommand: String) {
        val source = transport.getDestinationString()
        Notifications.notify(
            context,
            context.getString(R.string.usage_notification_title),
            context.getString(R.string.usage_notification_text_source, rawCommand, source),
            Notifications.CHANNEL_USAGE
        )
    }

    companion object {
        val TAG = CommandHandler::class.simpleName
    }
}
