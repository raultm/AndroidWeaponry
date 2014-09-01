package com.raulete.androidweaponry;

import android.content.Context;
import android.os.AsyncTask;


/**
 * Created by raulete on 20/01/14.
 */
public class AsyncCommandRunner {

    
    public static void execute(AsyncCommand command, Context context) {
        async(command, context);
    }

    public static void execute(AsyncCommand command, Context context, String param) {
        async(command, context, param);
    }


    private static void async(final AsyncCommand command, final Context context) {
        new AsyncTask() {
	        @Override
            protected Object doInBackground(Object[] params) {
                command.run(context);
                return null;
            }
        }.execute();
    }


    private static void async(final AsyncCommand command, final Context context, final String param) {
        new AsyncTask() {
			@Override
            protected Object doInBackground(Object[] params) {
                command.run(context, param);
                return null;
            }
        }.execute();
    }

}
