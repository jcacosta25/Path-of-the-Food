package com.pathofthefood.flyingburger.Address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.pathofthefood.flyingburger.*;
import com.pathofthefood.flyingburger.utils.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Juan Acosta on 11/10/2014.
 */
public class AddressAdapter extends BaseAdapter {
    View convertView;
    private Context context;
    private ArrayList<Address> address_list;

    public AddressAdapter(Context context, ArrayList<Address> address_list) {
        this.context = context;
        this.address_list = address_list;
    }

    @Override
    public int getCount() {
        return this.address_list.size();
    }

    public AddressAdapter getInstance() {
        return this;
    }

    @Override
    public Object getItem(int position) {
        return this.address_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.address, null);
        }

        TextView TVAN = (TextView) convertView.findViewById(R.id.TextViewAddresName);
        TextView TVAD = (TextView) convertView.findViewById(R.id.textViewDescriptionAddres);
        TextView TVTA = (TextView) convertView.findViewById(R.id.textViewTextAddres);
        ImageButton delete = (ImageButton) convertView.findViewById(R.id.deleteaddress);
        TVAN.setText(address_list.get(position).getLabel());
        Log.d("LABEL", address_list.get(position).getLabel());
        TVAD.setText(address_list.get(position).getDescription());
        TVTA.setText(address_list.get(position).getTextaddress());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager session = new SessionManager(context);
                new AddressDeleteTask(context, session.getUserDetails().getApi_token(), address_list.get(position).getId(), position).execute();


            }
        });
        return convertView;
    }

    class AddressDeleteTask extends AsyncTask<String, Void, Integer> {

        int position;
        private Context context;
        private ArrayList<Address> address;
        private SessionManager session;
        private String api, id;

        public AddressDeleteTask(Context context, String api, String id, int position) {
            this.context = context;
            this.api = api;
            this.id = id;
            this.position = position;
        }

        @Override
        protected Integer doInBackground(String... params) {
            session = new SessionManager(this.context);
            JSONObject jsonObject;
            try {
                if (session.isLoggedIn() && CONFIG.isOnline(this.context)) {
                    HttpClientHelp clienteHttp = new HttpClientHelp();
                    jsonObject = clienteHttp.delete_address(CONFIG.SERVER_URL, api, id);
                    if (jsonObject == null || jsonObject.length() == 0) {
                        return CONFIG.ERROR_NULL;
                    }
                    return CONFIG.DONE;
                } else if (session.isLoggedIn() && !CONFIG.isOnline(this.context)) {
                    return CONFIG.DONE;
                }
                return CONFIG.ERROR_NOT_AUTH;
            } catch (JSONException e) {
                return CONFIG.ERROR_JSON;
            } catch (NotAuthException e) {
                return CONFIG.ERROR_NOT_AUTH;
            } catch (Exception ex) {
                return CONFIG.ERROR_JSON;

            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case CONFIG.DONE:
                    Log.d("AddressTask", "Entro onPostExecute");
                    address_list.remove(position);
                    getInstance().notifyDataSetChanged();
                    break;
                case CONFIG.ERROR_NOT_AUTH:
                    this.context.startActivity(new Intent(this.context, Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    break;
                default:
                    this.context.startActivity(new Intent(this.context, Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    break;
            }


        }
    }
}
