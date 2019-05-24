package com.example.matan.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    NavigationView buttons;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ListView listView ;
    SwipeRefreshLayout swipeRefreshLayout;

    Button button_add, button_cancel, button_delete, button_update, button_logout_no, button_logout_yes, button_close;
    EditText editText_website_name, editText_website_url;

    List<HashMap<String, String>> listItems;

    DatabaseReference databaseReference;
    DatabaseReference getData;
    String Email;
    int p;
    int s;
    VideoView videoView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Email = getIntent().getStringExtra("EMAIL");
        Email = Email.replace('.', '_');

        databaseReference = FirebaseDatabase.getInstance().getReference(Email);

        listItems = new ArrayList<>();

        //Drawer menu + Header//
        drawerLayout = (DrawerLayout) findViewById(R.id.home_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");


        swipeRefreshLayout =findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.Blue));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listItems.clear();
                        onStart();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        buttons = (NavigationView) findViewById(R.id.navigation) ;
        buttons.setNavigationItemSelectedListener(this);

        //Listview + Adapter//
        listView = (ListView) findViewById(R.id.list);
        final SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.item_list,
                new String[]{"First Line", "Second Line", "Third Line"},
                new int[]{R.id.website_name, R.id.website_url, R.id.website_time});
        listView.setAdapter(adapter);




        //Intent to WebsiteActivity with the url and Email//
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(HomeActivity.this, WebsiteActivity.class);
                intent.putExtra("URL",listItems.get(position).get("Second Line"));
                intent.putExtra("EMAIL",Email);
                startActivity(intent);
                finish();
            }
        });

        //Open dialog on longclick - Cancel, Update, Delete//
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                p = position;

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this, R.style.RoundDialog);
                View mView = getLayoutInflater().inflate(R.layout.layout_dialog_delete_edit, null);

                mBuilder.setView(mView);
                final AlertDialog dialog_delete_edit = mBuilder.create();
                dialog_delete_edit.show();

                editText_website_name = (EditText) mView.findViewById(R.id.website_name);
                editText_website_url = (EditText) mView.findViewById(R.id.website_url);
                editText_website_name.setText(listItems.get(p).get("First Line"));
                editText_website_url.setText(listItems.get(p).get("Second Line"));

                button_cancel = (Button) mView.findViewById(R.id.cancel_button);
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_delete_edit.dismiss();
                    }
                });

                button_update = (Button) mView.findViewById(R.id.update_button);
                button_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        s = p;
                        String NameOfUpdateSite = listItems.get(p).get("First Line");

                        DatabaseReference dr2 = FirebaseDatabase.getInstance().getReference(Email+"/"+NameOfUpdateSite);
                        dr2.removeValue();
                        listItems.remove(p);

                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference(Email+"/"+editText_website_name.getText().toString());
                        Website website = new Website(editText_website_name.getText().toString(),editText_website_url.getText().toString(), System.currentTimeMillis());
                        dr.setValue(website);

