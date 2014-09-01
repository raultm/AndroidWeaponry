package com.raulete.androidweaponry;

import android.content.Context;

/**
 * Created by raulete on 20/01/14.
 */
public interface AsyncCommand {

    public void run(Context context);
    public void run(Context context, String param);

}
