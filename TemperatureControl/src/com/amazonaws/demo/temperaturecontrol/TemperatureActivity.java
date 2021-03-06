/**
 * Copyright 2010-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *    http://aws.amazon.com/apache2.0
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and
 * limitations under the License.
 */

package com.amazonaws.demo.temperaturecontrol;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.iotdata.AWSIotDataClient;
import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.GetThingShadowResult;
import com.amazonaws.services.iotdata.model.UpdateThingShadowRequest;
import com.amazonaws.services.iotdata.model.UpdateThingShadowResult;
import com.google.gson.Gson;

import java.nio.ByteBuffer;

public class TemperatureActivity extends Activity {

    private static final String LOG_TAG = TemperatureActivity.class.getCanonicalName();

    // --- Constants to modify per your configuration ---

    // Customer specific IoT endpoint
    // AWS Iot CLI describe-endpoint call returns: XXXXXXXXXX.iot.<region>.amazonaws.com
    private static final String CUSTOMER_SPECIFIC_ENDPOINT = "a2yc7w1ut7zbzi.iot.us-east-2.amazonaws.com";
    // Cognito pool ID. For this app, pool needs to be unauthenticated pool with
    // AWS IoT permissions.
    private static final String COGNITO_POOL_ID = "us-east-2:90173c29-9128-4867-b172-e6d54e98b8d4";
    // Region of AWS IoT
    private static final Regions MY_REGION = Regions.US_EAST_2;

    CognitoCachingCredentialsProvider credentialsProvider;

    AWSIotDataClient iotDataClient;
//    String resString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Amazon Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                COGNITO_POOL_ID, // Identity Pool ID
                MY_REGION // Region
        );

        iotDataClient = new AWSIotDataClient(credentialsProvider);
        String iotDataEndpoint = CUSTOMER_SPECIFIC_ENDPOINT;
        iotDataClient.setEndpoint(iotDataEndpoint);

