package com.digitalexperts.bookyachts.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.payment_utility.AvenuesParams;
import com.digitalexperts.bookyachts.payment_utility.Constants;
import com.digitalexperts.bookyachts.payment_utility.RSAUtility;
import com.digitalexperts.bookyachts.payment_utility.ServiceHandler;
import com.digitalexperts.bookyachts.payment_utility.ServiceUtility;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import java.net.URLEncoder;

public class WebViewActivity extends Activity {
	private ProgressDialog dialog;
	Intent mainIntent;
	String html, encVal;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_web_view);
        Log.e("okay_hai","okay");
		mainIntent = getIntent();


        Log.e("okay_hai","okay");
		// Calling async task to get display content
		new RenderView().execute();
	}
	
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class RenderView extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			dialog = new ProgressDialog(WebViewActivity.this);
			dialog.setMessage("Please wait...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
	
			// Making a request to url and getting response

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
			params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));

         //   Log.e("tag",mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL));
           // Log.e("tag",params.toString());
			String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), ServiceHandler.POST, params);

         //   Log.e("tag",vResponse);

			if(!ServiceUtility.chkNull(vResponse).equals("")
					&& ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR")==-1 ){
				StringBuffer vEncVal = new StringBuffer("");
				vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
				vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));

				encVal = RSAUtility.encrypt(vEncVal.substring(0,vEncVal.length()-1), vResponse);
				Log.e("encrypted",encVal);

			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (dialog.isShowing())
				dialog.dismiss();
			
			@SuppressWarnings("unused")
			class MyJavaScriptInterface
			{
				@JavascriptInterface
			    public void processHTML(String html)
			    {
			        // process the html as needed by the app
			    	String status = null;
			    	if(html.indexOf("Failure")!=-1){
			    		status = "Transaction Declined!";
			    	}else if(html.indexOf("Success")!=-1){
			    		status = "Transaction Successful!";
			    	}else if(html.indexOf("Aborted")!=-1){
			    		status = "Transaction Cancelled!";
			    	}else{
			    		status = "Status Not Known!";
			    	}
			    	//Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
			    	Intent intent = new Intent(getApplicationContext(),StatusActivity.class);
					intent.putExtra("transStatus", status);
					startActivity(intent);
					finish();
			    }

			}
			
			final WebView webview = (WebView) findViewById(R.id.webview);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
			webview.setWebViewClient(new WebViewClient(){
				@Override
	    	    public void onPageFinished(WebView view, String url) {
	    	        super.onPageFinished(webview, url);
	    	        if(url.indexOf("/ccavResponseHandler.php")!=-1){
	    	        	webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
	    	        }
	    	    }  

	    	    @Override
	    	    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	    	        Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
	    	    }
			});

            StringBuffer params;
            params = new StringBuffer();
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE,mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID,mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID,mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL,mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL,mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));

            params.append(ServiceUtility.addToPostParams("billing_name",mainIntent.getStringExtra("billing_name")));
            params.append(ServiceUtility.addToPostParams("billing_city",mainIntent.getStringExtra("billing_city")));
            params.append(ServiceUtility.addToPostParams("billing_state",mainIntent.getStringExtra("billing_state")));
            params.append(ServiceUtility.addToPostParams("billing_country",mainIntent.getStringExtra("billing_country")));
            params.append(ServiceUtility.addToPostParams("billing_zip",mainIntent.getStringExtra("billing_zip")));
            params.append(ServiceUtility.addToPostParams("billing_tel",mainIntent.getStringExtra("billing_tel")));
            params.append(ServiceUtility.addToPostParams("billing_email",mainIntent.getStringExtra("billing_email")));
            params.append(ServiceUtility.addToPostParams("billing_address",mainIntent.getStringExtra("billing_address")));
			/* An instance of this class will be registered as a JavaScript interface */
			Log.e("params",params.toString());

			try{
			String vPostParams = params.substring(0,params.length()-1);

				webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
			} catch (Exception e) {
				showToast("Exception occured while opening webview.");
			}
		}
	}
	
	public void showToast(String msg) {
		Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
	}
}

