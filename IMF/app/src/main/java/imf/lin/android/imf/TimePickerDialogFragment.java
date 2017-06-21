package imf.lin.android.imf;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by stmac0001 on 2017/06/04.
 */

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private static final String POSITION = "position";

    public static TimePickerDialogFragment getNewInstance(int position){
        TimePickerDialogFragment f = new TimePickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
        return timePickerDialog;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //時刻が選択されたときの処理
        mScheduleTimeSetListener.onScheduleTimeSet(getArguments().getInt(POSITION), hourOfDay, minute);
    }

    private OnScheduleTimeSetListener mScheduleTimeSetListener;

    public void setOnScheduleTimeSetListener(OnScheduleTimeSetListener listener){
        mScheduleTimeSetListener = listener;
    }

    public static interface OnScheduleTimeSetListener{
        public void onScheduleTimeSet(int position, int hourOfDay, int minute);
    }

}
