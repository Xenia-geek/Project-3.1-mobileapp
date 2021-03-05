package yakubenko.bstu.organizelaboratoryworks.fragments.support;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import yakubenko.bstu.organizelaboratoryworks.R;

public class SupportFragment extends Fragment {

    private static final int REQUEST_CODE_READ_CONTACTS = 100;
    private static boolean READ_CONTACTS_GRANTED = false;

    ListView contactList;
    Cursor ContactsCursor;
    SimpleCursorAdapter ContactsAdapter;
    Button startLoad;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_support, container, false);

        contactList = root.findViewById(R.id.contactList);

        //+
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG);
                snackbar.setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(getContext(),"Your action", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                snackbar.show();
            }
        });

        //load
        startLoad = root.findViewById(R.id.button3);
        startLoad.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int hasReadContactPermission = getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS);

                if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED)
                    READ_CONTACTS_GRANTED = true;
                else // разрешения
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);

                if (READ_CONTACTS_GRANTED)
                    loadContacts();
            }
        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume()
    {
        super.onResume();
        loadContacts();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    READ_CONTACTS_GRANTED = true;
        }

        if (READ_CONTACTS_GRANTED)
            Toast.makeText(getContext(), "Разрешения установленны", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getContext(), "Требуется установить разрешения", Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void loadContacts()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ Manifest.permission.READ_CONTACTS }, REQUEST_CODE_READ_CONTACTS);
        } else {
            ContactsCursor = getContext().getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    new String[]{ ContactsContract.Contacts.DISPLAY_NAME_PRIMARY, ContactsContract.Contacts._ID },
                    null,
                    null,
                    null
            );

            String[] headers = new String[]{ ContactsContract.Contacts.DISPLAY_NAME_PRIMARY, ContactsContract.Contacts._ID };

            ContactsAdapter = new SimpleCursorAdapter(
                    getContext(),
                    android.R.layout.two_line_list_item,
                    ContactsCursor,
                    headers,
                    new int[]{ android.R.id.text1, android.R.id.text2 },
                    0
            );

            if(!ContactsAdapter.isEmpty())
                contactList.setAdapter(ContactsAdapter);
        }
    }

}