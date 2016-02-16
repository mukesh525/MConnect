package in.vmc.mcubeconnect.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.vmc.mcubeconnect.R;
import in.vmc.mcubeconnect.adapter.Sitedetailadapter;
import in.vmc.mcubeconnect.adapter.VisitAdapter;
import in.vmc.mcubeconnect.model.SiteData;
import in.vmc.mcubeconnect.model.VisitData;
import in.vmc.mcubeconnect.utils.JSONParser;
import in.vmc.mcubeconnect.utils.TAG;
import in.vmc.mcubeconnect.utils.Utils;

public class SiteDetail extends AppCompatActivity implements TAG {
    public static String siteId;
    public static String authkey;
    @InjectView(R.id.listView2)
    ListView listView;
    @InjectView(R.id.root)
    RelativeLayout mroot;
    @InjectView(R.id.desc)
    TextView tvdesc;
    ArrayList<SiteData> SiteData = new ArrayList<>();
    private Toolbar toolbar;
    private ProgressDialog pd;
    private Sitedetailadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_detail);
        ButterKnife.inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        siteId=getIntent().getExtras().getString("ID");
//        authkey=getIntent().getExtras().getString("authkey");
        GetSites();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SiteDetail.this, LocationDetail.class);
                intent.putExtra(BID, SiteData.get(position).getBid());
                intent.putExtra(BEACONID, SiteData.get(position).getId());
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void GetSites() {


        if (Utils.onlineStatus2(SiteDetail.this)) {
            new Getsites().execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetSites();

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(SiteDetail.this, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    class Getsites extends AsyncTask<Void, Void, ArrayList<SiteData>> {
        String message = "n";
        String code = "n";
        JSONObject response = null;
        SiteData siteData = null;
        JSONArray data = null;
        ArrayList<SiteData> siteDatas;
        private String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SiteDetail.this);
            pd.setMessage("Loading...");
            pd.show();
        }


        @Override
        protected ArrayList<SiteData> doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.GetSiteDetail(GET_SITEDETAIL, authkey, siteId);
                Log.d("RESPONSE1", response.toString());


                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);
                }


                if (response.has(SITEDESC)) {
                    desc = response.getString(SITEDESC);

                }
                if (response.has(DATA)) {
                    siteDatas = new ArrayList<>();
                    data = response.getJSONArray(DATA);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject currentlocation = data.getJSONObject(i);
                        siteData = new SiteData();
                        if (currentlocation.has(ID)) {
                            siteData.setId(currentlocation.getString(ID));
                        }
                        if (currentlocation.has(BID)) {
                            siteData.setBid(currentlocation.getString(BID));
                        }
                        if (currentlocation.has(NAME)) {
                            siteData.setName(currentlocation.getString(NAME));
                        }
                        siteDatas.add(siteData);

                    }
                }

            } catch (Exception e) {

            }
            return siteDatas;
        }

        @Override
        protected void onPostExecute(ArrayList<SiteData> data) {
            pd.dismiss();
            if (data != null) {
                SiteData = data;
                Log.d("SITE", SiteData.toString());
                listView.setAdapter(new Sitedetailadapter(SiteDetail.this, SiteData));
                tvdesc.setText(desc);


            }


        }


    }

}
