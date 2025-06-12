package de.nulide.findmydevice.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.nulide.findmydevice.R
import de.nulide.findmydevice.data.LogRepository
import de.nulide.findmydevice.ui.FmdActivity
import de.nulide.findmydevice.ui.UiUtil.Companion.setupEdgeToEdgeAppBar
import de.nulide.findmydevice.ui.UiUtil.Companion.setupEdgeToEdgeScrollView
import de.nulide.findmydevice.utils.writeAsJson
import de.nulide.findmydevice.utils.writeToUri
import java.io.OutputStreamWriter


private const val EXPORT_REQ_CODE = 30

class LogViewActivity : FmdActivity() {


    private lateinit var repo: LogRepository

    private lateinit var adapter: LogViewAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        setupEdgeToEdgeAppBar(findViewById(R.id.appBar))
        setupEdgeToEdgeScrollView(findViewById(R.id.recycler_logs))

        repo = LogRepository.getInstance(this)

        // TODO: Observe list as LiveData or Flow
        adapter = LogViewAdapter()
        recyclerView = findViewById<RecyclerView>(R.id.recycler_logs)
        recyclerView.adapter = adapter

        synchronized(repo.list) { adapter.submitList(repo.list) }
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_log_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuExportLog) {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.putExtra(Intent.EXTRA_TITLE, LogRepository.filenameForExport())
            intent.type = "*/*"
            startActivityForResult(intent, EXPORT_REQ_CODE)
        } else if (item.itemId == R.id.menuClearLog) {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.log_view_clear))
                .setMessage(R.string.log_view_clear_confirm)
                .setPositiveButton(getString(R.string.Ok), { dialog, button ->
                    repo.clearLog()
                    // TODO: let adapter observe list, instead of explicitly updating the adapter
                    synchronized(repo.list) { adapter.submitList(repo.list) }
                    recyclerView.scrollToPosition(0)
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EXPORT_REQ_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                val uri = data.data
                if (uri != null) {
                    writeToUri(this, uri) { outputStream ->
                        synchronized(repo.list) { writeAsJson(OutputStreamWriter(outputStream), repo.list) }
                    }
                }
            }
        }
    }
}
