package tonyebrown.whoowesme.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import tonyebrown.whoowesme.R;

/**
 * Created by Tonye Brown on 11/20/2015.
 */
public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = (ImageView) findViewById(R.id.imgLogo);
        final TextView credits = (TextView) findViewById(R.id.textViewSplashHeader);
        final TextView credits3 = (TextView) findViewById(R.id.textViewSplashHeader2);
        //final TextView credits2 = (TextView) findViewById(R.id.textViewSplashFooter);
        final FrameLayout splashParent = (FrameLayout) findViewById(R.id.FrameLayoutSplash);

        /*TypedArray images = getResources().obtainTypedArray(R.array.loading_images);
        int choice = (int) (Math.random() * images.length());
        splashParent.setBackgroundResource(images.getResourceId(choice, R.drawable.img1));
        images.recycle();*/

        // set font for all displays
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/quicksand_light.otf");
        credits.setTypeface(font);
        credits3.setTypeface(font);
        //credits2.setTypeface(font);

        YoYo.with(Techniques.SlideInLeft)
                .duration(500)
                .playOn(credits);
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);


    }

}


