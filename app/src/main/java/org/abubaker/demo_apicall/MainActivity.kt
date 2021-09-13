package org.abubaker.demo_apicall

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Here we will call the asyncTask class for the api calling and pass the required parameters to it.
        // CallAPILoginAsyncTask("Panjutorials", "123456").execute()
    }

    /**
     * Inner Class
     * ===========
     * “A nested class marked as inner can access the members of its outer class.
     * Inner classes carry a reference to an object of an outer class:”
     * source: https://kotlinlang.org/docs/reference/nested-classes.html
     *
     * This is the background class is used to execute background task.
     *
     * Depreciated:
     * ===========
     * For Background we have used the AsyncTask, which creates a new asynchronous task.
     * This constructor must be invoked on the UI thread.
     */
    @SuppressLint("StaticFieldLeak")
    private inner class CallAPILoginAsyncTask(val username: String, val password: String) :
        AsyncTask<Any, Void, String>() {

        // A variable that will be used in creating a Custom Progress Dialog
        private lateinit var customProgressDialog: Dialog

        /** 01
         * This function is for the task which we wants to perform before background execution.
         * Here we have shown the progress dialog to user that UI is not freeze but executing something in background.
         */
        override fun onPreExecute() {
            super.onPreExecute()

            showProgressDialog()
        }


        /** 02
         * This function will be used to perform background execution.
         */
        override fun doInBackground(vararg params: Any?): String {

        }

        /** 03
         * Method is used to show the Custom Progress Dialog (XML)
         */
        private fun showProgressDialog() {

            // Define our Custom Dialog
            customProgressDialog = Dialog(this@MainActivity)


            /*Set the screen content from a layout resource.
            The resource will be inflated, adding all top-level views to the screen.*/
            customProgressDialog.setContentView(R.layout.dialog_custom_progress)

            //Start the dialog and display it on screen.
            customProgressDialog.show()

        }

        /** 04
         * This function is used to dismiss the progress dialog if it is visible to user.
         */
        private fun cancelProgressDialog() {

            // Cancels the Dialog
            customProgressDialog.dismiss()

        }

    }


}