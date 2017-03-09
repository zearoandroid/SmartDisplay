package com.zearoconsulting.smartdisplay.presentation.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zearoconsulting.smartdisplay.R;
import com.zearoconsulting.smartdisplay.presentation.model.KOTLineItems;
import com.zearoconsulting.smartdisplay.presentation.model.Product;
import java.util.List;

/**
 * Created by saravanan on 28-11-2016.
 */

public class TokenItemAdapter extends RecyclerView.Adapter<TokenItemAdapter.ItemListRowHolder>  {

    List<KOTLineItems> mKOTItemList;

    public TokenItemAdapter(List<KOTLineItems> kotItemList) {
        this.mKOTItemList = kotItemList;
    }

    @Override
    public TokenItemAdapter.ItemListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_items, null);
        TokenItemAdapter.ItemListRowHolder mh = new TokenItemAdapter.ItemListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(TokenItemAdapter.ItemListRowHolder holder, int pos) {

        final KOTLineItems kotItem = mKOTItemList.get(pos);

        Product product = kotItem.getProduct();

        holder.txtItemName.setText(kotItem.getQty()+" "+product.getProdName());

        if(!kotItem.getNotes().equals("")){
            holder.txtItemDescription.setText("  "+kotItem.getNotes());
        }

    }

    @Override
    public int getItemCount() {
        return (null != mKOTItemList ? mKOTItemList.size() : 0);
    }

    public class ItemListRowHolder extends RecyclerView.ViewHolder{

        protected TextView txtItemName;
        protected TextView txtItemDescription;
        public final View mView;

        public ItemListRowHolder(View view) {
            super(view);
            mView = view;
            this.txtItemName = (TextView) view.findViewById(R.id.txtItemName);
            this.txtItemDescription = (TextView) view.findViewById(R.id.txtItemDescription);
        }
    }
}