package com.kanmanus.kmutt.sit.ijoint;

/**
 * Created by kanmanus on 1/14/15 AD.
 */
public class Function {
    /*protected static String jsonParse(String url, List<NameValuePair> nameValuePairs) throws IOException, JSONException{
        *//*HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        JSONObject jObject = null;
        String result = null;
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            //String responseText = EntityUtils.toString(response.getEntity());

            InputStream inputStream = entity.getContent();

            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            result = sb.toString();
            inputStream.close();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        }

        return result;*//*
        return "";
    }*/
}
