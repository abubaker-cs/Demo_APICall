package org.abubaker.demo_apicall

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Here we will call the asyncTask class for the api calling and pass the required parameters to it.
        CallAPILoginAsyncTask("Panjutorials", "123456").execute()
    }

    /**
     * Inner Class
     * ===========
     * “A nested class marked as inner can access the members of its outer class.
     * Inner classes carry a reference to an object of an outer class:”JSON
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
         * This function is for the task which we wants to perform >>>> before <<<< background execution.
         * Here we have shown the progress dialog to user that UI is not freeze but executing something in background.
         */
        override fun onPreExecute() {
            super.onPreExecute()

            // We want to show the progress before the background task even starts
            showProgressDialog()
        }


        /** 02
         * This function will be used to perform background execution.
         */
        override fun doInBackground(vararg params: Any?): String {
            var result: String

            /**
             * https://developer.android.com/reference/java/net/HttpURLConnection
             *
             * You can use the above url for Detail understanding of HttpURLConnection class
             */
            var connection: HttpURLConnection? = null


            try {

                // Path
                val url = URL("http://www.mocky.io/v2/5e3826143100006a00d37ffa")

                // Establish the connection
                connection = url.openConnection() as HttpURLConnection

                /**
                 * A URL connection can be used for input and/or output.  Set the DoOutput
                 * flag to true if you intend to use the URL connection for output,
                 * false if not.  The default is false.
                 */
                connection.doOutput = true // Do we GET data?
                connection.doInput = true // Do we SEND data?

                /**
                 * Sets whether HTTP redirects should be automatically followed by this instance.
                 * The default value comes from followRedirects, which defaults to true.
                 */
                connection.instanceFollowRedirects = false

                /**
                 * Set the method for the URL request, one of:
                 *  GET
                 *  POST
                 *  HEAD
                 *  OPTIONS
                 *  PUT
                 *  DELETE
                 *  TRACE
                 *  are legal, subject to protocol restrictions.  The default method is GET.
                 */
                connection.requestMethod = "POST"

                /**
                 * Sets the general request property. If a property with the key already
                 * exists, overwrite its value with the new value.
                 */
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("charset", "utf-8")
                connection.setRequestProperty("Accept", "application/json")

                /**
                 * Some protocols do caching of documents.  Occasionally, it is important
                 * to be able to "tunnel through" and ignore the caches (e.g., the
                 * "reload" button in a browser).  If the UseCaches flag on a connection
                 * is true, the connection is allowed to use whatever caches it can.
                 *  If false, caches are to be ignored.
                 *  The default value comes from DefaultUseCaches, which defaults to
                 * true.
                 */
                connection.useCaches = false

                /**
                 * Creates a new data output stream to write data to the specified
                 * underlying output stream. The counter written is set to zero.
                 */
                val wr = DataOutputStream(connection.outputStream)

                // Create JSONObject Request
                val jsonRequest = JSONObject()
                jsonRequest.put("username", username) // Request Parameter 1
                jsonRequest.put("password", password) // Request Parameter 2

                /**
                 * Writes out the string to the underlying output stream as a
                 * sequence of bytes. Each character in the string is written out, in
                 * sequence, by discarding its high eight bits. If no exception is
                 * thrown, the counter written is incremented by the
                 * length of s.
                 */
                wr.writeBytes(jsonRequest.toString())
                wr.flush() // Flushes this data output stream.
                wr.close() // Closes this output stream and releases any system resources associated with the stream

                val httpResult: Int =
                    connection.responseCode // Gets the status code from an HTTP response message.

                if (httpResult == HttpURLConnection.HTTP_OK) {

                    /**
                     * Returns an input stream that reads from this open connection.
                     */
                    val inputStream = connection.inputStream

                    /**
                     * Creates a buffering character-input stream that uses a default-sized input buffer.
                     */

                    // BufferedReader
                    val reader = BufferedReader(
                        InputStreamReader(inputStream)
                    )

                    // Our StringBuilder : It will allow us to build strings
                    val sb = StringBuilder()

                    //We will use this variable (line) to go through individual lines
                    var line: String?


                    try {
                        /**
                         * Reads a line of text.  A line is considered to be terminated by any one
                         * of a line feed ('\n'), a carriage return ('\r'), or a carriage return
                         * followed immediately by a linefeed.
                         */
                        while (reader.readLine().also { line = it } != null) {
                            sb.append(line + "\n")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        try {
                            /**
                             * Closes this input stream and releases any system resources associated
                             * with the stream.
                             */
                            inputStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                    // If the connection was successful
                    result = sb.toString()
                } else {

                    // If the connection was failed
                    /**
                     * Gets the HTTP response message, if any, returned along with the
                     * response code from a server.
                     */
                    result = connection.responseMessage
                }

            } catch (e: SocketTimeoutException) {

                // Timeout
                result = "Connection Timeout"

            } catch (e: Exception) {

                // General
                result = "Error : " + e.message

            } finally {

                // Disconnect the Connection
                connection?.disconnect()

            }

            // You can notify with your result to onPostExecute.
            return result
        }


        /**
         * This function will be executed after the background execution is completed.
         */
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            cancelProgressDialog()

            Log.i("JSON Response Result", result)

            /**
             *Creates a new with name/value mappings from the JSON string.
             */
            val jsonObject = JSONObject(result)

            // Returns the value mapped by {name} if it exists.
            val message = jsonObject.optString("message")
            Log.i("Message", message)

            // Returns the value mapped by {name} if it exists.
            val userId = jsonObject.optInt("user_id")
            Log.i("User Id", "$userId")

            // Returns the value mapped by {name} if it exists.
            val name = jsonObject.optString("name")
            Log.i("Name", "$name")

            // Returns the value mapped by {name} if it exists.
            val email = jsonObject.optString("email")
            Log.i("Email", "$email")

            // Returns the value mapped by {name} if it exists.
            val mobileNumber = jsonObject.optLong("mobile")
            Log.i("Mobile", "$mobileNumber")

            // Returns the value mapped by {name} if it exists.
            val profileDetailsObject = jsonObject.optJSONObject("profile_details")

            val isProfileCompleted = profileDetailsObject.optBoolean("is_profile_completed")
            Log.i("Is Profile Completed", "$isProfileCompleted")

            val rating = profileDetailsObject.optDouble("rating")
            Log.i("Rating", "$rating")

            // Returns the value mapped by {name} if it exists.
            val dataListArray = jsonObject.optJSONArray("data_list")
            Log.i("Data List Size", "${dataListArray.length()}")

            for (item in 0 until dataListArray.length()) {
                Log.i("Value $item", "${dataListArray[item]}")

                // Returns the value mapped by {name} if it exists.
                val dataItemObject: JSONObject = dataListArray[item] as JSONObject

                val id = dataItemObject.optString("id")
                Log.i("ID", "$id")

                val value = dataItemObject.optString("value")
                Log.i("Value", "$value")
            }

            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
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