package imf.lin.android.imf;

/**
 * Created by stmac0001 on 2017/06/25.
 */
import android.app.Application;

public class MyApplication  extends Application {
    private static MyApplication instance = null;
    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

}
