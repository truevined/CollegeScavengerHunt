/*
package tonyebrown.whoowesme.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuIcon;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

import tonyebrown.whoowesme.R;

public class WhoOwesMeActivity extends AppCompatActivity {

    private MaterialMenuIcon materialMenu;
    private MaterialMenuIconToolbar materialMenuT;
    private int actionBarMenuState;

    protected static final int SUB_ACTIVITY_REQUEST_CODE = 100;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;

    private static final int PROFILE_SETTING = 1;

    */
/*save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private Crossfader crossFader;*//*


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_owes_me);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //setHamburgerMenu();
        handleToolbar();

        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);


        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        String[] drawerContent = new String[]{
                getString(R.string.menu_1),
                getString(R.string.menu_2),
                getString(R.string.menu_3),
                getString(R.string.menu_4),
                "Stop Animation (Back icon)",
                "Stop Animation (Home icon)",
                "Start Animation",
                "Change Color",
                "GitHub Page",
                "Share",
                "Rate"
        };

        handleDrawer(drawerContent);

        FloatingActionButton money = (FloatingActionButton)findViewById(R.id.money);
        money.setOnClickListener(handleFAB);


    }

    private void handleDrawer(String[] values) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        mDrawerToggle.setAnimateEnabled(false);
                        drawerArrow.setProgress(1f);
                        break;
                    case 1:
                        mDrawerToggle.setAnimateEnabled(false);
                        drawerArrow.setProgress(0f);
                        break;
                    case 2:
                        mDrawerToggle.setAnimateEnabled(true);
                        mDrawerToggle.syncState();
                        break;
                    case 3:
                        if (drawerArrowColor) {
                            drawerArrowColor = false;
                            drawerArrow.setColor(R.color.ldrawer_color);
                        } else {
                            drawerArrowColor = true;
                            drawerArrow.setColor(R.color.drawer_arrow_second_color);
                        }
                        mDrawerToggle.syncState();
                        break;
                    case 4:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/IkiMuhendis/LDrawer"));
                        startActivity(browserIntent);
                        break;
                    case 5:
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        share.putExtra(Intent.EXTRA_SUBJECT,
                                getString(R.string.app_name));
                        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.splash_author) + "\n" +
                                "GitHub Page :  https://github.com/IkiMuhendis/LDrawer\n" +
                                "Sample App : https://play.google.com/store/apps/details?id=" +
                                getPackageName());
                        startActivity(Intent.createChooser(share,
                                getString(R.string.app_name)));
                        break;
                    case 6:
                        String appUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();
                        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl));
                        startActivity(rateIntent);
                        break;
                }

            }
        });
    }

    private View.OnClickListener handleFAB = new View.OnClickListener() {
        public void onClick(View current) {

            //LinearLayout menuSelection = (LinearLayout) current; //for textviews

            if(current.getId() == R.id.money){
                Intent i = new Intent(WhoOwesMeActivity.this, AddToDatabaseActivity.class);
                startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);
            }

        }
    };

    private void handleToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarWOM);
        // materialMenu = new MaterialMenuIcon(this, Color.WHITE, MaterialMenuDrawable.Stroke.REGULAR);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle your drawable state here
                    materialMenu.animateState(MaterialMenuDrawable.IconState.BURGER);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
*/
