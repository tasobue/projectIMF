package imf.lin.android.imf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by stmac0001 on 2017/03/26.
 */

public class ScheduleListActivity extends AppCompatActivity implements SearchListFragment.MyListener{
    private RecyclerView recyclerView;
    private ScheduleListRecyclerAdapter adapter;
    private SlidingPaneLayout mSlidingLayout;
    private SearchListFragment _searchListFragment;
    private int backPosition;

    //************************************************************************************
    //Listener Event
    @Override
    public void onClickButton(String text ) {
        mSlidingLayout.openPane();
        adapter.addAtPosition(backPosition, text);
        adapter.removeAtPosition(backPosition +1 );
    }
    //************************************************************************************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set XML Layout at first
        setContentView(R.layout.layout_schedule_list);

        mSlidingLayout = (SlidingPaneLayout) findViewById(R.id.sliding_layout);

        // スライドアウトしたペインのフェードに使う色をセット
        mSlidingLayout.setSliderFadeColor(Color.TRANSPARENT);

        mSlidingLayout.openPane();
        setRecyclerView();

    }

    private void setRecyclerView(){
        recyclerView = (RecyclerView)findViewById(R.id.imf_schedule_recycler_view);
        recyclerView.setHasFixedSize(true);

        //setAdapter
        adapter = new ScheduleListRecyclerAdapter(DummyDataGenerator.generateStringListData());

        recyclerView.setAdapter(adapter);

        //implement ItemTouchHelper to swipe and Drug&Drop
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // ドラッグアンドドロップ時
                adapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.RIGHT){
                    // アイテム右スワイプ時
                    adapter.removeAtPosition(viewHolder.getAdapterPosition());
                }
                if(direction == ItemTouchHelper.LEFT){
                    backPosition  = viewHolder.getAdapterPosition();

                    mSlidingLayout.closePane();

                    String cond = "稲沢市";

                    android.app.Fragment f = SearchListFragment.getInstance(cond);

                    getFragmentManager().beginTransaction().replace(R.id.content_container, f)
                            .commit();
                }

            }
        }).attachToRecyclerView(recyclerView);

    }

    public static Intent createIntent(Context context) {
        return new Intent(context, ScheduleListActivity.class);
    }

    //******************************************************************************
//    public interface OnItemSelectedListener {
//        public void onItemSelected();
//    }
//
//    private OnItemSelectedListener _listener;
//
//    public void setListener(OnItemSelectedListener listener){
//        _listener = listener;
//    }
//
//    public void method(){
//        setContentView(R.layout.layout_schedule_list);
//        mSlidingLayout = (SlidingPaneLayout) findViewById(R.id.sliding_layout);
//        mSlidingLayout.openPane();
//    }
    //******************************************************************************


}