//        NumberPicker np = (NumberPicker) findViewById(R.id.setpoint);
//        np.setMinValue(60);
//        np.setMaxValue(80);
//        np.setWrapSelectorWheel(false);

        getShadows();
       // System.out.println("getShadows()");
    }

    public void temperatureStatusUpdated(String temperatureStatusState) {
        Gson gson = new Gson();
        TemperatureStatus ts = gson.fromJson(temperatureStatusState, TemperatureStatus.class);
        Log.i(LOG_TAG, String.format("intTemp:  %s", ts.state.reported.heart_rate));
       Log.i(LOG_TAG, String.format("extTemp:  %s", ts.state.reported.position));
//        Log.i(LOG_TAG, String.format("intTemp:  %d", ts.state.desired.intTemp));
//        Log.i(LOG_TAG, String.format("extTemp:  %d", ts.state.desired.extTemp));
//        Log.i(LOG_TAG, String.format("curState: %s", ts.state.desired.curState));
//
       TextView intTempText = (TextView) findViewById(R.id.intTemp);
       intTempText.setText(ts.state.reported.heart_rate);

        TextView intTempText2 = (TextView) findViewById(R.id.textView2);
        intTempText2.setText(ts.state.reported.position);
        final String positionStr=ts.state.reported.position;
        ImageView dangerImage=(ImageView) findViewById(R.id.imgresponse);
        if(positionStr.contentEquals("DANGER"))
        dangerImage.setBackgroundResource(R.drawable.danger);
        else
        dangerImage.setBackgroundResource(R.drawable.safe);


//        TextView extTempText = (TextView) findViewById(R.id.extTemp);
//        extTempText.setText(ts.state.desired.alarm);

//       // intTempText.setText(ts.state.desired.intTemp.toString());
//        if ("stopped".equals(ts.state.desired.curState)) {
//            intTempText.setTextColor(getResources().getColor(R.color.colorOff));
//        } else if ("heating".equals(ts.state.desired.curState)) {
//            intTempText.setTextColor(getResources().getColor(R.color.colorHeating));
//        } else if ("cooling".equals(ts.state.desired.curState)) {
//            intTempText.setTextColor(getResources().getColor(R.color.colorCooling));
//        }
//
//        TextView extTempText = (TextView) findViewById(R.id.extTemp);
//        extTempText.setText(ts.state.desired.extTemp.toString());
    }

    public void temperatureControlUpdated(String temperatureControlState) {
        Gson gson = new Gson();
        TemperatureControl tc = gson.fromJson(temperatureControlState, TemperatureControl.class);
       // System.out.println("TemperatureControl");
       // Log.i(LOG_TAG, String.format("robo:  %s", tc.state.desired.robot));
       // System.out.println("%s");


       //TextView extTempText = (TextView) findViewById(R.id.extTemp);
       //extTempText.setText(tc.state.desired.forward);

       // TextView roboText = (TextView) findViewById(R.id.robo);
        //roboText.setText(tc.state.desired.robot);
//
//        Log.i(LOG_TAG, String.format("setPoint: %d", tc.state.desired.setPoint));
//        Log.i(LOG_TAG, String.format("enabled: %b", tc.state.desired.enabled));

//        NumberPicker np = (NumberPicker) findViewById(R.id.setpoint);
//        np.setValue(tc.state.desired.setPoint);

        //ToggleButton tb1 = (ToggleButton) findViewById(R.id.enableButton);
        //tb1.setChecked(tc.state.desired.enabled);
    }

    public void getShadow(View view) {
        getShadows();
    }

    public void getShadows() {

        GetShadowTask getStatusShadowTask = new GetShadowTask("accelerometer");
        getStatusShadowTask.execute();

        //GetShadowTask getControlShadowTask = new GetShadowTask("robot");
        //getControlShadowTask.execute();

//        System.out.println("robot");
//        GetShadowTask getStatusShadowTask = new GetShadowTask("TemperatureStatus");
//        getStatusShadowTask.execute();
//
//        GetShadowTask getControlShadowTask = new GetShadowTask("TemperatureControl");
//        getControlShadowTask.execute();
    }


//    public void enableDisableClicked(View view) {
//        ToggleButton tb = (ToggleButton) findViewById(R.id.enableButton);
//
//        Log.i(LOG_TAG, String.format("System %s", tb.isChecked() ? "ON" : "OFF"));
//        UpdateShadowTask updateShadowTask = new UpdateShadowTask();
//        updateShadowTask.setThingName("led_trial");
//
//        //String newState="{\"state\":{\"desired\":{\"lightmode\":\"ON\"}}}";
//
//       String newState = String.format("{\"state\":{\"desired\":{\"heart_rate\":\"%s\"}}}",
//              tb.isChecked() ? "ON" : "OFF");
//
////        String newState = String.format("{\"state\":{\"desired\":{\"enabled\":%s}}}",
////                tb.isChecked() ? "true" : "false");
//        Log.i(LOG_TAG, newState);
//        updateShadowTask.setState(newState);
//        updateShadowTask.execute();
//    }


//    public void enableDisableBuzzer(View view) {
//        ToggleButton tb1 = (ToggleButton) findViewById(R.id.enableButton1);
//
//        Log.i(LOG_TAG, String.format("System %s", tb1.isChecked() ? "ON" : "OFF"));
//        UpdateShadowTask updateShadowTask = new UpdateShadowTask();
//        updateShadowTask.setThingName("led_trial");
//
//       // String newState="{\"state\":{\"desired\":{\"forward\":\"OFF\"}}}";
//
//        String newState = String.format("{\"state\":{\"desired\":{\"alarm\":\"%s\"}}}",
//                tb1.isChecked() ? "ON" : "OFF");
//          System.out.println(tb1.isChecked());
////        String newState = String.format("{\"state\":{\"desired\":{\"enabled\":%s}}}",
////                tb.isChecked() ? "true" : "false");
//        Log.i(LOG_TAG, newState);
//        updateShadowTask.setState(newState);
//        updateShadowTask.execute();
//    }

