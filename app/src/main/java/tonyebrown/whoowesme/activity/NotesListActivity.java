package tonyebrown.whoowesme.activity;

/**
 * Created by Tonye Brown on 1/26/2016.
 */
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;

import tonyebrown.whoowesme.R;

public class NotesListActivity extends AppCompatActivity {
    ListView listview;
    TextView empty;
    String selected;
    List<String> list;
    String[] filenames;

    String row;// = cursor.getString(DBAdapter.COL_TITLE);
    String title;// = cursor.getString(DBAdapter.COL_TITLE);
    String body;// = cursor.getString(DBAdapter.COL_BODY);
    String date;// = cursor.getString(DBAdapter.COL_DATE);
    String location;// = cursor.getString(DBAdapter.COL_LOCATION);

    DBAdapter myDb;

    //Spinner
    private ProgressBar spinner;

    @Override
    protected void onResume() {
        super.onResume();

        spinner.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        // animated bg image start

        final KenBurnsView kbv = (KenBurnsView) findViewById(R.id.imageViewMenu);

        TypedArray images = getResources().obtainTypedArray(R.array.loading_images);
        int choice = (int) (Math.random() * images.length());
        kbv.setImageResource(images.getResourceId(choice, R.drawable.img1));
        images.recycle();
        //animated bg image end

        //get spinner
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        listview = (ListView) findViewById(R.id.dblist);
        openDB();
        listviewitems();
    }

    private void listviewitems() {
        // TODO Auto-generated method stub

        Cursor cursor = myDb.getAllRows();

        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:

        // Process the data:
        List<String> values = new ArrayList<String>();

        if (cursor != null && cursor.moveToFirst()){
            do {
            row = cursor.getString(DBAdapter.COL_ROWID);
            title = cursor.getString(DBAdapter.COL_TITLE);
            body = cursor.getString(DBAdapter.COL_BODY);
            date = cursor.getString(DBAdapter.COL_DATE);
            location = cursor.getString(DBAdapter.COL_LOCATION);

            if (body == null)
            { body = ""; }

            else if (body.length() < 20){
                body = body;
            }

            else {
                body.replaceAll("(?<=.{20})\\b.*", "..."); // How easy was that!? :)
            }

                if (body.isEmpty()) {
                    body = "[ Empty Note ]";
                }

                if (title.isEmpty()) {
                    title = "[ Untitled Note ]";
                }

            //make title bold
            String tempTitle = "<b>" + title + "</b> ";

            //mytextview.setText(Html.fromHtml(tempTitle));

            values.add(title + "\n" + body + "\nLast Modified: " + date +" at: " +location);

            } while(cursor.moveToNext());

            /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.activity_notes_list, R.id.dblist, values);*/

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simplerow,
                    R.id.rowTextView, values);

            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    spinner.setVisibility(View.VISIBLE);
                    spinner.bringToFront();
                    /*String selectedNote = listview.getItemAtPosition(position);
                    inputSearch.setText(selectedProduct);*/
                    //Toast.makeText(NotesListActivity.this, "Item clicked. Open in editor activity", Toast.LENGTH_SHORT).show();

                    String result = "";

                    Intent intent = new Intent(NotesListActivity.this, EditorExampleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(EditorExampleActivity.TITLE_PARAM, title);
                    bundle.putString(EditorExampleActivity.CONTENT_PARAM, body);
                    bundle.putInt(EditorExampleActivity.EDITOR_PARAM, EditorExampleActivity.USE_NEW_EDITOR);

                    bundle.putString("savedRow", row);
                    bundle.putString("savedLocation", location);
                    bundle.putString("savedDate", date);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
        
        else {
            Toast.makeText(NotesListActivity.this, "You haven't created any notes yet!", Toast.LENGTH_SHORT).show();
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();
    }

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

}