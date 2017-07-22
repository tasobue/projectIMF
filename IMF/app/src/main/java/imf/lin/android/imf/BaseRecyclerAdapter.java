package imf.lin.android.imf;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by stmac0001 on 2017/06/24.
 */

public abstract class BaseRecyclerAdapter <VH extends BaseRecyclerAdapter.ViewHolder> extends RecyclerView.Adapter<VH>  {
    protected ArrayList<CharSequence> dataset;

    /** getItemCount */
    @Override
    public int getItemCount(){
        return dataset.size();
    }
    private int mView;
    private int mLayout;

    /** Constructor */
    public BaseRecyclerAdapter(ArrayList<CharSequence> _dataset, int _layout){
        super();
        dataset = _dataset;
        mLayout = _layout;
    }

    /** ViewHolder innerClass*/
    public static abstract class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View v){
            super(v);
        }
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(VH holder, int position);

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

    //データの内容を変更する
    public void changeText(int position, String addText){
        String text = dataset.get(position).toString();
        dataset.remove(position);
        notifyItemRemoved(position);
        dataset.add(position, addText + "  " + text);
        notifyItemInserted(position);
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

}
