package imf.lin.android.imf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by stmac0001 on 2017/05/02.
 */

public class RecommendListRecyclerAdapter extends RecyclerView.Adapter<RecommendListRecyclerAdapter.ViewHolder>  {

    //List of text set into recommend card
    protected ArrayList<CharSequence> dataset;
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
    public RecommendListRecyclerAdapter(ArrayList<CharSequence> _dataset){
        dataset = _dataset;
    }
    public RecommendListRecyclerAdapter(){};

    /** onCreateViewHolder inflate Recycler XML and set into ViewHolder*/
    @Override
    public RecommendListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new View
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recommend_card, parent, false);
        //add ClickListener to view
        if(onItemViewClickListener != null){
            v.setOnClickListener(onItemViewClickListener);
        }
        //coordinate layout which has nothing to do with data.
        RecommendListRecyclerAdapter.ViewHolder vh = new RecommendListRecyclerAdapter.ViewHolder(v);
        return vh;
    }

    /** onBindViewHolder */
    //change data in View( Called By LayoutManager )
    @Override
    public void onBindViewHolder(RecommendListRecyclerAdapter.ViewHolder holder, int position){
        //get data
        CharSequence text = dataset.get(position);
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
        final CharSequence text = dataset.get(fromPosition);
        dataset.remove(fromPosition);
        dataset.add(toPosition, text);
        notifyItemMoved(fromPosition, toPosition);
    }

    //テキストを取得する
    public CharSequence getText(int position){
        return dataset.get(position);
    }

    //************************************************************************************
    // コールバック用のリスナー
    private RecommendListRecyclerAdapter.OnButtonClickListener mListener;

    /*
     * コールバックの定義
     */
    public interface OnButtonClickListener {
        // クリック処理
        public void onClick(int position);
    }

    /*
     * コールバックの設定
     */
    public void setOnButtonClickListener(RecommendListRecyclerAdapter.OnButtonClickListener listener) {
        mListener = listener;
    }
    //************************************************************************************

}
