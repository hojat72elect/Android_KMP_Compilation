package de.nulide.findmydevice.ui.settings;

import static de.nulide.findmydevice.ui.UiUtil.setupEdgeToEdgeAppBar;
import static de.nulide.findmydevice.ui.UiUtil.setupEdgeToEdgeScrollView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.unifiedpush.android.connector.UnifiedPush;

import java.security.PrivateKey;

import de.nulide.findmydevice.R;
import de.nulide.findmydevice.data.EncryptedSettingsRepository;
import de.nulide.findmydevice.data.Settings;
import de.nulide.findmydevice.data.SettingsRepository;
import de.nulide.findmydevice.net.FMDServerApiRepoSpec;
import de.nulide.findmydevice.net.FMDServerApiRepository;
import de.nulide.findmydevice.permissions.NotificationAccessPermission;
import de.nulide.findmydevice.receiver.PushReceiver;
import de.nulide.findmydevice.services.ServerLocationUploadService;
import de.nulide.findmydevice.services.FmdBatteryLowService;
import de.nulide.findmydevice.services.ServerConnectivityCheckService;
import de.nulide.findmydevice.ui.FmdActivity;
import de.nulide.findmydevice.ui.home.PermissionView;
import de.nulide.findmydevice.utils.CypherUtils;
import de.nulide.findmydevice.utils.UnregisterUtil;
import de.nulide.findmydevice.utils.Utils;

public class FMDServerActivity extends FmdActivity implements CompoundButton.OnCheckedChangeListener, TextWatcher {

    private SettingsRepository settings;
    private FMDServerApiRepository fmdServerRepo;

    private EditText editTextCheckInterval;
    private EditText editTextNotifyAfterTime;
    private EditText editTextFMDServerUpdateTime;

    private CheckBox checkBoxFMDServerGPS;
    private CheckBox checkBoxFMDServerCell;