//   public void updateSetpoint(View view) {
////       // NumberPicker np = (NumberPicker) findViewById(R.id.setpoint);
////       // Integer newSetpoint = np.getValue();
////        //Log.i(LOG_TAG, "New setpoint:" + newSetpoint);
//       UpdateShadowTask updateShadowTask = new UpdateShadowTask();
//        updateShadowTask.setThingName("led_trial");
////       // String newState = String.format("{\"state\":{\"desired\":{\"setPoint\":%d}}}", newSetpoint);
////       // Log.i(LOG_TAG, newState);
//        // updateShadowTask.setState(newState);
//          updateShadowTask.execute();
//    }

//    public void updateRobot(View view)
//    {
//        UpdateShadowTask updateShadowTask = new UpdateShadowTask();
//        updateShadowTask.setThingName("robot");
//        updateShadowTask.execute();
//    }

    private class GetShadowTask extends AsyncTask<Void, Void, AsyncTaskResult<String>> {

        private final String thingName;

        public GetShadowTask(String name) {
            thingName = name;
        }

        @Override
        protected AsyncTaskResult<String> doInBackground(Void... voids) {
            try {
                GetThingShadowRequest getThingShadowRequest = new GetThingShadowRequest()
                        .withThingName(thingName);
                System.out.println("thingName");
                GetThingShadowResult result = iotDataClient.getThingShadow(getThingShadowRequest);
                byte[] bytes = new byte[result.getPayload().remaining()];
                result.getPayload().get(bytes);
                String resultString = new String(bytes);
                return new AsyncTaskResult<String>(resultString);
            } catch (Exception e) {
                Log.e("E", "getShadowTask", e);
                return new AsyncTaskResult<String>(e);
            }

        }

        @Override
        protected void onPostExecute(AsyncTaskResult<String> result) {
            if (result.getError() == null) {
                Log.i(GetShadowTask.class.getCanonicalName(), result.getResult());
                if ("accelerometer".equals(thingName)) {
                    temperatureStatusUpdated(result.getResult());
                } else if ("LightControl".equals(thingName)) {
                    temperatureControlUpdated(result.getResult());
                }
//               else if ("power_control".equals(thingName))
//                {
//                    temperatureControlUpdated(result.getResult());
//                }
              ///  System.Out.Println(getResult());
            } else {
                Log.e(GetShadowTask.class.getCanonicalName(), "getShadowTask", result.getError());
            }
        }

    }

    private class UpdateShadowTask extends AsyncTask<Void, Void, AsyncTaskResult<String>> {

        private String thingName;
        private String updateState;

        public void setThingName(String name) {
            thingName = name;
        }

        public void setState(String state) {
            updateState = state;
        }

        @Override
        protected AsyncTaskResult<String> doInBackground(Void... voids) {
            try {
                UpdateThingShadowRequest request = new UpdateThingShadowRequest();
                request.setThingName(thingName);

                ByteBuffer payloadBuffer = ByteBuffer.wrap(updateState.getBytes());
                request.setPayload(payloadBuffer);

                UpdateThingShadowResult result = iotDataClient.updateThingShadow(request);

                byte[] bytes = new byte[result.getPayload().remaining()];
                result.getPayload().get(bytes);
                String resultString = new String(bytes);
                return new AsyncTaskResult<String>(resultString);
            } catch (Exception e) {
                Log.e(UpdateShadowTask.class.getCanonicalName(), "updateShadowTask", e);
                return new AsyncTaskResult<String>(e);
            }
        }

        @Override
        protected void onPostExecute(AsyncTaskResult<String> result) {
            if (result.getError() == null) {
                Log.i(UpdateShadowTask.class.getCanonicalName(), result.getResult());
            } else {
                Log.e(UpdateShadowTask.class.getCanonicalName(), "Error in Update Shadow",
                        result.getError());
            }
        }
    }
}
