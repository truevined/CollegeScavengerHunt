package tonyebrown.whoowesme.activity;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import tonyebrown.whoowesme.R;

public class EditorExampleActivity extends AppCompatActivity {
    public static final String EDITOR_PARAM = "EDITOR_PARAM";
    public static final String TITLE_PARAM = "TITLE_PARAM";
    public static final String CONTENT_PARAM = "CONTENT_PARAM";
    public static final String DRAFT_PARAM = "DRAFT_PARAM";
    public static final String TITLE_PLACEHOLDER_PARAM = "TITLE_PLACEHOLDER_PARAM";
    public static final String CONTENT_PLACEHOLDER_PARAM = "CONTENT_PLACEHOLDER_PARAM";
    public static final int USE_NEW_EDITOR = 1;
    public static final int USE_LEGACY_EDITOR = 2;

    public static final int ADD_MEDIA_ACTIVITY_REQUEST_CODE = 1111;
    public static final int ADD_MEDIA_FAIL_ACTIVITY_REQUEST_CODE = 1112;

    public static final String MEDIA_REMOTE_ID_SAMPLE = "123";

    private static final int SELECT_PHOTO_MENU_POSITION = 0;
    private static final int SELECT_PHOTO_FAIL_MENU_POSITION = 1;

    private Map<String, String> mFailedUploads;

    LocationManager locationManager;
    LocationListener locationListener;
    TextView loctext01;
    String bestProvider;
    double lattitude;
    double longitude;

    DBAdapter myDb;

    Context context;

    public static final int  MY_PERMISSIONS_REQUEST_LOCATION = 3;

    //Spinner
    private ProgressBar spinner;

