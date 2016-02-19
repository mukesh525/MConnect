package in.vmc.mcubeconnect.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import in.vmc.mcubeconnect.R;
import in.vmc.mcubeconnect.activity.Home;
import in.vmc.mcubeconnect.adapter.VisitAdapter;
import in.vmc.mcubeconnect.callbacks.Popupcallback;
import in.vmc.mcubeconnect.model.VisitData;
import in.vmc.mcubeconnect.utils.EndlessScrollListener;
import in.vmc.mcubeconnect.utils.JSONParser;
import in.vmc.mcubeconnect.utils.ReferDialogFragment;
import in.vmc.mcubeconnect.utils.TAG;
import in.vmc.mcubeconnect.utils.Utils;


public class FragmentAll extends Fragment implements SwipeRefreshLayout.OnRefreshListener, TAG {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayList<in.vmc.mcubeconnect.model.VisitData> VisitData = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int MIN = 0, MAX = 10;
    private boolean loading = false;
    private Popupcallback popupcallback;
    private VisitAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ReferDialogFragment referDialogFragment = new ReferDialogFragment();
    private RelativeLayout mroot;
    private LinearLayout pdloadmore;

    public FragmentAll() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentAll newInstance(String param1, String param2) {
        FragmentAll fragment = new FragmentAll();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onPause() {

        super.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmentall, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipefollowUp);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mroot = (RelativeLayout) view.findViewById(R.id.root);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new VisitAdapter(getActivity(), VisitData, mroot, FragmentAll.this);
        pdloadmore = (LinearLayout) view.findViewById(R.id.loadmorepd1);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore() {
                if (pdloadmore.getVisibility() == View.GONE) {
                    pdloadmore.setVisibility(View.VISIBLE);
                }
                if (!loading) {
                    GetMoreData();
                }

            }

            @Override
            public void onLoadUp() {

                // if (VisitData != null && VisitData.size() >= MAX) {
                if (pdloadmore.getVisibility() == View.VISIBLE) {
                    pdloadmore.setVisibility(View.GONE);


                }

            }
        });

        if (savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            GetVisits();
        } else {
//            String Json = savedInstanceState.getString("key");
//            Log.d("serialise", Json);
//            VisitData = new Gson().fromJson(Json, new TypeToken<ArrayList<VisitData>>() {
//            }.getType());
            if (VisitData != null) {
                adapter = new VisitAdapter(getActivity(), VisitData, mroot, FragmentAll.this);
            }
        }


        return view;
    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(false);
        GetVisits();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            Activity activity = (Activity) context;
            popupcallback = (Home) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()
                    + " must implement SellFragmentListener");
        }

    }


    public void GetVisits() {

        if (Utils.onlineStatus2(getActivity())) {
            MIN = 0;
            new GetVistHistory().execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetVisits();

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    public void GetMoreData() {

        if (Utils.onlineStatus2(getActivity())) {
            MIN = MIN + 10;
            new GetMoreData().execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetMoreData();

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(getActivity(), R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    public void Resetdapter() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GetVisits();

            }
        }, 800);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("key", "test");

    }

    class GetVistHistory extends AsyncTask<Void, Void, ArrayList<VisitData>> {
        String message = "n";
        String code = "n";
        JSONObject response = null;
        VisitData visitData = null;
        JSONArray data = null;
        ArrayList<VisitData> visitDatas;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;

        }


        @Override
        protected ArrayList<VisitData> doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                //  response = JSONParser.GetVistDetail(GET_ALLSITES, ((Home) getActivity()).authkey);
                response = JSONParser.GetAllSites(GET_ALLSITES, ((Home) getActivity()).authkey, MIN + "", MAX + "");
                Log.d("RESPONSE", response.toString());
                visitDatas = new ArrayList<VisitData>();

                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);
                }
                if (response.has(DATA)) {
                    visitDatas = new ArrayList<>();
                    data = response.getJSONArray(DATA);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject currentvisit = data.getJSONObject(i);
                        visitData = new VisitData();
                        if (currentvisit.has(SITENAME)) {
                            visitData.setSitename(currentvisit.getString(SITENAME));
                        }
                        if (currentvisit.has(SITEID)) {
                            visitData.setSiteid(currentvisit.getString(SITEID));
                        }
                        if (currentvisit.has(SITEDESC)) {
                            visitData.setSitedesc(currentvisit.getString(SITEDESC));
                        }
                        if (currentvisit.has(SITEICON)) {
                            visitData.setSiteicon(currentvisit.getString(SITEICON));
                        }
                        if (currentvisit.has(NUMBER)) {
                            visitData.setNumber(currentvisit.getString(NUMBER));
                        }
                        if (currentvisit.has(BID)) {
                            visitData.setBid(currentvisit.getString(BID));
                        }


                        if (currentvisit.has(OFFER_PER)) {
                            visitData.setOffer(currentvisit.getString(OFFER_PER));
                        }
                        if (currentvisit.has(OFFER_DEC)) {
                            visitData.setOffer_desc(currentvisit.getString(OFFER_DEC));
                        }
                        if (currentvisit.has(LIKES)) {
                            String Like = currentvisit.getString(LIKES);
                            if (Like.equals("1")) {
                                visitData.setLike(true);
                            } else {
                                visitData.setLike(false);
                            }
                        }
                        visitDatas.add(visitData);

                    }
                }

            } catch (Exception e) {

            }
            return visitDatas;
        }

        @Override
        protected void onPostExecute(ArrayList<VisitData> data) {

            loading = false;
            if (data != null) {
                VisitData = data;
                adapter = new VisitAdapter(getActivity(), VisitData, mroot, FragmentAll.this);
                recyclerView.setAdapter(adapter);
            }


        }


    }

    class GetMoreData extends AsyncTask<Void, Void, ArrayList<VisitData>> {
        String message = "n";
        String code = "n";
        JSONObject response = null;
        VisitData visitData = null;
        JSONArray data = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;

            if (pdloadmore.getVisibility() == View.GONE) {
                pdloadmore.setVisibility(View.VISIBLE);
            }
        }


        @Override
        protected ArrayList<VisitData> doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                //  response = JSONParser.GetVistDetail(GET_ALLSITES, ((Home) getActivity()).authkey);
                response = JSONParser.GetAllSites(GET_ALLSITES, ((Home) getActivity()).authkey, MIN + "", MAX + "");
                Log.d("RESPONSE", response.toString());

                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);
                }
                if (response.has(DATA)) {
                    data = response.getJSONArray(DATA);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject currentvisit = data.getJSONObject(i);
                        visitData = new VisitData();
                        if (currentvisit.has(SITENAME)) {
                            visitData.setSitename(currentvisit.getString(SITENAME));
                        }
                        if (currentvisit.has(SITEID)) {
                            visitData.setSiteid(currentvisit.getString(SITEID));
                        }
                        if (currentvisit.has(SITEDESC)) {
                            visitData.setSitedesc(currentvisit.getString(SITEDESC));
                        }
                        if (currentvisit.has(SITEICON)) {
                            visitData.setSiteicon(currentvisit.getString(SITEICON));
                        }
                        if (currentvisit.has(NUMBER)) {
                            visitData.setNumber(currentvisit.getString(NUMBER));
                        }

                        if (currentvisit.has(OFFER_PER)) {
                            visitData.setOffer(currentvisit.getString(OFFER_PER));
                        }
                        if (currentvisit.has(OFFER_DEC)) {
                            visitData.setOffer_desc(currentvisit.getString(OFFER_DEC));
                        }
                        if (currentvisit.has(LIKES)) {
                            String Like = currentvisit.getString(LIKES);
                            if (Like.equals("1")) {
                                visitData.setLike(true);
                            } else {
                                visitData.setLike(false);
                            }
                        }
                        VisitData.add(visitData);

                    }
                }

            } catch (Exception e) {

            }
            return VisitData;
        }

        @Override
        protected void onPostExecute(ArrayList<VisitData> data) {
            if (pdloadmore.getVisibility() == View.VISIBLE) {
                pdloadmore.setVisibility(View.GONE);
            }

            loading = false;


            if (code.equals("202") && Home.currentPosition == 0) {
                Snackbar.make(mroot, "No more records availabe", Snackbar.LENGTH_SHORT).show();

            } else if (data != null && data.size() > 0) {
                VisitData = data;
                adapter = new VisitAdapter(getActivity(), VisitData, mroot, FragmentAll.this);
                recyclerView.setAdapter(adapter);
            }


        }


    }


}