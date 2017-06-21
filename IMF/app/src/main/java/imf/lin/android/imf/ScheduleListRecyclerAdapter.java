package imf.lin.android.imf;

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

public class ScheduleListRecyclerAdapter extends RecyclerView.Adapter<ScheduleListRecyclerAdapter.ViewHolder>
            implements  View.OnClickListener {
    //List of text set into recommend card
    protected List<String> dataset;
    //List of text set into time card
    protected List<String> timeset;


    private RecyclerView mRecycler;
    private OnItemClickListener mListener;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecycler= recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecycler = null;
    }


    /** ViewHolder innerClass*/
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView planTextView;
        public final TextView timeTextView;

        public ViewHolder(View v){
            super(v);

            planTextView = (TextView) v.findViewById(R.id.recommend_card_textView);
            timeTextView = (TextView) v.findViewById(R.id.time_textView);

        }
    }

    /** Constructor */
    public ScheduleListRecyclerAdapter(List<String> _dataset,List<String> _timeset){
        dataset = _dataset;
        timeset = _timeset;
    }

    /** onCreateViewHolder inflate Recycler XML and set into ViewHolder*/
    @Override
    public ScheduleListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new View
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_schedule_card, parent, false);
        //add ClickListener to view
        v.setOnClickListener(this);

        //coordinate layout which has nothing to do with data.
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /** onBindViewHolder */
    //change data in View( Called By LayoutManager )
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        //get data
        String plan = dataset.get(position);
        String time = timeset.get(position);
        //change data
        holder.planTextView.setText(plan);
        holder.timeTextView.setText(time);

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

    // データを変更する
    public void changeAtPosition(int position) {
        if (position < dataset.size()) {
            // データを削除する
            dataset.remove(position);
            // 削除したことをAdapterに教える
            notifyItemRemoved(position);
            if (position > dataset.size()) {
                // 現在存在するアイテムの個数より多い位置を指定しているので、最後の位置に追加
                position = position - dataset.size();
            }
            // データを追加する
            dataset.add(position, dataset.get(position + 5));
            // 挿入したことをAdapterに教える
            notifyItemInserted(position);
        }
    }


    // データを移動する
    public void move(int fromPosition, int toPosition) {
        final String text = dataset.get(fromPosition);
        dataset.remove(fromPosition);
        dataset.add(toPosition, text);
        notifyItemMoved(fromPosition, toPosition);
    }

    //データの内容を変更する
    public void changeText(int position, String addText){
        String text = dataset.get(position);
        dataset.remove(position);
        notifyItemRemoved(position);
        dataset.add(position, addText + "  " + text);
        notifyItemInserted(position);
    }

    //時刻のセット
    public void setTime(int position, String time){
        timeset.remove(position);
        notifyItemRemoved(position);
        timeset.add(position, time);
        notifyItemInserted(position);

    }

    //ペインを動かす
    public void openPane(int fromPosition, int toPosition) {

    }

    //テキストを取得する
     public String getText(int position){
         return dataset.get(position);
     }

    //クリックイベント
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (mRecycler == null) {
            return;
        }

        if (mListener != null) {
            int position = mRecycler.getChildAdapterPosition(view);
            mListener.onItemClick(this, position);
        }
    }

    public static interface OnItemClickListener {
        public void onItemClick(ScheduleListRecyclerAdapter adapter, int position);
    }

}
