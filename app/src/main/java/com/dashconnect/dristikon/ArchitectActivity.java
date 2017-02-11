package com.dashconnect.dristikon;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;


import java.io.IOException;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class ArchitectActivity extends AppCompatActivity {
    private static int TIME_OUT = 10000;
    /**
     * holds the Wikitude SDK AR-View, this is where camera, markers, compass, 3D models etc. are rendered
     */
    private ArchitectView	architectView;


    /** Called when the activity is first created. */
    @Override
    public void onCreate( final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_architect);

        ArchitectView.isDeviceSupported(this);
        this.architectView = (ArchitectView)this.findViewById( R.id.architectView  );

		/* pass SDK key if you have one, this one is only valid for this package identifier and must not be used somewhere else */
        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setLicenseKey(getResources().getString(R.string.liscense_key));

        try {
			/* first mandatory life-cycle notification */
            this.architectView.onCreate( config );
        } catch (RuntimeException rex) {
            this.architectView = null;
            Toast.makeText(getApplicationContext(), "can't create Architect View", Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getName(), "Exception in ArchitectView.onCreate()", rex);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(ArchitectActivity.this, MapsActivity.class);

                        i.putExtra("clue","Clue was found!");

                startActivity(i);
                finish();
            }
        }, TIME_OUT);
    }



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        architectView.onPostCreate();

        try {
            this.architectView.load( "demo/index.html" );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        architectView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        architectView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        architectView.onPause();
    }


}
