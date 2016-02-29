package tonyebrown.whoowesme.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import java.util.List;
import java.util.Locale;

import tonyebrown.whoowesme.R;


public class MainActivity extends AppCompatActivity {
    //private static final long RIPPLE_DURATION = 150; //actually controls hamburger menu response time when clicked
    protected static final int SUB_ACTIVITY_REQUEST_CODE = 100;

    //Interpolator test = new Interpolator(1,1);
    RandomTransitionGenerator generator = new RandomTransitionGenerator();

    //Spinner
    private ProgressBar spinner;


    @Override
    protected void onPause() {
        super.onPause();

       /* //animated bg image start
        final KenBurnsView kbv = (KenBurnsView) findViewById(R.id.imageViewMenu);
        kbv.pause();*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        spinner.setVisibility(View.GONE);

        /*//animated bg image start
        final KenBurnsView kbv = (KenBurnsView) findViewById(R.id.imageViewMenu);
        TypedArray images = getResources().obtainTypedArray(R.array.loading_images);
        int choice = (int) (Math.random() * images.length());
        kbv.setImageResource(images.getResourceId(choice, R.drawable.img1));
        images.recycle();
        kbv.resume();*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new SimpleEula(this).show();//displays EULA

        // ButterKnife.inject(this);
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


        final Typeface font = Typeface.createFromAsset(getAssets(), "fonts/quicksand_light.otf");

        TextView cvWhoOwesMe = (TextView) findViewById (R.id.cvWhoOwesMe);
        //TextView cvWhoIOwe = (TextView) findViewById (R.id.cvWhoIOwe);
        TextView cvHistory = (TextView) findViewById (R.id.cvHistory);
        TextView cvCalendar = (TextView) findViewById (R.id.cvCalendar);
        TextView cvSettings  = (TextView) findViewById (R.id.cvSettings);

        TextView compose1  = (TextView) findViewById (R.id.tvComposeNewNote);
        TextView compose2  = (TextView) findViewById (R.id.tvComposeNewNote2);

        cvWhoOwesMe.setTypeface(font);
        //cvWhoIOwe.setTypeface(font);
        cvHistory.setTypeface(font);
        cvSettings.setTypeface(font);
        cvCalendar.setTypeface(font);

        compose1.setTypeface(font);
        compose2.setTypeface(font);

        LinearLayout btnWOM = (LinearLayout) findViewById (R.id.typeNote_group);
        //LinearLayout btnWIO = (LinearLayout) findViewById (R.id.dictate_group);
        LinearLayout btnH = (LinearLayout) findViewById (R.id.history_group);
        LinearLayout btnS  = (LinearLayout) findViewById (R.id.settings_group);
        LinearLayout btnCal  = (LinearLayout) findViewById (R.id.calendar_group);

        btnWOM.setOnClickListener(handleMenu);
        //btnWIO.setOnClickListener(handleMenu);
        btnH.setOnClickListener(handleMenu);
        btnS.setOnClickListener(handleMenu);
        btnCal.setOnClickListener(handleMenu);

        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
    }

    private View.OnClickListener handleMenu = new View.OnClickListener() {
        public void onClick(View current) {

            LinearLayout menuSelection = (LinearLayout) current; //for TextViews

            if(menuSelection.getId() == R.id.typeNote_group){
                menuChoice(1);
                spinner.setVisibility(View.VISIBLE);
                spinner.bringToFront();
            }

            /*if(menuSelection.getId() == R.id.dictate_group){
                menuChoice(2);
                spinner.setVisibility(View.VISIBLE);
                spinner.bringToFront();
            }*/

            if(menuSelection.getId() == R.id.history_group){
                menuChoice(3);
                spinner.setVisibility(View.VISIBLE);
                spinner.bringToFront();

            }

            if(menuSelection.getId() == R.id.calendar_group){
                menuChoice(4);
                spinner.setVisibility(View.VISIBLE);
                spinner.bringToFront();
            }

            if(menuSelection.getId() == R.id.settings_group){
                menuChoice(5);
                //spinner.setVisibility(View.VISIBLE);
            }

        }
    };

    public static int TTS_DATA_CHECK = 1;
    public static int VOICE_RECOGNITION = 2;
    private TextToSpeech tts, ttsagain;
    private boolean ttsIsInit, ttsagainIsInit = false;
    public String demotext = "This is a test of the text-to-speech engine in Android.";

