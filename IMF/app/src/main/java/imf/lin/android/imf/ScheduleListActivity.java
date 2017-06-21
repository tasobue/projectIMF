package imf.lin.android.imf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import static imf.lin.android.imf.TimePickerDialogFragment.getNewInstance;

/**
 * Created by stmac0001 on 2017/03/26.
 */

public class ScheduleListActivity extends AppCompatActivity
        implements  SearchListFragment.MyListener,
                    ScheduleListRecyclerAdapter.OnItemClickListener,
                    TimePickerDialogFragment.OnScheduleTimeSetListener{
    private RecyclerView recyclerView;
    private ScheduleListRecyclerAdapter adapter;
    private SlidingPaneLayout mSlidingLayout;
    private SearchListFragment _searchListFragment;
    private int backPosition;

    private LocationFragment mLocationFragment;

    //************************************************************************************
    //Listener Event
    @Override
    public void onClickButton(String text ) {
        mSlidingLayout.openPane();
        adapter.addAtPosition(backPosition, text);
        adapter.removeAtPosition(backPosition +1 );
    }

    @Override
    public void onItemClick(ScheduleListRecyclerAdapter adapter, int position){
        TimePickerDialogFragment timePicker = getNewInstance(position);
        timePicker.setOnScheduleTimeSetListener(this);
        timePicker.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onScheduleTimeSet(int position, int hourOfDay, int minute){
        adapter.setTime(position, String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
    };
    //************************************************************************************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //*********Main Layout
        setContentView(R.layout.layout_schedule_list);

        //*********LocationFragment
        mLocationFragment = (LocationFragment) getSupportFragmentManager().findFragmentByTag(LocationFragment.TAG);
        if (mLocationFragment == null) {
            mLocationFragment = mLocationFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(mLocationFragment, LocationFragment.TAG)
                    .commit();
        }

        //*********SlidinPanLayOut***********
        mSlidingLayout = (SlidingPaneLayout) findViewById(R.id.sliding_layout);


        // スライドアウトしたペインのフェードに使う色をセット
        mSlidingLayout.setSliderFadeColor(Color.TRANSPARENT);

        mSlidingLayout.openPane();

        //*********RecyclerView************
        setRecyclerView();

    }

    private void setRecyclerView(){
        recyclerView = (RecyclerView)findViewById(R.id.imf_schedule_recycler_view);
        recyclerView.setHasFixedSize(true);

        //setAdapter
        adapter = new ScheduleListRecyclerAdapter(DummyDataGenerator.generateStringListData(), DummyDataGenerator.generateTimeList());
        adapter.setOnItemClickListener(this);

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
                    adapter.changeAtPosition(viewHolder.getAdapterPosition());
                }
                if(direction == ItemTouchHelper.LEFT){
                    backPosition  = viewHolder.getAdapterPosition();

                    mSlidingLayout.closePane();

                    String cond = adapter.getText(backPosition);

                    android.app.Fragment f = SearchListFragment.getInstance(cond, mLocationFragment.getdblLat(), mLocationFragment.getdblLong());

                    getFragmentManager().beginTransaction().replace(R.id.content_container, f)
                            .commit();
                }

            }
        }).attachToRecyclerView(recyclerView);

    }

    public static Intent createIntent(Context context) {
        return new Intent(context, ScheduleListActivity.class);
    }


}
