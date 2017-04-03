package com.zearoconsulting.smartdisplay.presentation.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zearoconsulting.smartdisplay.R;
import com.zearoconsulting.smartdisplay.data.AppDataManager;
import com.zearoconsulting.smartdisplay.data.DBHelper;
import com.zearoconsulting.smartdisplay.presentation.model.KOTHeader;
import com.zearoconsulting.smartdisplay.presentation.model.KOTLineItems;
import com.zearoconsulting.smartdisplay.presentation.model.Product;
import com.zearoconsulting.smartdisplay.presentation.model.Tables;
import com.zearoconsulting.smartdisplay.presentation.presenter.ITokenSelectedListener;
import com.zearoconsulting.smartdisplay.presentation.view.activity.KOTItemDisplay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by saravanan on 28-11-2016.
 */

public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.TokenListRowHolder>  {

    List<KOTHeader> mKOTHeaderList;
    private Context mContext;
    ITokenSelectedListener listener;
    long mTerminalID;
    private AppDataManager mAppDataManager;
    private DBHelper mDBHelper;
    SimpleDateFormat mDFormat = new SimpleDateFormat("dd-MM-yy hh:mm a");

    public TokenAdapter(Context context, DBHelper dbHelper, AppDataManager appDataManager, List<KOTHeader> kotHeaderList, long terminalId) {
        this.mKOTHeaderList = kotHeaderList;
        this.mContext = context;
        this.mTerminalID = terminalId;
        this.mDBHelper = dbHelper;
        this.mAppDataManager = appDataManager;
    }

    @Override
    public TokenListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_token, null);
        TokenListRowHolder mh = new TokenListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(TokenListRowHolder holder, int pos) {

        final KOTHeader kotHeader = mKOTHeaderList.get(pos);

        holder.txtKOTNumView.setText("KOT: "+kotHeader.getKotNumber());

        if(kotHeader.getTablesId() == 0){
            holder.txtTableNameView.setText("CS"+kotHeader.getInvoiceNumber());
        }else{
            Tables tables =mDBHelper.getTableData(mAppDataManager.getClientID(),mAppDataManager.getOrgID(),kotHeader.getTablesId());
            holder.txtTableNameView.setText("Table: "+tables.getTableName());
        }

        holder.txtOrderByView.setText("Order By: "+kotHeader.getOrderBy());

        long mCreatedTime = kotHeader.getCreateTime();
        holder.txtDateTimeView.setText(String.valueOf(mDFormat.format(new Date(mCreatedTime))));

        List<KOTLineItems> mKotItemList = mDBHelper.getKOTLineItem(kotHeader.getKotNumber());

        long maxPreTime  = mDBHelper.getKOTLineItemPreTime(kotHeader.getKotNumber());
        Date d2 = new Date(maxPreTime);
        Date d1 = new Date(System.currentTimeMillis());
        long diff = d2.getTime() - d1.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);

        long diffTime = diffHours*60 + diffMinutes;

        if(diffTime<=1){
            holder.mView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
        }else{
            holder.mView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }

        StringBuffer mStrProd = new StringBuffer();
        for(int i=0; i<mKotItemList.size(); i++){

            Product product = mKotItemList.get(i).getProduct();
            String desc = mKotItemList.get(i).getNotes();
            int qty = mKotItemList.get(i).getQty();
            long insertTime = mKotItemList.get(i).getCreateTime();

            Date d3 = new Date(insertTime);
            Date d4 = new Date(System.currentTimeMillis());

            long diff1 = d3.getTime() - d4.getTime();
            long diffMinutes1 = diff1 / (60 * 1000) % 60;
            long diffHours1 = diff1 / (60 * 60 * 1000);

            long diffTime1 = diffHours1*60 + diffMinutes1;

            if(diffTime1<=1){
                //holder.txtProductsView.setTextColor(ContextCompat.getColor(mContext, R.color.red));

                if(!desc.equals("")){
                    mStrProd.append("<font COLOR=\'RED\'><b>" + qty +" "+product.getProdName()+"</b></font><br/>"+"<font COLOR=\'#00FF00\'><i>" + desc +"</i></font><br/><br/>");
                }else{
                    mStrProd.append("<font COLOR=\'RED\'><b>" + qty +" "+product.getProdName()+"\n"+"</b></font><br/><br/>");
                }

            }else{

                if(!desc.equals("")){
                    mStrProd.append("<font><b>" +qty+" "+product.getProdName()+"</b></font><br/>"+"<font COLOR=\'#338AFF\'><i>"+desc+"</i></font><br/><br/>");
                }else{
                    mStrProd.append("<font><b>" +qty+" "+product.getProdName()+"</b></font><br/><br/>");
                }
            }
        }

        holder.txtProductsView.setText(Html.fromHtml(mStrProd.toString()));

        if(mAppDataManager.getTerminalID()==0)
            holder.btnKOTComplete.setText("Order Delivered");
        else
            holder.btnKOTComplete.setText("Order Complete");
    }

    @Override
    public int getItemCount() {
        return (null != mKOTHeaderList ? mKOTHeaderList.size() : 0);
    }

    public class TokenListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView txtKOTNumView;
        protected TextView txtTableNameView;
        protected TextView txtOrderByView;
        protected TextView txtDateTimeView;
        protected TextView txtProductsView;
        protected RecyclerView tokenItemListView;
        protected Button btnKOTComplete;
        public final View mView;

        public TokenListRowHolder(View view) {
            super(view);
            mView = view;
            this.txtKOTNumView = (TextView) view.findViewById(R.id.txtKOTNumView);
            this.txtTableNameView = (TextView) view.findViewById(R.id.txtTableNameView);
            this.txtOrderByView = (TextView) view.findViewById(R.id.txtOrderByView);
            this.txtDateTimeView = (TextView) view.findViewById(R.id.txtDateTimeView);
            this.txtProductsView = (TextView) view.findViewById(R.id.txtProductsView);
            this.tokenItemListView = (RecyclerView) view.findViewById(R.id.tokenItemListView);
            this.btnKOTComplete = (Button) view.findViewById(R.id.btnKOTComplete);
            this.btnKOTComplete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

           selectedItem(getAdapterPosition());
        }
    }

    public void selectedItem(int pos) {
        try {
            KOTHeader token = mKOTHeaderList.get(pos);

            /*
             * TokenListener not working so instanceof
             * method implemented
             */
            if (mContext instanceof KOTItemDisplay) {
                if(mAppDataManager.getTerminalID()==0)
                    ((KOTItemDisplay) mContext).updateKOTDelivered(token.getKotNumber());
                else
                    ((KOTItemDisplay) mContext).updateKOTComplete(token.getKotNumber());

            }
            //listener.OnTokenSelectedListener(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnTokenSelectListener(ITokenSelectedListener paramTokenSelectListener) {
        this.listener = paramTokenSelectListener;
    }

    public void refresh(List<KOTHeader> list){
        if(list!=null) {
            if (mKOTHeaderList != null) {
                mKOTHeaderList.clear();
                mKOTHeaderList.addAll(list);
            } else {
                mKOTHeaderList = list;
            }
            notifyDataSetChanged();
        }
    }

    public List<KOTHeader> getExistsTokenList(){
        return mKOTHeaderList;
    }
}
