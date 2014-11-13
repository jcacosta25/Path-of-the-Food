package com.pathofthefood.flyingburger;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pathofthefood.flyingburger.utils.SessionManager;

import org.json.JSONException;


public class SplashScreen extends Activity {
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new PrefetchData().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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

   private class PrefetchData extends AsyncTask<Void, Void, Integer>{
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           session = new SessionManager(getApplicationContext());
       }

       @Override
       protected Integer doInBackground(Void... arg0) {

          if(session.isLoggedIn() && CONFIG.isOnline(getApplicationContext())){
              //Obtenemos info del usuario
              HttpClientHelp httpClientHelp = new HttpClientHelp();
              try {
                  httpClientHelp.user_info(CONFIG.SERVER_URL, session.getUserDetails().getApi_token());
              } catch (JSONException e) {
                  return CONFIG.ERROR_JSON;
              } catch (NotAuthException e) {
                  return CONFIG.ERROR_NOT_AUTH;
              }

              return CONFIG.DONE;
          }else if(session.isLoggedIn() && !CONFIG.isOnline(getApplicationContext())){
              return CONFIG.DONE;
          }
           return CONFIG.ERROR_NOT_AUTH;
       }

       @Override
       protected void onPostExecute(Integer result) {
           super.onPostExecute(result);

           switch (result){
               case CONFIG.DONE:
                   startActivity(new Intent(getApplicationContext(), Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK));
                   finish();
                   break;
               case CONFIG.ERROR_NOT_AUTH:
                   startActivity(new Intent(getApplicationContext(), Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK));
                   finish();
                   break;
               default:
                   startActivity(new Intent(getApplicationContext(), Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK));
                   finish();
                   break;
           }
       }
    }

}