    private void dictate(View v) {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What do you want to make a new note about?");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(10000));
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
            startActivityForResult(intent, VOICE_RECOGNITION);
        } catch (ActivityNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TTS_DATA_CHECK) {


        } else if (requestCode == VOICE_RECOGNITION) {
            Log.i("SpeechDemo", "## INFO 02: RequestCode VOICE_RECOGNITION = " + requestCode);
            if (resultCode == RESULT_OK) {
                String result = "";

                List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                float[] confidence = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
                for (int i = 0; i < results.size(); i++) {
                    result = results.get(i);
                    Log.i("SpeechDemo", "## INFO 05: Result: " + result + " (confidence: " + confidence[i] + ")");
                }

                Intent intent = new Intent(MainActivity.this, EditorExampleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(EditorExampleActivity.TITLE_PARAM, "");
                bundle.putString(EditorExampleActivity.CONTENT_PARAM, result);
                bundle.putString(EditorExampleActivity.TITLE_PLACEHOLDER_PARAM,
                        getString(R.string.example_post_title_placeholder));
                bundle.putString(EditorExampleActivity.CONTENT_PLACEHOLDER_PARAM, "Type note here");
                bundle.putInt(EditorExampleActivity.EDITOR_PARAM, EditorExampleActivity.USE_NEW_EDITOR);
                intent.putExtras(bundle);
                startActivity(intent);

            }

            else {



            }
        }
    }

    private void menuChoice(int choice) {
        switch (choice) {
            case 1: {
                menuHelper(choice);
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                break;
            }

            case 2: {
                menuHelper(choice);
                dictate(getCurrentFocus());
                spinner.setVisibility(View.VISIBLE);
                break;
            }

            case 3: {
                menuHelper(choice);
                Intent i = new Intent(MainActivity.this, NotesListActivity.class);
                i.putExtra("menuChoice","3");
                startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);
                break;
            }

            case 4: {
                /*menuHelper(choice);
                Toast.makeText(MainActivity.this, "Notebooks coming soon!!", Toast.LENGTH_SHORT).show();*/
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                i.putExtra("menuChoice","3");
                startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);
                break;
            }

            case 5: {
                menuHelper(choice);
                Toast.makeText(MainActivity.this, "Settings coming soon!!", Toast.LENGTH_SHORT).show();
                break;
            }

            default: {
                Toast.makeText(MainActivity.this, "This toast in MenuChoice() should never show", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void menuHelper(int thisOne) {
        /*TextView one = (TextView) findViewById(R.id.cvWhoOwesMe);
        TextView two = (TextView) findViewById(R.id.cvWhoIOwe);
        TextView three = (TextView) findViewById(R.id.cvHistory);
        TextView four = (TextView) findViewById(R.id.cvSettings);
        TextView five = (TextView) findViewById(R.id.cvCalendar);

        int ColorSelected = Color.parseColor("#ffffff");
        int ColorNotSelected = Color.parseColor("#ffffff");

        switch (thisOne) {
            case 1: {
                one.setTextColor(ColorSelected);
                two.setTextColor(ColorNotSelected);
                three.setTextColor(ColorNotSelected);
                four.setTextColor(ColorNotSelected);
                break;
            }

            case 2: {
                two.setTextColor(ColorSelected);
                one.setTextColor(ColorNotSelected);
                three.setTextColor(ColorNotSelected);
                four.setTextColor(ColorNotSelected);
                break;
            }

            case 3: {
                three.setTextColor(ColorSelected);
                two.setTextColor(ColorNotSelected);
                one.setTextColor(ColorNotSelected);
                four.setTextColor(ColorNotSelected);
                break;
            }

            case 4: {
                four.setTextColor(ColorSelected);
                two.setTextColor(ColorNotSelected);
                three.setTextColor(ColorNotSelected);
                one.setTextColor(ColorNotSelected);
                break;
            }

            default: {
                Toast.makeText(MainActivity.this, "This toast in MenuHelper() should never show", Toast.LENGTH_SHORT).show();
                break;
            }
        }*/
    }

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
        spinner.setVisibility(View.GONE);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        /* SnackBar.make(findViewById(android.R.id.content).getRootView(), "Press BACK again to exit", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show(); */
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        if (ttsIsInit) {
            tts.stop();
            tts.shutdown();
        }


        if (ttsagainIsInit) {
            ttsagain.stop();
            ttsagain.shutdown();
        }
    }
}