    private CheckBox checkBoxLowBat;

    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_m_d_server);

        setupEdgeToEdgeAppBar(findViewById(R.id.appBar));
        setupEdgeToEdgeScrollView(findViewById(R.id.scrollView));

        settings = SettingsRepository.Companion.getInstance(this);
        fmdServerRepo = FMDServerApiRepository.Companion.getInstance(new FMDServerApiRepoSpec(this));

        TextView textViewServerUrl = findViewById(R.id.textViewServerUrl);
        TextView textViewUserId = findViewById(R.id.textViewUserId);
        textViewServerUrl.setText((String) settings.get(Settings.SET_FMDSERVER_URL));
        textViewUserId.setText((String) settings.get(Settings.SET_FMDSERVER_ID));

        findViewById(R.id.buttonOpenWebClient).setOnClickListener(this::onOpenWebClientClicked);
        findViewById(R.id.buttonCopyServerUrl).setOnClickListener(this::onCopyServerUrlClicked);
        findViewById(R.id.buttonCopyUserId).setOnClickListener(this::onCopyUserIdClicked);

        findViewById(R.id.buttonChangePassword).setOnClickListener(this::onChangePasswordClicked);
        findViewById(R.id.buttonLogout).setOnClickListener(this::onLogoutClicked);
        findViewById(R.id.buttonDeleteData).setOnClickListener(this::onDeleteClicked);

        findViewById(R.id.buttonOpenPushDistributor).setOnClickListener(this::onOpenPushDistributorClicked);
        findViewById(R.id.buttonCopyPushDistributor).setOnClickListener(this::onCopyPushDistributorClicked);
        findViewById(R.id.buttonCopyPushUrl).setOnClickListener(this::onCopyPushUrlClicked);
        findViewById(R.id.buttonRegisterPush).setOnClickListener(this::onRegisterPushClicked);
        findViewById(R.id.buttonOpenUnifiedPush).setOnClickListener(this::onOpenUnifiedPushClicked);

        editTextCheckInterval = findViewById(R.id.editTextCheckInterval);
        editTextCheckInterval.setText(settings.get(Settings.SET_FMD_SERVER_CONNECTIVITY_CHECK_INTERVAL_HOURS).toString());
        editTextCheckInterval.addTextChangedListener(this);

        editTextNotifyAfterTime = findViewById(R.id.editTextNotifyAfterTime);
        editTextNotifyAfterTime.setText(settings.get(Settings.SET_FMD_SERVER_CONNECTIVITY_CHECK_NOTIFY_AFTER_HOURS).toString());
        editTextNotifyAfterTime.addTextChangedListener(this);

        editTextFMDServerUpdateTime = findViewById(R.id.editTextFMDServerUpdateTime);
        editTextFMDServerUpdateTime.setText(settings.get(Settings.SET_FMDSERVER_UPDATE_TIME).toString());
        editTextFMDServerUpdateTime.addTextChangedListener(this);

        checkBoxFMDServerGPS = findViewById(R.id.checkBoxFMDServerGPS);
        checkBoxFMDServerCell = findViewById(R.id.checkBoxFMDServerCell);
        switch ((Integer) settings.get(Settings.SET_FMDSERVER_LOCATION_TYPE)) {
            case 0:
                checkBoxFMDServerGPS.setChecked(true);
                checkBoxFMDServerCell.setChecked(false);
                break;
            case 1:
                checkBoxFMDServerGPS.setChecked(false);
                checkBoxFMDServerCell.setChecked(true);
                break;
            case 2:
                checkBoxFMDServerGPS.setChecked(true);
                checkBoxFMDServerCell.setChecked(true);
                break;
            case 3:
                checkBoxFMDServerGPS.setChecked(false);
                checkBoxFMDServerCell.setChecked(false);
                break;
        }
        checkBoxFMDServerGPS.setOnCheckedChangeListener(this);
        checkBoxFMDServerCell.setOnCheckedChangeListener(this);

        checkBoxLowBat = findViewById(R.id.checkBoxFMDServerLowBatUpload);
        checkBoxLowBat.setChecked((Boolean) settings.get(Settings.SET_FMD_LOW_BAT_SEND));
        checkBoxLowBat.setOnCheckedChangeListener(this);
        if ((Boolean) settings.get(Settings.SET_FMD_LOW_BAT_SEND)) {
            FmdBatteryLowService.scheduleJobNow(this);
        }

        PermissionView notificationAccessPerm = findViewById(R.id.perm_notification_access);
        notificationAccessPerm.setPermission(new NotificationAccessPermission(), this, true);

        getServerVersion();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkConnection();
        checkPushRegistration();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == checkBoxFMDServerCell || buttonView == checkBoxFMDServerGPS) {
            if (checkBoxFMDServerGPS.isChecked() && checkBoxFMDServerCell.isChecked()) {
                settings.set(Settings.SET_FMDSERVER_LOCATION_TYPE, 2);
            } else if (checkBoxFMDServerGPS.isChecked()) {
                settings.set(Settings.SET_FMDSERVER_LOCATION_TYPE, 0);
            } else if (checkBoxFMDServerCell.isChecked()) {
                settings.set(Settings.SET_FMDSERVER_LOCATION_TYPE, 1);
            } else {
                settings.set(Settings.SET_FMDSERVER_LOCATION_TYPE, 3);
            }
        } else if (buttonView == checkBoxLowBat) {
            settings.set(Settings.SET_FMD_LOW_BAT_SEND, isChecked);
            if (isChecked) {
                FmdBatteryLowService.scheduleJobNow(this);
            } else {
                FmdBatteryLowService.stopJobNow(this);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable edited) {
        String string = edited.toString();
        if (string.isEmpty()) {
            return;
        }

        long value;
        try {
            // The EditText's inputType shouldn't allow non-numbers, but catch the exception anyway.
            value = Long.parseLong(string);
        } catch (NumberFormatException e) {
            return;
        }

        if (edited == editTextCheckInterval.getText()) {
            settings.set(Settings.SET_FMD_SERVER_CONNECTIVITY_CHECK_INTERVAL_HOURS, value);

            if (value > 0) {
                ServerConnectivityCheckService.scheduleJob(this);
            } else {
                ServerConnectivityCheckService.cancelJob(this);
            }
        } else if (edited == editTextNotifyAfterTime.getText()) {
            settings.set(Settings.SET_FMD_SERVER_CONNECTIVITY_CHECK_NOTIFY_AFTER_HOURS, value);
        } else if (edited == editTextFMDServerUpdateTime.getText()) {
            settings.set(Settings.SET_FMDSERVER_UPDATE_TIME, (int) value);
        }
    }

    private void onOpenWebClientClicked(View view) {
        String url = (String) settings.get(Settings.SET_FMDSERVER_URL);
        Utils.openUrl(this, url);
    }

    private void onCopyServerUrlClicked(View view) {
        String label = getString(R.string.Settings_FMD_Server_Server_URL).replace(":", "");
        String text = (String) settings.get(Settings.SET_FMDSERVER_URL);
        Utils.copyToClipboard(this, label, text);
    }

    private void onCopyUserIdClicked(View view) {
        String label = getString(R.string.Settings_FMD_Server_User_ID).replace(":", "");
        String text = (String) settings.get(Settings.SET_FMDSERVER_ID);
        Utils.copyToClipboard(this, label, text);
    }

    private void onDeleteClicked(View view) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.Settings_FMDServer_Delete_Account))
                .setMessage(R.string.Settings_FMDServer_Alert_DeleteData_Desc)
                .setPositiveButton(getString(R.string.Ok), (dialog, whichButton) -> runDelete())
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void onLogoutClicked(View view) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.Settings_FMDServer_Logout_Button))
                .setMessage(R.string.Settings_FMDServer_Logout_Text)
                .setPositiveButton(getString(R.string.Ok), (dialog, whichButton) -> {
                    settings.removeServerAccount();
                    // TODO: API to invalidate access tokens. Maybe combine with session management.
                    EncryptedSettingsRepository encryptedSettingsRepo = EncryptedSettingsRepository.Companion.getInstance(this);
                    encryptedSettingsRepo.setCachedAccessToken("");
                    ServerLocationUploadService.cancelJob(this);
                    ServerConnectivityCheckService.cancelJob(this);
                    PushReceiver.unregisterWithUnifiedPush(this);
                    finish();
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void onChangePasswordClicked(View view) {
        LayoutInflater inflater = getLayoutInflater();
        final AlertDialog.Builder alert = new MaterialAlertDialogBuilder(this);
        alert.setTitle(getString(R.string.Settings_FMDServer_Change_Password_Button));
        View registerLayout = inflater.inflate(R.layout.dialog_password_change, null);
        alert.setView(registerLayout);
        EditText oldPasswordInput = registerLayout.findViewById(R.id.editTextFMDOldPassword);
        EditText passwordInput = registerLayout.findViewById(R.id.editTextPassword);
        EditText passwordInputCheck = registerLayout.findViewById(R.id.editTextFMDPasswordCheck);
        alert.setView(registerLayout);

        alert.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String oldPassword = oldPasswordInput.getText().toString();
                String password = passwordInput.getText().toString();
                String passwordCheck = passwordInputCheck.getText().toString();

                if (password.isEmpty() || oldPassword.isEmpty()) {
                    Toast.makeText(view.getContext(), R.string.pw_change_empty, Toast.LENGTH_LONG).show();
                } else if (!password.equals(passwordCheck)) {
                    Toast.makeText(view.getContext(), R.string.pw_change_mismatch, Toast.LENGTH_LONG).show();
                } else {
                    runChangePassword(oldPassword, password);
                }
            }
        });
        alert.show();
    }

    private void showLoadingIndicator(Context context) {
        View loadingLayout = getLayoutInflater().inflate(R.layout.dialog_loading, null);
        loadingDialog = new MaterialAlertDialogBuilder(context).setView(loadingLayout).setCancelable(false).create();
        loadingDialog.show();
    }

    private void onOpenPushDistributorClicked(View view) {
        String packageName = UnifiedPush.getDistributor(this);
        Utils.openApp(this, packageName);
    }

    private void onCopyPushDistributorClicked(View view) {
        String text = UnifiedPush.getDistributor(this);
        Utils.copyToClipboard(this, "Push Distributor", text);
    }

    private void onCopyPushUrlClicked(View view) {
        String text = (String) settings.get(Settings.SET_FMDSERVER_PUSH_URL);
        Utils.copyToClipboard(this, "Push URL", text);
    }

    private void onOpenUnifiedPushClicked(View view) {
        Utils.openUrl(this, "https://unifiedpush.org/");
    }

    private void runChangePassword(String oldPassword, String password) {
        showLoadingIndicator(this);
        // do expensive async crypto and hashing in a background thread (not on the UI thread)
        new Thread(() -> {
            try {
                PrivateKey privKey = CypherUtils.decryptPrivateKeyWithPassword((String) settings.get(Settings.SET_FMD_CRYPT_PRIVKEY), oldPassword);
                if (privKey == null) {
                    Toast.makeText(this, R.string.pw_change_wrong_password, Toast.LENGTH_LONG).show();
                    loadingDialog.cancel();
                    return;
                }
                String newPrivKey = CypherUtils.encryptPrivateKeyWithPassword(privKey, password);
                String hashedPW = CypherUtils.hashPasswordForLogin(password);

                runOnUiThread(() -> {
                    fmdServerRepo.changePassword(hashedPW, newPrivKey,
                            (response -> {
                                loadingDialog.cancel();
                                Toast.makeText(this, R.string.pw_change_success, Toast.LENGTH_LONG).show();
                            }),
                            (error) -> {
                                Toast.makeText(this, R.string.pw_change_network_failed, Toast.LENGTH_LONG).show();
                                loadingDialog.cancel();
                            });
                });
            } catch (Exception bdp) {
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.pw_change_wrong_password, Toast.LENGTH_LONG).show();
                    loadingDialog.cancel();
                });
            }
        }).start();
    }

    private void runDelete() {
        Context context = this;
        showLoadingIndicator(context);
        ServerLocationUploadService.cancelJob(context);
        ServerConnectivityCheckService.cancelJob(context);
        PushReceiver.unregisterWithUnifiedPush(context);
        fmdServerRepo.unregister(
                response -> {
                    loadingDialog.cancel();
                    Toast.makeText(context, R.string.Settings_FMDServer_Unregister_Success, Toast.LENGTH_LONG).show();
                    finish();
                }, error -> {
                    loadingDialog.cancel();
                    UnregisterUtil.showUnregisterFailedDialog(context, error, this::finish);
                }
        );
    }

    private void checkConnection() {
        TextView textViewConnectionStatus = findViewById(R.id.textViewConnectionStatus);

        fmdServerRepo.checkConnection(
                response -> {
                    settings.set(
                            Settings.SET_FMD_SERVER_LAST_CONNECTIVITY_UNIX_TIME,
                            System.currentTimeMillis()
                    );

                    textViewConnectionStatus.setText(R.string.Settings_FMD_Server_Connection_Status_Success);
                    textViewConnectionStatus.setTextColor(ContextCompat.getColor(this, R.color.md_theme_primary));
                    textViewConnectionStatus.setOnClickListener(v -> {
                    });
                },
                error -> {
                    textViewConnectionStatus.setText(error.toString());
                    textViewConnectionStatus.setTextColor(ContextCompat.getColor(this, R.color.md_theme_error));
                    textViewConnectionStatus.setOnClickListener(v -> {
                        Utils.copyToClipboard(this, "", error.toString());
                    });
                }
        );
    }

    @SuppressLint("SetTextI18n")
    private void getServerVersion() {
        TextView serverVersion = findViewById(R.id.serverVersion);

        String baseUrl = (String) settings.get(Settings.SET_FMDSERVER_URL);
        fmdServerRepo.getServerVersion(baseUrl, response -> {
            serverVersion.setText(getString(R.string.server_version) + ": " + response);
            serverVersion.setVisibility(View.VISIBLE);
        }, error -> {
            // Silently ignore
            serverVersion.setVisibility(View.GONE);
        });
    }

    private void checkPushRegistration() {
        if (!PushReceiver.isRegisteredWithUnifiedPush(this)) {
            PushReceiver.registerWithUnifiedPush(this);
        }

        LinearLayout sectionPushDistributor = findViewById(R.id.sectionPushDistributor);
        TextView textPushDistributor = findViewById(R.id.textPushDistributor);
        LinearLayout sectionPushUrl = findViewById(R.id.sectionPushUrl);
        TextView textPushUrl = findViewById(R.id.textPushUrl);
        Button buttonRegister = findViewById(R.id.buttonRegisterPush);

        String distributor = UnifiedPush.getDistributor(this);
        if (!distributor.isEmpty()) {
            sectionPushDistributor.setVisibility(View.VISIBLE);
            textPushDistributor.setText(getString(R.string.Settings_FMDServer_Push_Distributor, distributor));

            sectionPushUrl.setVisibility(View.VISIBLE);
            String url = (String) settings.get(Settings.SET_FMDSERVER_PUSH_URL);
            textPushUrl.setText(getString(R.string.Settings_FMDServer_Push_Url, url));

            buttonRegister.setText(R.string.Settings_FMDServer_Push_Register_Again);
        } else {
            sectionPushDistributor.setVisibility(View.GONE);
            sectionPushUrl.setVisibility(View.GONE);

            buttonRegister.setText(R.string.Settings_FMDServer_Push_Register);
        }
    }

    private void onRegisterPushClicked(View view) {
        PushReceiver.unregisterWithUnifiedPush(this);
        checkPushRegistration();

        // TODO: Hack to get the screen to refresh.
        // The proper way to do this would be callbacks.
        view.postDelayed(this::checkPushRegistration, 5 * 1000);
    }
}
