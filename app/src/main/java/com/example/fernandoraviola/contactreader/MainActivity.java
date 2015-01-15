package com.example.fernandoraviola.contactreader;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public static class PlaceholderFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
        // An adapter that binds the result Cursor to the ListView
        private SimpleCursorAdapter mCursorAdapter;

        /*
         * Defines an array that contains column names to move from
         * the Cursor to the ListView.
         */
        @SuppressLint("InlinedApi")
        private final static String[] FROM_COLUMNS = {
                Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                        ContactsContract.Contacts.DISPLAY_NAME
        };

        private final static int[] TO_IDS = {
                android.R.id.text1
        };

        @SuppressLint("InlinedApi")
        private static final String[] PROJECTION =
                {
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.LOOKUP_KEY,
                        Build.VERSION.SDK_INT
                                >= Build.VERSION_CODES.HONEYCOMB ?
                                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                                ContactsContract.Contacts.DISPLAY_NAME
                };

        // Defines the text expression
        @SuppressLint("InlinedApi")
        private static final String SELECTION =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
                        ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";

        // Defines a variable for the search string
        private String mSearchString = "";
        // Defines the array to hold values that replace the ?
        private String[] mSelectionArgs = { mSearchString };


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mCursorAdapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, null,
                    FROM_COLUMNS, TO_IDS,
                    0);
            setListAdapter(mCursorAdapter);

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getLoaderManager().initLoader(0, null, this);
        }

        /**
         * Loaders callback
         */
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            /*
             * Makes search string into pattern and
             * stores it in the selection array
             */
            mSelectionArgs[0] = "%" + mSearchString + "%";
            // Starts the query
            return new CursorLoader(
                    getActivity(),
                    ContactsContract.Contacts.CONTENT_URI,
                    PROJECTION,
                    SELECTION,
                    mSelectionArgs,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            // Put the result Cursor in the adapter for the ListView
            mCursorAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            // Delete the reference to the existing Cursor
            mCursorAdapter.swapCursor(null);
        }
    }
}
