package tonyebrown.whoowesme.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuIcon;
import com.balysv.materialmenu.MaterialMenuView;

import com.balysv.materialmenu.MaterialMenuDrawable.Stroke;

import static com.balysv.materialmenu.MaterialMenuDrawable.IconState;


import io.realm.Realm;
import tonyebrown.whoowesme.R;
import tonyebrown.whoowesme.databases.DBDebts;

public class AddToDatabaseActivity extends AppCompatActivity {
    private MaterialMenuIcon materialMenu;
    private int actionBarMenuState;
    // Get a Realm instance for this thread
    Realm realm;
    public final static int SUCCESS_RETURN_CODE = 1;

    EditText name, memo;
    Button dSave;
    String fabClicked;//current floating action button clicked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_database);

        //get which menu item was pressed from intent
        fabClicked = getIntent().getStringExtra("menuChoice");
        Log.d("***DEBUG****", "Intent=" + fabClicked);

        realm = Realm.getInstance(this);
        dSave = (Button) findViewById(R.id.buttonSaveDebt);

        dSave.setOnClickListener(handleSave);
    }

    private View.OnClickListener handleSave = new View.OnClickListener() {

        @Override
        public void onClick(View current) {
            Intent intent = new Intent();
            Bundle a = new Bundle();
            a.putString("menuChoice", fabClicked);

            if (current.getId() == R.id.buttonSaveDebt) {

                name = (EditText) findViewById(R.id.EditTextName);
                memo = (EditText) findViewById(R.id.EditTextMemo);

                DBDebts debt = new DBDebts();

                debt.setName(name.getText().toString());
                debt.setDebtMemo(memo.getText().toString());
                debt.setMenu(fabClicked);

                Log.v("TAG DB", "Name: " + debt.getName());
                Log.v("TAG DB", "Memo: " + debt.getDebtMemo());

                realm.beginTransaction();
                realm.copyToRealm(debt);
                realm.commitTransaction();

                Toast.makeText(AddToDatabaseActivity.this, "Debt Saved Successfully!", Toast.LENGTH_SHORT).show();
            }

            intent.putExtras(a);
            setResult(SUCCESS_RETURN_CODE, intent);
            finish();
            finishActivity(1);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.embedded, menu);
        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
