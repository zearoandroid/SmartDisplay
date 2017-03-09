package com.zearoconsulting.smartdisplay.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    SimpleDateFormat mDFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

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

        StringBuffer mStrProd = new StringBuffer();
        for(int i=0; i<mKotItemList.size(); i++){

            Product product = mKotItemList.get(i).getProduct();
            String desc = mKotItemList.get(i).getNotes();
            int qty = mKotItemList.get(i).getQty();

            if(!desc.equals("")){
                mStrProd.append(qty+" "+product.getProdName()+"\n"+" "+desc+"\n");
            }else{
                mStrProd.append(qty+" "+product.getProdName()+"\n"+"\n");
            }
        }

        holder.txtProductsView.setText(mStrProd.toString());

        if(mAppDataManager.getTerminalID()==0)
            holder.btnKOTComplete.setVisibility(View.GONE);
        else
            holder.btnKOTComplete.setVisibility(View.VISIBLE);

        /*TokenItemAdapter mTokenItemAdapter = new TokenItemAdapter(mKotItemList);
        holder.tokenItemListView.setAdapter(mTokenItemAdapter);
        mTokenItemAdapter.notifyDataSetChanged();
        */
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
}
