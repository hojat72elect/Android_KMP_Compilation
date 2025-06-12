package de.nulide.findmydevice.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mikepenz.aboutlibraries.LibsBuilder;

import java.util.List;

import de.nulide.findmydevice.R;
import de.nulide.findmydevice.data.SettingsRepository;
import de.nulide.findmydevice.ui.TaggedFragment;
import de.nulide.findmydevice.ui.helper.SettingsEntry;
import de.nulide.findmydevice.ui.helper.SettingsViewAdapter;
import de.nulide.findmydevice.utils.SettingsImportExporter;


public class SettingsFragment extends TaggedFragment {

    private final int EXPORT_REQ_CODE = 30;
    private final int IMPORT_REQ_CODE = 40;

    private SettingsRepository settings;

    @NonNull
    @Override
    public String getStaticTag() {
        return "SettingsFragment";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = SettingsRepository.Companion.getInstance(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<SettingsEntry> settingsEntries = SettingsEntry.getSettingsEntries(view.getContext());

        ListView listSettings = view.findViewById(R.id.listSettings);
        listSettings.setAdapter(new SettingsViewAdapter(view.getContext(), settingsEntries));
        listSettings.setOnItemClickListener(this::onItemClick);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = view.getContext();

        Intent settingIntent = null;
        switch (position) {
            case 0:
                settingIntent = new Intent(context, FMDConfigActivity.class);
                break;
            case 1:
                if (!settings.serverAccountExists()) {
                    settingIntent = new Intent(context, AddAccountActivity.class);
                } else {
                    settingIntent = new Intent(context, FMDServerActivity.class);
                }
                break;
            case 2:
                settingIntent = new Intent(context, AllowlistActivity.class);
                break;
            case 3:
                settingIntent = new Intent(context, OpenCellIdActivity.class);
                break;
            case 4:
                settingIntent = new Intent(context, AppearanceActivity.class);
                break;
            case 5:
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.putExtra(Intent.EXTRA_TITLE, SettingsImportExporter.Companion.filenameForExport());
                intent.setType("*/*");
                startActivityForResult(intent, EXPORT_REQ_CODE);
                break;
            case 6:
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                startActivityForResult(intent, IMPORT_REQ_CODE);
                break;
            case 7:
                settingIntent = new Intent(context, LogViewActivity.class);
                break;
            case 8:
                String activityTitle = getString(R.string.Settings_About);
                settingIntent = new LibsBuilder().withActivityTitle(activityTitle).withListener(AboutLibsListener.listener).intent(context);
                break;
        }
        if (settingIntent != null) {
            startActivity(settingIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getActivity();
        if (context == null) {
            return;
        }
        if (requestCode == IMPORT_REQ_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    new SettingsImportExporter(context).importData(uri);
                }
            }
        } else if (requestCode == EXPORT_REQ_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    new SettingsImportExporter(context).exportData(uri);
                }
            }
        }
    }
}
