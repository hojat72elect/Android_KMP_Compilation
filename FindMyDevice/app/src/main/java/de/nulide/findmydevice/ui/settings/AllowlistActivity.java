package de.nulide.findmydevice.ui.settings;

import static de.nulide.findmydevice.ui.UiUtil.setupEdgeToEdge;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.LinkedList;
import java.util.List;

import de.nulide.findmydevice.R;
import de.nulide.findmydevice.data.AllowlistRepository;
import de.nulide.findmydevice.data.Contact;
import de.nulide.findmydevice.data.Settings;
import de.nulide.findmydevice.data.SettingsRepository;
import de.nulide.findmydevice.ui.FmdActivity;
import de.nulide.findmydevice.ui.allowlist.AllowlistAdapter;
import kotlin.Unit;


public class AllowlistActivity extends FmdActivity {

    private AllowlistRepository allowlistRepository;
    private SettingsRepository settings;

    private AllowlistAdapter allowlistAdapter;

    private TextView textWhitelistEmpty;

    private final static int REQUEST_CODE = 6438;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowlist);

        setupEdgeToEdge(findViewById(android.R.id.content));

        allowlistRepository = AllowlistRepository.Companion.getInstance(this);
        settings = SettingsRepository.Companion.getInstance(this);

        allowlistAdapter = new AllowlistAdapter(this::onDeleteContact);
        RecyclerView recyclerView = findViewById(R.id.recycler_allowlist);
        recyclerView.setAdapter(allowlistAdapter);

        textWhitelistEmpty = findViewById(R.id.whitelistEmpty);
        findViewById(R.id.buttonAddContact).setOnClickListener(this::onAddContactClicked);
        findViewById(R.id.buttonAddPhoneNumber).setOnClickListener(this::onAddPhoneNumberClicked);

        updateScreen();
    }

    private void updateScreen() {
        if (allowlistRepository.getList().isEmpty()) {
            textWhitelistEmpty.setVisibility(View.VISIBLE);
        } else {
            textWhitelistEmpty.setVisibility(View.GONE);
        }

        allowlistAdapter.submitContactList(allowlistRepository.getList());
    }

    private void onAddPhoneNumberClicked(View v) {
        Context context = v.getContext();
        View layout = getLayoutInflater().inflate(R.layout.dialog_phone_number, null);
        EditText nameInput = layout.findViewById(R.id.editTextName);
        EditText phoneNumberInput = layout.findViewById(R.id.editTextPhoneNumber);

        new MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.allowlist_add_phone_number))
                .setView(layout)
                .setPositiveButton(getString(R.string.add), (dialog, whichButton) -> {
                    String name = nameInput.getText().toString();
                    String number = phoneNumberInput.getText().toString();
                    Contact dummyContact = new Contact(name, number);
                    addContactToAllowList(dummyContact);
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void onAddContactClicked(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            try {
                startActivityForResult(intent, REQUEST_CODE);
            } catch (ActivityNotFoundException e2) {
                Toast.makeText(this, getString(R.string.WhiteList_no_contact_picker), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    String[] projection = new String[]{
                            ContactsContract.Contacts.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    };
                    Cursor c = managedQuery(contactData, projection, null, null, null);
                    List<Contact> contacts = new LinkedList<>();
                    List<String> numbers = new LinkedList<>();
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME));
                        String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        contacts.add(new Contact(name, phoneNumber));
                        numbers.add(phoneNumber);

                        while (c.moveToNext()) {
                            String cNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String cName = c.getString(c.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                            if (!cNumber.isEmpty()) {
                                contacts.add(new Contact(cName, cNumber));
                                numbers.add(cNumber);
                            }
                        }
                    }

                    if (numbers.size() == 1) {
                        addContactToAllowList(contacts.get(0));
                    } else {
                        final List<Contact> finalContacts = contacts;
                        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this);
                        builder.setTitle(getString(R.string.WhiteList_Select_Number));
                        String[] numbersArray = numbers.toArray(new String[numbers.size()]);
                        builder.setItems(numbersArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addContactToAllowList(finalContacts.get(which));
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + reqCode);
        }
    }

    private void addContactToAllowList(Contact contact) {
        if (contact != null) {
            if (!allowlistRepository.contains(contact)) {
                allowlistRepository.add(contact);
                updateScreen();

                if (!(Boolean) settings.get(Settings.SET_FIRST_TIME_CONTACT_ADDED)) {
                    new MaterialAlertDialogBuilder(this)
                            .setMessage(this.getString(R.string.Alert_First_Time_contact_added))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    settings.set(Settings.SET_FIRST_TIME_CONTACT_ADDED, true);
                                }
                            })
                            .show();
                }
            } else {
                Toast toast = Toast.makeText(this, getString(R.string.Toast_Duplicate_contact), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    private Unit onDeleteContact(String phoneNumber) {
        allowlistRepository.remove(phoneNumber);
        updateScreen();
        // make Kotlin-interop happy
        return null;
    }

}