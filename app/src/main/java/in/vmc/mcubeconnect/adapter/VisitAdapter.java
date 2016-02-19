package in.vmc.mcubeconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import in.vmc.mcubeconnect.R;
import in.vmc.mcubeconnect.activity.DatailImgeView;
import in.vmc.mcubeconnect.activity.Home;
import in.vmc.mcubeconnect.fragment.FragmentAll;
import in.vmc.mcubeconnect.fragment.FragmentLike;
import in.vmc.mcubeconnect.fragment.FragmentOffer;
import in.vmc.mcubeconnect.fragment.FragmentVisit;
import in.vmc.mcubeconnect.model.VisitData;
import in.vmc.mcubeconnect.utils.JSONParser;
import in.vmc.mcubeconnect.utils.TAG;
import in.vmc.mcubeconnect.utils.Utils;

/**
 * Created by mukesh on 8/1/16.
 */
public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.FollowViewHolder> implements TAG {

    public ViewClickedListner viewClickedListner;
    public RelativeLayout mroot;
    public Fragment fragment;
    ArrayList<VisitData> visitDatas;
    private Context context;
    private LayoutInflater inflator;
    private int position;


    public VisitAdapter(Context context, ArrayList<VisitData> visitDatas, RelativeLayout mroot, Fragment fragment) {
        this.context = context;
        this.visitDatas = visitDatas;
        this.mroot = mroot;
        this.fragment = fragment;
        this.viewClickedListner = (Home) context;
    }

    public static void getImagesList(final String siteId, final Context context, final RelativeLayout mroot) {


        if (Utils.onlineStatus2(context)) {
            new GetImagesList(siteId, context).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(context.getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getImagesList(siteId, context, mroot);

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(context, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }
    }

    public static void deleteSite(final String siteId, final int pos, final View v, final Context context, final Fragment fragment, final RelativeLayout mroot) {


        if (Utils.onlineStatus2(context)) {
            new DeletVisit(siteId, pos, v, context, fragment).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(context.getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSite(siteId, pos, v, context, fragment, mroot);

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(context, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }
    }

    @Override
    public void onViewRecycled(FollowViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public FollowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_likeview, parent, false);
        return new FollowViewHolder(itemView, context, viewClickedListner, visitDatas);
    }

    @Override
    public void onBindViewHolder(final FollowViewHolder holder, final int position) {


        if (((Home) context).widthDp >= 600) {
            holder.businessDeatil.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.
                    getResources().getDimension(R.dimen.result_fonttab));

        } else {
            holder.businessDeatil.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.
                    getResources().getDimension(R.dimen.result_font));
        }


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
        holder.overflow.setOnClickListener(new OnOverflowSelectedListener(context, holder.getAdapterPosition(), visitDatas, mroot, fragment));
        if (visitDatas.get(holder.getAdapterPosition()).isLike()) {
            holder.likelogo.setBackgroundResource(R.drawable.ic_liked);
        } else {
            holder.likelogo.setBackgroundResource(R.drawable.ic_like);
        }
        holder.likelogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (context instanceof Home) {
                    ((Home) context).GetLikeUnlike(holder.likelogo, visitDatas.get(holder.getAdapterPosition()).getSiteid(), visitDatas.get(holder.getAdapterPosition()).getBid());
                }
            }
        });

        if (visitDatas.get(holder.getAdapterPosition()).getOffer() != null && !visitDatas.get(holder.getAdapterPosition()).getOffer().equals("") && !visitDatas.get(holder.getAdapterPosition()).getOffer().equals("null")) {
            if (holder.offer.getVisibility() == View.INVISIBLE) {
                holder.offer.setVisibility(View.VISIBLE);
            }
            holder.offer.setText(visitDatas.get(holder.getAdapterPosition()).getOffer() + "%OFF");
        } else {
            if (holder.offer.getVisibility() == View.VISIBLE) {
                holder.offer.setVisibility(View.INVISIBLE);
            }
        }
        if (visitDatas.get(holder.getAdapterPosition()).getOffer_desc() != null &&
                !visitDatas.get(holder.getAdapterPosition()).getOffer_desc().equals("") &&
                !visitDatas.get(holder.getAdapterPosition()).getOffer().equals("null")) {

            holder.businessDeatil.setText(visitDatas.get(holder.getAdapterPosition()).getOffer_desc());
        } else {
            holder.businessDeatil.setText(visitDatas.get(position).getSitedesc());
        }


        if (visitDatas.get(position).getBitmapLogp() != null) {
            holder.Logo.setImageBitmap(visitDatas.get(position).getBitmapLogp());
        } else {
            new GetImageFromUrl(visitDatas.get(position).getSiteicon(), holder.Logo, true).execute();
        }


    }

    @Override
    public int getItemCount() {
        return visitDatas.size();
    }

    public interface ViewClickedListner {
        public void OnItemClick(int position, View v, VisitData visitData);
    }

    public static class FollowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView businessDeatil;
        protected ImageView Logo;
        protected Context ctx;
        protected ImageView likelogo;
        protected View overflow;
        protected TextView offer;
        protected ViewClickedListner viewClickedListner;
        protected ArrayList<VisitData> visitDatas;


        public FollowViewHolder(View v, Context ctx, ViewClickedListner viewClickedListner, ArrayList<VisitData> visitDatas) {
            super(v);
            businessDeatil = (TextView) v.findViewById(R.id.desc);
            offer = (TextView) v.findViewById(R.id.offer);
            Logo = (ImageView) v.findViewById(R.id.blogo);
            likelogo = (ImageView) v.findViewById(R.id.likelogo);
            this.ctx = ctx;
            this.viewClickedListner = viewClickedListner;
            this.visitDatas = visitDatas;
            overflow = v.findViewById(R.id.album_overflow);
            v.setClickable(true);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (viewClickedListner != null) {
                viewClickedListner.OnItemClick(getAdapterPosition(), v, visitDatas.get(getAdapterPosition()));
            }
        }


    }

    public static class OnOverflowSelectedListener implements View.OnClickListener {
        private Context mContext;
        private int position;
        private ArrayList<VisitData> visitDatas;
        private RelativeLayout mroot;
        private Fragment fragment;


        public OnOverflowSelectedListener(Context context, int pos, ArrayList<VisitData> visitDatas, RelativeLayout mroot, Fragment fragment) {
            mContext = context;
            this.position = pos;
            this.visitDatas = visitDatas;
            this.mroot = mroot;
            this.fragment = fragment;
        }

        @Override
        public void onClick(final View v) {
            PopupMenu popupMenu = new PopupMenu(mContext, v) {
                @Override
                public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.call:
                            Utils.makeAcall(visitDatas.get(position).getNumber(), (Home) mContext);
                            return true;

                        case R.id.delete:
                            deleteSite(visitDatas.get(position).getSiteid(), position, v, mContext, fragment, mroot);
                            return true;

                        case R.id.share:
                            if (mContext instanceof Home) {
                                ((Home) mContext).showReferDailogDialog(visitDatas.get(position).getSiteid());
                            }
                            return true;

                        case R.id.image:
                            getImagesList(visitDatas.get(position).getSiteid(), mContext, mroot);
                            return true;
                        default:
                            return super.onMenuItemSelected(menu, item);
                    }
                }
            };

            // Force icons to show
            Object menuHelper = null;
            Class[] argTypes;
            try {
                Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                fMenuHelper.setAccessible(true);
                menuHelper = fMenuHelper.get(popupMenu);
                argTypes = new Class[]{boolean.class};
                menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
            } catch (Exception e) {
                Log.w("t", "error forcing menu icons to show", e);
                popupMenu.show();
                // Try to force some horizontal offset
                try {
                    Field fListPopup = menuHelper.getClass().getDeclaredField("mPopup");
                    fListPopup.setAccessible(true);
                    Object listPopup = fListPopup.get(menuHelper);
                    argTypes = new Class[]{int.class};
                    Class listPopupClass = listPopup.getClass();
                } catch (Exception e1) {

                    Log.w("T", "Unable to force offset", e);
                }
                return;
            }


            if (visitDatas.get(position).isDelete()) {
                popupMenu.inflate(R.menu.popupmenu);
            } else {
                popupMenu.inflate(R.menu.popupnodel);
            }


            popupMenu.show();


        }
    }

    static class GetImagesList extends AsyncTask<Void, Void, ArrayList<String>> {
        String message = "n";
        String code = "n";
        JSONObject response = null;
        String SiteId;
        private ArrayList<String> images;
        private JSONObject imagesData = null;
        private Context context;

        public GetImagesList(String siteId, Context context) {
            SiteId = siteId;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.GetImages(GET_IMAGES, SiteId);
                Log.d("RESPONSE", response.toString());
                images = new ArrayList<String>();

                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);

                }
                if (response.has(DATA))

                    try {
                        imagesData = response.getJSONObject(DATA);
                        Iterator keys = imagesData.keys();

                        while (keys.hasNext()) {

                            String currentKey = (String) keys.next();
                            images.add(imagesData.getString(currentKey));

                            // do something here with the value...
                        }


                    } catch (Exception e) {

                    }
            } catch (Exception e) {

            }
            return images;
        }

        @Override
        protected void onPostExecute(ArrayList<String> data) {

            if (data != null && code.equals("400")) {
                Intent intent = new Intent(context, DatailImgeView.class);
                intent.putExtra("mylist", data);
                context.startActivity(intent);

            }

        }


    }

    static class DeletVisit extends AsyncTask<Void, Void, String> {
        String message = "n";
        String code = "n";
        JSONObject response = null;
        String SiteId;
        int pos;
        String authkey;
        View v;
        Fragment fragment;
        Context context;

        public DeletVisit(String siteId, int pos, View v, Context context, Fragment fragment) {
            SiteId = siteId;
            this.pos = pos;
            authkey = Utils.getFromPrefs(context, AUTHKEY, "n");
            this.v = v;
            this.fragment = fragment;
            this.context = context;
        }


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.DeleteSite(DELETE_SITE, SiteId, authkey);
                Log.d("RESPONSE", response.toString());

                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);

                }

            } catch (Exception e) {

            }
            return message;
        }

        @Override
        protected void onPostExecute(String data) {

            if (data != null && code.equals("400")) {
                LinearLayout linearLayout = (LinearLayout) v.getParent();
                LinearLayout linearLayout1 = (LinearLayout) linearLayout.getParent();
                CardView v = (CardView) linearLayout1.getParent();
                YoYo.with(Techniques.FadeOut).duration(300).playOn(v);
                if (fragment instanceof FragmentVisit) {
                    ((FragmentVisit) fragment).Resetdapter();
                } else if (fragment instanceof FragmentAll) {
                    ((FragmentAll) fragment).Resetdapter();
                } else if (fragment instanceof FragmentLike) {
                    ((FragmentLike) fragment).Resetdapter();
                } else if (fragment instanceof FragmentOffer) {
                    ((FragmentOffer) fragment).Resetdapter();
                }
                ((Home) context).ResetVisitedBeaconList();
            }

        }


    }

    class GetImageFromUrl extends AsyncTask<Void, Void, Bitmap> {
        String url;
        Bitmap bitmap;
        ImageView imageView;
        boolean logo;

        public GetImageFromUrl(String url, ImageView imageView, boolean logo) {
            this.url = url;
            this.imageView = imageView;
            this.logo = logo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Bitmap doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                bitmap = JSONParser.getBitmapFromURL(url);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap data) {

            if (data != null) {
                if (logo) {
                    Bitmap resize = Bitmap.createScaledBitmap(data, 150, 150, false);
                    imageView.setImageBitmap(resize);
                }
            }
        }

    }


}

