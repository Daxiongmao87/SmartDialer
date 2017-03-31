package smartdialer.bitjunkie.com.smartdialer;

        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {
    TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost host = (TabHost)findViewById(R.id.tabhost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Dialer");
        spec.setContent(R.id.Dialer);
        spec.setIndicator("Dialer");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.Log);
        spec.setIndicator("Tab Two");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.Contacts);
        spec.setIndicator("Tab Three");
        host.addTab(spec);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
         //   return true;
        //}

        return super.onOptionsItemSelected(item);
    }
}