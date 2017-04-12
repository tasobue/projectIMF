package imf.lin.android.imf;

import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by stmac0001 on 2017/03/22.
 *
 */

public class ScheduleListRecyclerAdapter extends RecyclerView.Adapter<ScheduleListRecyclerAdapter.ViewHolder> {
    //List of text set into recommend card
    protected List<String> dataset;
    private View.OnClickListener onItemViewClickListener;


    /** ViewHolder innerClass*/
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView textView;

        public ViewHolder(View v){
            super(v);

            textView = (TextView) v.findViewById(R.id.recommend_card_textView);

        }
    }

    /** Constructor */
    public ScheduleListRecyclerAdapter(List<String> _dataset){
        dataset = _dataset;
    }
    /** onCreateViewHolder inflate Recycler XML and set into ViewHolder*/
    @Override
    public ScheduleListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new View
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recommend_card, parent, false);
        //add ClickListener to view
        if(onItemViewClickListener != null){
            v.setOnClickListener(onItemViewClickListener);
        }
        //coordinate layout which has nothing to do with data.
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /** onBindViewHolder */
    //change data in View( Called By LayoutManager )
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        //get data
        String text = dataset.get(position);
        //change data
        holder.textView.setText(text);
    }

    /** getItemCount */
    @Override
    public int getItemCount(){
        return dataset.size();
    }

    // データを挿入する
    public void addAtPosition(int position, String text) {
        if (position > dataset.size()) {
            // 現在存在するアイテムの個数より多い位置を指定しているので、最後の位置に追加
            position = dataset.size();
        }
        // データを追加する
        dataset.add(position, text);
        // 挿入したことをAdapterに教える
        notifyItemInserted(position);
    }

    // データを削除する
    public void removeAtPosition(int position) {
        if (position < dataset.size()) {
            // データを削除する
            dataset.remove(position);
            // 削除したことをAdapterに教える
            notifyItemRemoved(position);
        }
    }


    // データを移動する
    public void move(int fromPosition, int toPosition) {
        final String text = dataset.get(fromPosition);
        dataset.remove(fromPosition);
        dataset.add(toPosition, text);
        notifyItemMoved(fromPosition, toPosition);
    }

    //ペインを動かす
    public void openPane(int fromPosition, int toPosition) {

    }

}
