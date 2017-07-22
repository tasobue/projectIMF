package imf.lin.android.imf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stmac0001 on 2017/05/02.
 */

public class RecommendListRecyclerAdapter extends BaseRecyclerAdapter<RecommendListRecyclerAdapter.ViewHolder>  {
    protected List<CharSequence> rcmnd_detail_list;

    /** ViewHolder innerClass*/
    public static class ViewHolder extends BaseRecyclerAdapter.ViewHolder{
        public final TextView rcmnd_title;
        public final TextView rcmnd_detail;

        public ViewHolder(View v){
            super(v);
            rcmnd_title = (TextView) v.findViewById(R.id.recommend_card_textView);
            rcmnd_detail = (TextView) v.findViewById(R.id.recommend_card_detail_textView);
        }
    }

    /** Constructor */
    public RecommendListRecyclerAdapter(ArrayList<CharSequence> _titles, ArrayList<CharSequence> _details){
        super(_titles, R.layout.layout_recommend_card);
        rcmnd_detail_list = _details;
    }

    /** onCreateViewHolder inflate Recycler XML and set into ViewHolder*/
    @Override
    public RecommendListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new View
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recommend_card, parent, false);
        //coordinate layout which has nothing to do with data.
        RecommendListRecyclerAdapter.ViewHolder vh = new RecommendListRecyclerAdapter.ViewHolder(v);
        return vh;
    }

    /** onBindViewHolder */
    //change data in View( Called By LayoutManager )
    @Override
    public void onBindViewHolder(RecommendListRecyclerAdapter.ViewHolder holder, int position){
        holder.rcmnd_title.setText(super.dataset.get(position));
        holder.rcmnd_detail.setText(rcmnd_detail_list.get(position));
    }
}
