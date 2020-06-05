package com.myapplicationdev.android.p07_ps;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

public class FragmentSecond extends Fragment {
    TextView tv1, tv2;
    Button b1, b2;
    EditText et1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container, false);
        tv1 = view.findViewById(R.id.tvFrag2);
        tv2 = view.findViewById(R.id.textView2);
        b1 = view.findViewById(R.id.btnAddTextFrag2);
        et1 = view.findViewById(R.id.editText2);
        b2 = view.findViewById(R.id.button);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permission = PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_SMS);
                if (permission != PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, 0);
                    return;
                }
                Uri uri = Uri.parse("content://sms");
                String[] reqCols = new String[]{"date", "address", "body", "type"};
                ContentResolver cr = getActivity().getContentResolver();
                String filter = "body LIKE ?";
                String[] split = et1.getText().toString().split(",", 0);
                String[] ar = new String[split.length];
                ar[0] = "%" + split[0] + "%";
                for (int i = 1; i < split.length; i++) {
                    filter += " OR body LIKE ?";
                    ar[i] = "%" + split[i] + "%";
                }
                Cursor cursor = cr.query(uri, reqCols, filter, ar, null);
                String smsBody = "";
                if (cursor.moveToFirst()) {
                    do {
                        long dateinMillis = cursor.getLong(0);
                        String date = (String) DateFormat.format("dd MM yyyy h:mm:ss aa", dateinMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox: ";
                        } else {
                            type = "Sent: ";
                        }
                        smsBody += type + address + "\n" + "at" + date + "\n" + body + "\n";
                    } while (cursor.moveToNext());
                }
                tv2.setText(smsBody);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"ronmoy88@gmail.com"});
                int permission = PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_SMS);
                if (permission != PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, 0);
                    return;
                }
                Uri uri = Uri.parse("content://sms");
                String[] reqCols = new String[]{"date", "address", "body", "type"};
                ContentResolver cr = getActivity().getContentResolver();
                String filter = "body LIKE ?";
                String[] split = et1.getText().toString().split(",", 0);
                String[] ar = new String[split.length];
                ar[0] = "%" + split[0] + "%";
                for (int i = 1; i < split.length; i++) {
                    filter += " OR body LIKE ?";
                    ar[i] = "%" + split[i] + "%";
                }
                Cursor cursor = cr.query(uri, reqCols, filter, ar, null);
                String smsBody = "";
                if (cursor.moveToFirst()) {
                    do {
                        long dateinMillis = cursor.getLong(0);
                        String date = (String) DateFormat.format("dd MM yyyy h:mm:ss aa", dateinMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox: ";
                        } else {
                            type = "Sent: ";
                        }
                        smsBody += type + address + "\n" + "at" + date + "\n" + body + "\n";
                    } while (cursor.moveToNext());
                }
                email.putExtra(Intent.EXTRA_TEXT, smsBody);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Please choose 1 Email client :"));
            }
        });

        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    b1.performClick();
                } else {
                    Toast.makeText(getActivity(), "Permission Not Granted", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