    //editing vars
    String savedRow, savedLocation, savedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rte_demo_2);

        context = this;
        openDB();

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        // TODO - put all kenBurnsView's into threads if possible

        // animated bg image start
        final KenBurnsView kbv = (KenBurnsView) findViewById(R.id.imageViewMenu);

        TypedArray images = getResources().obtainTypedArray(R.array.loading_images);
        int choice = (int) (Math.random() * images.length());
        kbv.setImageResource(images.getResourceId(choice, R.drawable.img1));
        images.recycle();
        //animated bg image end

        mFailedUploads = new HashMap<>();

        final Typeface font = Typeface.createFromAsset(getAssets(), "fonts/quicksand_light.otf");
        final Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/ikaros_reg.otf");

        TextView cancel = (TextView) findViewById(R.id.textView5);
        TextView save = (TextView) findViewById(R.id.textView2);

        TextView date = (TextView) findViewById(R.id.textView6);
        TextView dateContent = (TextView) findViewById(R.id.textView9);
        TextView location = (TextView) findViewById(R.id.textView8);
        TextView locationContent = (TextView) findViewById(R.id.textView7);

        TextView editorTitle = (TextView) findViewById(R.id.textView10);
        editorTitle.setTypeface(font);

        //cancel.setTypeface(font2);
        //save.setTypeface(font2);

        date.setTypeface(font);
        dateContent.setTypeface(font);
        location.setTypeface(font);
        locationContent.setTypeface(font);

        LinearLayout btnCancel = (LinearLayout) findViewById(R.id.CancelGroup);
        LinearLayout btnSave = (LinearLayout) findViewById(R.id.SaveNoteGroup);

        LinearLayout btnLocation = (LinearLayout) findViewById(R.id.LocationGroup);
        LinearLayout btnDate = (LinearLayout) findViewById(R.id.DateGroup);

        btnCancel.setOnClickListener(handleEditor);
        btnSave.setOnClickListener(handleEditor);
        btnLocation.setOnClickListener(handleEditor);
        btnDate.setOnClickListener(handleEditor);

        editorTitle.setOnClickListener(handleEditor2);

        //location stuff

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if ((ActivityCompat.shouldShowRequestPermissionRationale(EditorExampleActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        || (ActivityCompat.shouldShowRequestPermissionRationale(EditorExampleActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)))) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Log.d("DEBUG", "PERMISSION REQ  -> should Show Request Permission Rationale");
                    ActivityCompat.requestPermissions(EditorExampleActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }

                else {
                    Log.d("DEBUG", "PERMISSION REQ  -> No explanation needed");

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(EditorExampleActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }

                return;
            }
            else {
                //Toast.makeText(EditorExampleActivity.this, "Location Permission is granted!", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(EditorExampleActivity.this, "Location Permission not granted! Memora won't function properly!", Toast.LENGTH_SHORT).show();

        }

        //if (locationManager.getLastKnownLocation(bestProvider)!= null)


        //Location service
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        bestProvider =
                locationManager.getBestProvider(criteria, true);
        Log.d("Location", "1- Recommended Location provider is " + bestProvider);

        // 4. getLastKnownLocation
        Location LastKnownLocation = locationManager.getLastKnownLocation(bestProvider);

        UpdateLocation(LastKnownLocation, "Last Known Location");
        // 2. Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.d("Location", "2- A new location is found by the location provider ");
                UpdateLocation(location, "Location Changed");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


        //set date and time
        String  currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());
        dateContent.setText(currentDateTimeString);

        //set location
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lattitude, longitude, 1);
            if (addresses != null) {

                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }

                Log.d("TAG", "I am at: " + strAddress.toString());
                locationContent.setText(strAddress.toString());
            } else
                Log.d("TAG", "No location found..!");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not get address..!", Toast.LENGTH_LONG).show();
        }

        //set title text/randomize editor title text
        setEditorTitleText();

        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);


        //get existing date and location if editing note
        savedRow = getIntent().getStringExtra("savedRow");
        savedLocation = getIntent().getStringExtra("savedLocation");
        savedDate = getIntent().getStringExtra("savedDate");

        if (savedLocation != null)
            locationContent.setText(savedLocation);

        if (savedDate != null)
            dateContent.setText(savedDate);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        Log.d("DEBUG", "onRequestPermissionsResult -> requestCode = " + requestCode + ",permissions = " + permissions + "grantResults = " + grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("DEBUG", "PERMISSION REQ  -> requestCode + ->*** ACCESS_FINE/CORSE_LOCATION -> PERMISSION_GRANTED ****");

                } else {
                    Log.d("DEBUG", "PERMISSION REQ  -> ACCESS DENIED");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                return;

            default:


                // other 'case' lines to check for other
                // permissions this app might request
        }
    }


    public void setEditorTitleText() {
        String[] titles = getResources().getStringArray(R.array.editor_title_array);

        TextView textView = (TextView) findViewById(R.id.textView10);
        Random r = new Random();
        int i = (r.nextInt(10) + 1);
        textView.setText(titles[i]);
    }

    public void UpdateLocation(Location location, String state) {
        TextView tvLocation = (TextView) findViewById(R.id.textView7);
        if (location!=null){

            lattitude = location.getLatitude();
            longitude = location.getLongitude();

            //set location
            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
            try {
                List<Address> addresses = geocoder.getFromLocation(lattitude, longitude, 1);
                if (addresses != null) {

                    Address fetchedAddress = addresses.get(0);
                    StringBuilder strAddress = new StringBuilder();

                    for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                        strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                    }

                    Log.d("TAG", "I am at: " + strAddress.toString());
                    tvLocation.setText(strAddress.toString());
                } else
                    Log.d("TAG", "No location found..!");

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not get address..!", Toast.LENGTH_LONG).show();
            }
            //Log.d("Location", "** " + state + " ** - Lattitude = " + lattitude + ", and Longitude = " + longitude);
            //tvLocation.setText(state + " " + lattitude + " " + longitude);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
    }

    private View.OnClickListener handleEditor = new View.OnClickListener() {
        public void onClick(View current) {

            LinearLayout menuSelection = (LinearLayout) current; //for TextViews

            if (menuSelection.getId() == R.id.SaveNoteGroup) {
                spinner.setVisibility(View.VISIBLE);
                spinner.bringToFront();
                saveNote();
            }

            if (menuSelection.getId() == R.id.CancelGroup) {
                spinner.setVisibility(View.VISIBLE);
                spinner.bringToFront();
                discardNote();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
            }

            if (menuSelection.getId() == R.id.DateGroup) {
                spinner.setVisibility(View.VISIBLE);
                spinner.bringToFront();
                TextView dateContent = (TextView) findViewById(R.id.textView9);
                String  currentDateTimeString = DateFormat.getDateTimeInstance()
                        .format(new Date());
                dateContent.setText(currentDateTimeString);
            }

            if (menuSelection.getId() == R.id.LocationGroup) {
                spinner.setVisibility(View.VISIBLE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location LastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
                UpdateLocation(LastKnownLocation, "Last Known Location");
            }
        }
    };

    private View.OnClickListener handleEditor2 = new View.OnClickListener() {
        public void onClick(View current) {

            TextView menuSelection = (TextView) current; //for TextViews

            if(menuSelection.getId() == R.id.textView10){
                setEditorTitleText();
                spinner.setVisibility(View.VISIBLE);
                spinner.bringToFront();
            }
        }
    };

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        closeDB();
        finish();
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
    private void closeDB() {
        if (null != myDb)
            myDb.close();
        //myDb.close();
        finish();
    }

    public void onClick_AddRecord() {
        //public void onClick_AddRecord(View v) {
        //displayText("Clicked add record!");

        EditText eTitle = (EditText) findViewById(R.id.tvNoteTitle);
        TextView eLocation = (TextView) findViewById(R.id.textView7);
        TextView eDate = (TextView) findViewById(R.id.textView9);

        Date cDate = new Date();
        //String DATE = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        String DATE = eDate.getText().toString();

        //eDate.setText(DATE);

        if (!(eTitle.toString().isEmpty())) {
            long newId = myDb.insertRow(eTitle.getText().toString(), "".toString(), eDate.toString(),
                    eLocation.getText().toString());
            SendJSON send = new SendJSON();
            send.execute(eTitle.getText().toString(), "".toString(), eDate.toString(), eLocation.getText().toString());

            // Query for the record we just added.
            // Use the ID:
            Cursor cursor = myDb.getRow(newId);

            //displayRecordSet(cursor);

            if (EditorExampleActivity.TITLE_PLACEHOLDER_PARAM.toString() != getString(R.string.example_post_title_placeholder))
            {
                deleteOldRecordSet(cursor);//delete the old record if updating note
            }

            Toast.makeText(EditorExampleActivity.this, "Note Saved!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(EditorExampleActivity.this, "You cannot save an empty note!", Toast.LENGTH_SHORT).show();
        }



    }

    // Display an entire recordset to the screen.
    private void displayRecordSet(Cursor cursor) {
        String message = "";
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String title = cursor.getString(DBAdapter.COL_TITLE);
                String body = cursor.getString(DBAdapter.COL_BODY);
                String date = cursor.getString(DBAdapter.COL_DATE);
                String location = cursor.getString(DBAdapter.COL_LOCATION);


                // Append data to the message:
                message += "id=" + id
                        +", title=" + title
                        +", body=" + body
                        +", date=" + date
                        +", location=" + location
                        +"\n";
            } while(cursor.moveToNext());
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();

        Toast.makeText(EditorExampleActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    // Display an entire recordset to the screen.
    private void deleteOldRecordSet(Cursor cursor) {
        String message = "";
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                myDb.deleteRow(id);
            } while(cursor.moveToNext());
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();

        Toast.makeText(EditorExampleActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, SELECT_PHOTO_MENU_POSITION, 0, getString(R.string.select_photo));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_PICK);

        switch (item.getItemId()) {
            case SELECT_PHOTO_MENU_POSITION:
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent = Intent.createChooser(intent, getString(R.string.select_photo));

                startActivityForResult(intent, ADD_MEDIA_ACTIVITY_REQUEST_CODE);
                return true;
            case SELECT_PHOTO_FAIL_MENU_POSITION:
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent = Intent.createChooser(intent, getString(R.string.select_photo_fail));

                startActivityForResult(intent, ADD_MEDIA_FAIL_ACTIVITY_REQUEST_CODE);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
    }

    public void saveNote () {
        onClick_AddRecord();
        Intent i = new Intent(EditorExampleActivity.this, NotesListActivity.class);
        startActivity(i);
        this.finish();
    }

    public void discardNote() {
        this.finish();
    }

    /**
     * Displays a dialog asking the user to confirm that they want to exit, discarding unsaved changes.
     */
    private void showDiscardChangesDialog(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyAlertDialogStyle);
            builder.setTitle("Save Changes?");
        builder.setMessage("You have unsaved changes in this note! \nExit without saving?");

        builder.setPositiveButton(getString(R.string.editor_save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                    saveNote();
                    //getFragmentManager().popBackStack();
                }
            });

            builder.setNegativeButton(getString(R.string.editor_discard), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    discardNote();
                }
            }).create();
            builder.create();
            builder.show();
        //}
    }

    public void onClick_ClearAll(View v) {
        Toast.makeText(EditorExampleActivity.this, "Cleared all data from database", Toast.LENGTH_SHORT).show();
        myDb.deleteAll();
    }

    public void onClick_DisplayRecords(View v) {
        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);
    }
}
