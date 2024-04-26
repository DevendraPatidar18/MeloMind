package com.example.melomind;

import static android.os.Environment.getExternalStorageDirectory;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity   {
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        Button lo = findViewById(R.id.lo);
        SearchView search = findViewById(R.id.search_bar);



        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (arrayAdapter != null) {
                    arrayAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });



        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ArrayList<File> mySongs = fetchSongs(getExternalStorageDirectory());
                        String[] items = new String[mySongs.size()];


                        for (int i = 0; i < mySongs.size(); i++) {
                            items[i] = mySongs.get(i).getName().replace(".mp3", "");
                        }
                        Arrays.sort(items);
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(arrayAdapter);
                        CustomArrayAdapter adapter = new CustomArrayAdapter(MainActivity.this, mySongs);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener((adapterView, view, i, l) -> {
                            Intent intent = new Intent(MainActivity.this, Playsong.class);
                            String currentsong = listView.getItemAtPosition(i).toString();
                            intent.putExtra("songList", mySongs);
                            intent.putExtra("currentsong", currentsong);
                            intent.putExtra("Position", i);
                            startActivity(intent);

                        });


                    }


                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                })
                .check();

        lo.setOnClickListener(view -> {
            Intent ii = new Intent(MainActivity.this,web.class);

            startActivity(ii);

        });
    }


    public ArrayList <File>  fetchSongs(File file) {
        ArrayList arraylist = new ArrayList();
        File[] songs = file.listFiles();
        if (songs != null) {
            for (File myFile : songs) {
                if (!myFile.isHidden() && myFile.isDirectory()) {
                    arraylist.addAll(fetchSongs(myFile));
                } else {
                    if (myFile.getName().endsWith("mp3") || (myFile.getName().endsWith("m4a")) && !myFile.getName().startsWith(".")) {
                        arraylist.add(myFile);
                    }
                }
            }
        }
        return arraylist;

    }
}