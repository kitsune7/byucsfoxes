package com.foxninja.familymap.web;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.foxninja.familymap.R;
import com.foxninja.familymap.base.MapFragment;
import com.foxninja.familymap.model.FamilyMapData;
import com.foxninja.familymap.model.UserInfo;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Used for all HttpRequests used by the app
 */
public class HttpRequest extends FragmentActivity {
    private Context context;
    private String base_url;

    public HttpRequest(Context context, String hostname, String port) {
        this.context = context;

        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(hostname);
        sb.append(":");
        sb.append(port);
        base_url = sb.toString();
    }


    private String post(String url_string, String post_data, Map<String,String> headers) {
        try {
            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set HTTP request headers, if necessary
            for (Map.Entry<String, String> entry: headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }

            connection.connect();

            // Write post data to request body
            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(post_data.getBytes());
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get HTTP response headers, if necessary
                // Map<String, List<String>> headers = connection.getHeaderFields();

                // Get response body input stream
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                return baos.toString();
            }
            else {
                System.out.println("Bad response code!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showToast(String toast) {
        Toast.makeText(
                context,
                toast,
                Toast.LENGTH_LONG
        ).show();
    }


    public void login(String username, String password) {
        StringBuilder sb = new StringBuilder();
        sb.append(base_url);
        sb.append("/user/login");
        String url_string = sb.toString();

        sb.setLength(0);
        sb.append("{");
        sb.append("username:\"");
        sb.append(username);
        sb.append("\",password:\"");
        sb.append(password);
        sb.append("\"}");
        String post_data = sb.toString();

        LoginTask login_task = new LoginTask();
        login_task.execute(url_string, post_data);
    }


    public class LoginTask extends AsyncTask<String,String,String> {
        /**
         * Attempts to login the user.
         * @param params [0]: String url_string, [1]: String post_data
         * @return Server response
         */
        @Override
        protected String doInBackground(String... params) {
            return post(params[0], params[1], new HashMap<String, String>());
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
            try {
                JSONObject object = new JSONObject(result);
                if (object.has("message")) {
                    showToast(object.getString("message"));
                } else {
                    UserInfo info = new UserInfo(
                            object.getString("Authorization"),
                            object.getString("userName"),
                            object.getString("personId")
                    );
                    FamilyMapData instance = FamilyMapData.getInstance(info);

                    // Welcome the user
                    GetUserFullName getUserFullName = new GetUserFullName();
                    getUserFullName.execute(base_url+"/person/"+instance.getPersonId(),"");

                    // Change to the map fragment
                    FragmentManager fm = getSupportFragmentManager();
                    Fragment fragment = new MapFragment();
                    fm.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class GetUserFullName extends AsyncTask<String,String,String> {
        /**
         * Gets the user's login data
         * @param params [0]: String url_string, [1]: String post_data
         * @return Server response
         */
        @Override
        protected String doInBackground(String... params) {
            Map<String,String> headers = new HashMap<>();
            FamilyMapData instance = FamilyMapData.getInstance();

            headers.put("Authorization", instance.getAuthorization());
            return post(params[0], params[1], headers);
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
            try {
                JSONObject object = new JSONObject(result);
                if (object.has("firstName") && object.has("lastName")) {
                    showToast("Welcome "+object.get("firstName")+" "+object.get("lastName")+"!");
                } else {
                    System.out.println("Could not get first and last name for some reason...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