//                        HashMap<String, String> test = new HashMap<String, String>();
//                        test.put("First Line",editText_website_name.getText().toString());
//                        test.put("Second Line",editText_website_url.getText().toString());
//                        listItems.set(s,test);
//                        dialog_delete_edit.dismiss();
//                        listView.invalidateViews();
                        dialog_delete_edit.dismiss();
                        listItems.clear();
                        onStart();
                    }
                });


                button_delete =(Button) mView.findViewById(R.id.delete_button);
                button_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String NameOfDeleteSite = listItems.get(p).get("First Line");
                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference(Email+"/"+NameOfDeleteSite);
                        dr.removeValue();
                        listItems.remove(p);
                        adapter.notifyDataSetChanged();
                        dialog_delete_edit.dismiss();
                    }
                });
                return true;
            }
        });

    }

    //Get all websites from Firebase database on start activity and show them//
    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot websiteSnapshot : dataSnapshot.getChildren())
                {
                    Website website = websiteSnapshot.getValue(Website.class);
                    String name = website.getmWebsiteName();
                    String url = website.getmWebsiteURL();
                    long time = website.getmTime();

                    HashMap<String, String> Load_websites = new HashMap<String, String>();
                    Load_websites.put("First Line",name);
                    Load_websites.put("Second Line",url);
                    Load_websites.put("Third Line", TimeAgo.getTimeAgo(time));
                    listItems.add(Load_websites);
                }

                listView.invalidateViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //Button on header- Add, Refresh//
    public boolean onOptionsItemSelected( final MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        if(item.getItemId() == R.id.add_button)
        {
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this, R.style.RoundDialog);
            View mView = getLayoutInflater().inflate(R.layout.layout_dialog_add, null);

            mBuilder.setView(mView);
            final AlertDialog dialog_add = mBuilder.create();

            editText_website_name = (EditText) mView.findViewById(R.id.website_name);
            editText_website_url = (EditText) mView.findViewById(R.id.website_url);

            button_add =(Button) mView.findViewById(R.id.add_button);
            button_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText_website_name.getText().toString().length()>0 && Patterns.WEB_URL.matcher(editText_website_url.getText().toString()).matches())
                    {
                        for (int i = 0; listItems.size()>i; i++)
                        {
                            if(editText_website_name.getText().toString().equals(listItems.get(i).get("First Line")))
                            {
                                Toast.makeText(HomeActivity.this, "You can not add the same name of a site twice", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                            Toast.makeText(HomeActivity.this, "Site successfully added", Toast.LENGTH_SHORT).show();
                            addWebsite();
                            HashMap<String, String> values = new HashMap<String, String>();
                            values.put("First Line", editText_website_name.getText().toString());
                            values.put("Second Line", editText_website_url.getText().toString().toLowerCase());
                            values.put("Third Line", TimeAgo.getTimeAgo(System.currentTimeMillis()));
                            listItems.add(values);
                            dialog_add.dismiss();
                            listView.invalidateViews();
                    }
                    else {
                        if(editText_website_name.getText().toString().matches(""))
                        {
                            Toast.makeText(HomeActivity.this, "Fill the website name ", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(editText_website_url.getText().toString().matches(""))
                            {
                                Toast.makeText(HomeActivity.this, "Fill the website url", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if (!Patterns.WEB_URL.matcher(editText_website_url.getText().toString()).matches())
                                {
                                    Toast.makeText(HomeActivity.this, "The URL not valid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            });


            button_cancel = (Button) mView.findViewById(R.id.cancel_button);
            button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    dialog_add.dismiss();
                }
            });

            dialog_add.show();
        }

        if(item.getItemId() == R.id.refresh_button)
        {
            if(!swipeRefreshLayout.isRefreshing())
            {
                listItems.clear();


                item.setEnabled(false);

                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                item.setEnabled(true);
                            }
                        });
                    }
                }, 500);

                onStart();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    //Show add and refresh button on header//
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Method that add the website to Firebase database//
    public void addWebsite(){
        String Name = editText_website_name.getText().toString();

        String id = databaseReference.push().getKey();
        Website website = new Website(editText_website_name.getText().toString(), editText_website_url.getText().toString(), System.currentTimeMillis());
        databaseReference.child(Name).setValue(website);
    }



    @Override
    public void onClick(View v) {
    }

    //Button in drawer menu - Logout, About, Share//
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.logout)
        {

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this, R.style.RoundDialog);
            View mView = getLayoutInflater().inflate(R.layout.layout_dialog_logout, null);

            mBuilder.setView(mView);
            final AlertDialog dialog_logout = mBuilder.create();
            dialog_logout.show();


            button_logout_no = (Button) mView.findViewById(R.id.no_button);
            button_logout_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_logout.dismiss();
                }
            });

            button_logout_yes = (Button) mView.findViewById(R.id.yes_button);
            button_logout_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final SharedPreferences pref = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("IsCheck");
                    editor.commit();



                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.putExtra("LastActivity","HomeActivity");
                    startActivity(intent);
                    finish();

                }
            });
        }

        if(menuItem.getItemId() == R.id.about)
        {
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            intent.putExtra("EMAIL",Email);
            startActivity(intent);
            finish();
        }

        if(menuItem.getItemId() == R.id.html)
        {
            Intent intent = new Intent(HomeActivity.this, HtmlActivity.class);
            intent.putExtra("EMAIL",Email);
            startActivity(intent);
            finish();
        }

        if(menuItem.getItemId() == R.id.share)
        {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Download my bookmark app";
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        return true;
    }


    //Show exit dialog on backpress//
    @Override
    public void onBackPressed(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this, R.style.RoundDialog);
        View mView = getLayoutInflater().inflate(R.layout.layout_dialog_quit_app, null);

        mBuilder.setView(mView);
        final AlertDialog dialog_exit = mBuilder.create();
        dialog_exit.show();

        button_logout_no = (Button) mView.findViewById(R.id.no_button);
        button_logout_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_exit.dismiss();
            }
        });

        button_logout_yes = (Button) mView.findViewById(R.id.yes_button);
        button_logout_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.exit(0);
            }
        });
    }



}
