package tonyebrown.whoowesme.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import tonyebrown.whoowesme.R;
import tonyebrown.whoowesme.databases.DBDebts;

public class WhoOwesMeActivity2 extends AppCompatActivity {

    // Get a Realm instance for this thread
    Realm realm;

    protected static final int SUB_ACTIVITY_REQUEST_CODE = 100;
    String toolbarCurrentTitle;

    com.getbase.floatingactionbutton.FloatingActionButton money;
    com.getbase.floatingactionbutton.FloatingActionButton book;
    com.getbase.floatingactionbutton.FloatingActionButton clothing;
    com.getbase.floatingactionbutton.FloatingActionButton other;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_owes_me2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarCurrentTitle = getIntent().getStringExtra("menuChoice");
        Log.d("***DEBUG****", "Intent=" + toolbarCurrentTitle);
        setToolbarTitle(toolbar);//choose toolbar name based on what menu item was clicked

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //animated bg image start
        final ImageView kbv = (ImageView) findViewById(R.id.imageViewDrawer);
        TypedArray images = getResources().obtainTypedArray(R.array.loading_images);
        int choice = (int) (Math.random() * images.length());
        kbv.setImageResource(images.getResourceId(choice, R.drawable.img1));
        images.recycle();
        //animated bg image end

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        money = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.money);
        book = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.book);
        clothing = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.clothing);
        other = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.other);

        money.setOnClickListener(handleFAB);
        book.setOnClickListener(handleFAB);
        clothing.setOnClickListener(handleFAB);
        other.setOnClickListener(handleFAB);

        //recycle view
        whoOwesMeRecycler(toolbarCurrentTitle);
    }


    /*
    * This method refreshes data from the database whenever it is called
    *
    * variable thisMenu holds which part of the database to refresh.
    * */
    ArrayList<String> miniDebtList;

    private void whoOwesMeRecycler(String thisMenu) {
        miniDebtList = new ArrayList<String>();
        ListView lsWOM = (ListView) findViewById(R.id.listViewWOM);

        switch (thisMenu) {
            case "1": {
                // Query and use the result in another thread
                Thread thread = new Thread(new Runnable() {
                    String name, memo;
                    @Override
                    public void run() {
                        // Get a Realm instance for this thread
                        Realm realm = Realm.getInstance(getApplicationContext());

                        // Build the query looking at all players:
                        RealmResults<DBDebts> result = realm.where(DBDebts.class)
                                .equalTo("menu", 1)
                                .findAll();

                        name = result.get(result.size()-1).getName();
                        memo = result.get(result.size()-1).getDebtMemo();

                        buildMiniDebtList(name, memo);


                        Log.v("TAG DB", "Player 1: " +name);
                        Log.v("TAG DB", "Player 2: " + memo);

                        realm.close();
                        //thread.start();//gets the name of the player playing android
                    }
                });


                break;
            }

            case "2": {

                // Query and use the result in another thread
                Thread thread = new Thread(new Runnable() {
                    String name, memo;
                    @Override
                    public void run() {
                        // Get a Realm instance for this thread
                        Realm realm = Realm.getInstance(getApplicationContext());

                        // Build the query looking at all players:
                        RealmResults<DBDebts> result = realm.where(DBDebts.class)
                                .equalTo("menu", 2)
                                .findAll();

                        name = result.get(result.size()-1).getName();
                        memo = result.get(result.size()-1).getDebtMemo();

                        buildMiniDebtList(name, memo);


                        Log.v("TAG DB", "Player 1: " +name);
                        Log.v("TAG DB", "Player 2: " + memo);

                        realm.close();
                        //thread.start();//gets the name of the player playing android
                    }
                });

                break;
            }

            case "3": {
                // Query and use the result in another thread
                Thread thread = new Thread(new Runnable() {
                    String name, memo;
                    @Override
                    public void run() {
                        // Get a Realm instance for this thread
                        Realm realm = Realm.getInstance(getApplicationContext());

                        // Build the query looking at all players:
                        RealmResults<DBDebts> result = realm.where(DBDebts.class)
                                .equalTo("menu", 3)
                                .findAll();

                        name = result.get(result.size()-1).getName();
                        memo = result.get(result.size()-1).getDebtMemo();

                        buildMiniDebtList(name, memo);


                        Log.v("TAG DB", "Player 1: " +name);
                        Log.v("TAG DB", "Player 2: " + memo);

                        realm.close();
                        //thread.start();//gets the name of the player playing android
                    }
                });


                break;
            }

            default:
                Toast.makeText(WhoOwesMeActivity2.this,
                        "This toast message from WhoOwesMeActivity2/whoOwesMeRecycler" +
                                " should never show", Toast.LENGTH_SHORT).show();

        }

        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, miniDebtList);
        // Set The Adapter
        lsWOM.setAdapter(arrayAdapter);

    }

    private void buildMiniDebtList(String name, String memo) {
        miniDebtList.add(name +"\n" +memo);
    }

    private View.OnClickListener handleFAB = new View.OnClickListener() {
        public void onClick(View current) {

            //LinearLayout menuSelection = (LinearLayout) current; //for textviews

            FloatingActionsMenu fab = (FloatingActionsMenu) findViewById(R.id.fab_bottom_right);

            if(current.getId() == R.id.money){
                Intent i = new Intent(WhoOwesMeActivity2.this, AddToDatabaseActivity.class);
                i.putExtra("menuChoice", "1");
                fab.collapse();
                startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);
            }

            else if(current.getId() == R.id.book){
                Intent i = new Intent(WhoOwesMeActivity2.this, AddToDatabaseActivity.class);
                i.putExtra("menuChoice", "2");
                fab.collapse();
                startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);
            }

            else if(current.getId() == R.id.clothing){
                Intent i = new Intent(WhoOwesMeActivity2.this, AddToDatabaseActivity.class);
                i.putExtra("menuChoice","3");
                fab.collapse();
                startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);
            }

            else if(current.getId() == R.id.other){
                Intent i = new Intent(WhoOwesMeActivity2.this, AddToDatabaseActivity.class);
                i.putExtra("menuChoice","4");
                fab.collapse();
                startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);
            }

        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.who_owes_me_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    //@Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.wom_menu1) { // Handle the who owes me action
            /*Intent i = new Intent(WhoOwesMeActivity2.this, WhoOwesMeActivity2.class);
            startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);
            finish();*/
            setToolbarTitle(toolbar);
            whoOwesMeRecycler("1");
        }
        else if (id == R.id.wom_menu2) { // Handle the who I owe action
            /*Intent i = new Intent(WhoOwesMeActivity2.this, WhoIOweActivity.class);
            startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);*/
            setToolbarTitle(toolbar);
            whoOwesMeRecycler("2");
        }
        else if (id == R.id.wom_menu3) { // Handle the history action
            /*Intent i = new Intent(WhoOwesMeActivity2.this, HistoryActivity.class);
            startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);*/
            setToolbarTitle(toolbar);
            whoOwesMeRecycler("3");
        }
        else if (id == R.id.wom_menu4) { // Handle the Share action
            //share link to playstore download page
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tonyebrown.com"));
            startActivity(browserIntent);
        }
        else if (id == R.id.wom_menu5) { // Handle the Rate us action
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tonyebrown.com"));
            startActivity(browserIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setToolbarTitle(Toolbar toolbar) {
        switch (toolbarCurrentTitle) {
            case "1": { setTitle("Who Owes Me"); break; }
            case "2": { setTitle("Who I Owe"); break; }
            case "3": { setTitle("History"); break; }
            case "4": { setTitle("Settings"); break; }
            default: {
                Toast.makeText(WhoOwesMeActivity2.this,
                        "This toast message from WhoOwesMeActivity/setToolbarTitle() " +
                                "should never show", Toast.LENGTH_SHORT).show();
                break;
            }

        }
    }
}
