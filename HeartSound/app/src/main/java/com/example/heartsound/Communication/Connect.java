package com.example.heartsound.Communication;

import android.support.v7.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.heartsound.Basic_information.PersonFragment;
import com.example.heartsound.MainActivity2;
import com.example.heartsound.REST_API.ElasticRestClient;
import com.golife.contract.AppContract;
import com.golife.customizeclass.CareAlarm;
import com.golife.customizeclass.CareDoNotDisturb;
import com.golife.customizeclass.CareHRWarning;
import com.golife.customizeclass.CareIdleAlert;
import com.golife.customizeclass.CareMeasureHR;
import com.golife.customizeclass.ScanBluetoothDevice;
import com.golife.customizeclass.SetCareSetting;
import com.golife.database.table.TablePulseRecord;
import com.golife.database.table.TableSleepRecord;
import com.golife.database.table.TableSpO2Record;
import com.golife.database.table.TableStepRecord;
import com.goyourlife.gofitsdk.GoFITSdk;
import com.example.remotecamera.CameraClass;
import com.example.remotecamera.RemoteCamera;

import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import com.example.heartsound.R;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;

public class Connect extends AppCompatActivity {

    public static GoFITSdk _goFITSdk = null;
    public static String sdk_license = null;
    public static String sdk_certificate = null;
    private String mMacAddress = null;
    private String mPairingCode = null;
    private String mPairingTime = null;
    private String mProductID = null;
    SetCareSetting mCareSettings;
    public static String _tag = "activity_connect";
    private ScanBluetoothDevice mSelectDevice = null;
    private Button connect, next3;
    public static String subject = null;

    public enum SettingItem {
        STEP_TARGET,
        UNIT,
        TIME_FORMAT,
        AUTO_SHOW_SCREEN,
        SIT_REMINDER,
        BLE_DISCONNECT_NOTIFICATION,
        HANDEDNESS,
        ALARM_CLOCK,
        HR_TIMING_MEASURE,
        LANGUAGE,
        DND,
        SCREEN_LOCK,
        HR_WARNING
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        sdk_certificate = null;
        connect = (Button) findViewById(R.id.connect);
        next3 = (Button) findViewById(R.id.next3);
        next3.setEnabled(false);

        try {
            InputStream inputstream = this.getAssets().open("client_cert.crt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            sdk_certificate = sb.toString();
        } catch (Exception e) {
            Log.e(_tag, e.toString());
            //showToast("Exception : " + e.toString());
        }

//        String enabledListeners = android.provider.Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
//        if (enabledListeners == null || !enabledListeners.contains(NotificationListenerService.class.getName())) {
//            new AlertDialog.Builder(this)
//                    .setTitle("提示")
//                    .setMessage("如果您想使用智能手環通知功能，請在您的智能手機中打開設置的APP通知。 （將APP設置為白名單，防止被清理。）")
//                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            try {
//                                Connect.this.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).setNegativeButton("取消", null).show();
//        }

        CameraClass.checkPermission(AppContract.PermissionType.storage, this);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SDKinit();
                scan();

            }
        });


    }

    void showToast(String text) {
        Toast.makeText(Connect.this, text, Toast.LENGTH_SHORT).show();
    }

    public void SDKinit() {
        if (_goFITSdk == null) {
            // Read license if exist in local storage
            SharedPreferences sp;
            sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor pe = sp.edit();
            sdk_license = sp.getString("sdk_license", null);
            pe.apply();

            _goFITSdk = GoFITSdk.getInstance(this, sdk_certificate, sdk_license, new GoFITSdk.ReceivedLicenseCallback() {
                @Override
                public void onSuccess(String receivedLicense) {
                    Log.i(_tag, receivedLicense);
                    sdk_license = receivedLicense;

                    // Store license in local storage
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Connect.this);
                    SharedPreferences.Editor pe = sp.edit();
                    pe.putString("sdk_license", sdk_license);
                    pe.commit();


//                    showToast("SDK init OK : \n" + sdk_license);
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
//                    Log.e(_tag, "GoFITSdk.getInstance() : (callback) onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
//                    showToast("SDK init Error : \n" + errorMsg);
                }
            });
            _goFITSdk.reInitInstance();
            showToast("請再點擊一次連接裝置!按完之後須等待...");
//            showToast("SDK init!");
        } else {
            _goFITSdk.reInitInstance();
//            showToast("SDK init!");
        }
    }

    public void scan() {

        if (_goFITSdk != null) {

            // Demo - doScanDevice API
            _goFITSdk.doScanDevice(new GoFITSdk.DeviceScanCallback() {
                @Override
                public void onSuccess(ScanBluetoothDevice device) {
                    // TODO : TBD
//                    Log.i(_tag, "doScanDevice() : onSuccess() : device = " + device.getDevice().getName() + ", " + device.getDevice().getAddress() + ", " + device.getRSSI() + ", " + device.getProductID());
                    pairing();
                }

                @Override
                public void onCompletion(ArrayList<ScanBluetoothDevice> devices) {


                    for (ScanBluetoothDevice device : devices) {
                        Log.i(_tag, "doScanDevice() : onCompletion() : device = " + device.getDevice().getName() + ", " + device.getDevice().getAddress() + ", " + device.getRSSI() + ", " + device.getProductID());
                    }

                    if (devices.size() > 0) {
                        mSelectDevice = devices.get(0);
                        String summary = "Recommended Device : \n" + mSelectDevice.getDevice().getAddress() + ", " + mSelectDevice.getRSSI();

//                        showToast(summary);
//                        Log.i(_tag, "doScanDevice() : onCompletion() : mSelectDevice = " + mSelectDevice.getDevice().getName() + ", " + mSelectDevice.getDevice().getAddress() + ", " + mSelectDevice.getRSSI() + ", " + mSelectDevice.getProductID());
                    } else {
                        showToast("未找到裝置,請重新連接!");
                    }

                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
//                    Log.e(_tag, "doScanDevice() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
//                    showToast("doScanDevice() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }
            });
        } else {
//            showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    public boolean pairing() {
        if (_goFITSdk != null) {

            if (mSelectDevice != null) {
                mMacAddress = mSelectDevice.getDevice().getAddress();
            } else {
//                Toast.makeText(Status.this, "No Device Selected, `Scan Device` First!", Toast.LENGTH_SHORT).show();
                return true;
            }

            // Demo - doNewPairing API
            _goFITSdk.doNewPairing(mSelectDevice, new GoFITSdk.NewPairingCallback() {
                @Override
                public void onSuccess(String pairingCode, String pairingTime) {
                    Log.i(_tag, "doNewPairing() : onSuccess() : Got pairingCode = " + pairingCode);
                    Log.i(_tag, "doNewPairing() : onSuccess() : Confirming...");
                    mPairingCode = pairingCode;
                    mPairingTime = pairingTime;
                    mConfirmPairingCodeHandler.postDelayed(mConfirmPairingCodeRunnable, 5000);
                    connect();
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
//                    Log.e(_tag, "doNewPairing() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
//                    showToast("doNewPairing() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }
            });
        } else {
//            showToast("SDK Instance invalid, needs `SDK init`");
        }
        return false;
    }

    public void connect() {
        if (_goFITSdk != null) {


            // Demo - get connect information from local storage
            if (mMacAddress == null || mPairingCode == null || mPairingTime == null) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor pe = sp.edit();
                mMacAddress = sp.getString("macAddress", "");
                mPairingCode = sp.getString("pairCode", "");
                mPairingTime = sp.getString("pairTime", "");
                mProductID = sp.getString("productID", "");
                pe.apply();
            }

            // Demo - doConnectDevice API
            _goFITSdk.doConnectDevice(mMacAddress, mPairingCode, mPairingTime, mProductID, new GoFITSdk.GenericCallback() {
                @Override
                public void onSuccess() {
                    Log.i(_tag, "doConnectDevice() : onSuccess()");
//                    showToast("Connect complete");

                    //demo_connect_status

                    // Demo - isBLEConnect API
                    boolean isConnect = _goFITSdk.isBLEConnect();
                    String summary = isConnect ? "Connected" : "Disconnected";
//                    showToast(summary);

                    //demo_function_connect

                    //showToast("Connected : " + mMacAddress);

                    // Demo - setRemoteCameraHandler API
                    demoSetRemoteCameraHandler();
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    Log.e(_tag, "doConnectDevice() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
//                    showToast("doConnectDevice() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }
            });
        }
        else {
//            showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    public void init() {
        if (_goFITSdk != null) {

            // Demo - doInitialDevice API
            _goFITSdk.doInitialDevice(new GoFITSdk.GenericCallback() {
                @Override
                public void onSuccess() {
                    Log.i(_tag, "doInitialDevice() : onSuccess()");
                    showToast("Initialize OK");
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    Log.e(_tag, "doInitialDevice() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                    showToast("doInitialDevice() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }
            });
        }
        else {
            showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    public void find() {
        if (_goFITSdk != null) {

            // Demo - doFindMyCare API
            _goFITSdk.doFindMyCare(3);
        }
        else {
//            showToast("SDK Instance invalid, needs `SDK init`");
        }
    }


    public  void timeout() {
        if (_goFITSdk != null) {


            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            final SharedPreferences.Editor pe = sp.edit();
            mProductID = sp.getString("productID", "");
            pe.apply();

            if (mProductID.length() > 0 && mProductID != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Set timeout (default is 60s)");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String userInput = input.getText().toString();
                            int timeout = Integer.valueOf(userInput);

                            // Demo - setConnectTimeout API
                            _goFITSdk.setConnectTimeout(mProductID, timeout);

                            //demo_function_set_connect_timeout
                            String summary = String.format("%d s", timeout);
                            showToast(summary);
                        }
                        catch (NumberFormatException e) {
                            showToast("Error Format (not number format)");
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
            else {
                showToast("`New Pairing` first!");
            }
        }
        else {
            showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    public void battery() {
        if (_goFITSdk != null) {
            Log.i(_tag, "demo_battery");

            // Demo - getDeviceBatteryValue API
            _goFITSdk.getDeviceBatteryValue(new GoFITSdk.GetDeviceInfoCallback() {
                @Override
                public void onSuccess(String info) {
                    Log.i(_tag, "getDeviceBatteryValue() : onSuccess() : info = " + info  );
//                    showToast("Get Battery OK");

                    //demo_battery
                    showToast(info +"%");
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    Log.e(_tag, "getDeviceBatteryValue() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
//                    showToast("getDeviceBatteryValue() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }
            });
        }
        else {
//            showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    public void disconnect() {
        if (Connect._goFITSdk != null) {
//            Log.i(Status._tag, "demo_function_disconnect");
//            showToast("Device Disconnect");

            // Demo - doDisconnectDevice API
            Connect._goFITSdk.doDisconnectDevice();


            // Demo - isBLEConnect API
            boolean isConnect = Connect._goFITSdk.isBLEConnect();
            String summary = isConnect ?"Connected" : "Disconnected";


        }
        else {
            //showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    private Handler mConfirmPairingCodeHandler = new Handler();
    private final Runnable mConfirmPairingCodeRunnable = new Runnable() {
        public void run() {
            mConfirmPairingCodeHandler.removeCallbacks(mConfirmPairingCodeRunnable);

            // Demo - confirmPairingCode API
            if (_goFITSdk != null) {
                mProductID = mSelectDevice.getProductID();
                _goFITSdk.doConfirmPairingCode(mPairingCode, mPairingTime, mProductID, new GoFITSdk.GenericCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i(_tag, "doConfirmPairingCode() : onSuccess() : Pairing Complete!");
                        showToast("配對完成!"+mPairingCode);

                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Connect.this);
                        SharedPreferences.Editor pe = sp.edit();
                        pe.putString("productID", mProductID);
                        pe.commit();


                        String summary = "Confirm Paring Code : " + mPairingCode + "(" + mPairingTime + ")";
//                        showToast(summary);

                        //demo_connect_status

                        // Demo - isBLEConnect API
                        boolean isConnect = _goFITSdk.isBLEConnect();
                        summary = isConnect ? "連接成功!" : "未連接成功!!";
                        //showToast(summary);
                        if(isConnect) {
                            next3.setEnabled(true);
                            next3.getBackground().setColorFilter(null);
                            startActivity(new Intent(Connect.this, MainActivity2.class));
                        }
                        else{
                            next3.setEnabled(false);
                        }


                        // Demo - setRemoteCameraHandler API
                        demoSetRemoteCameraHandler();
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        Log.e(_tag, "doConfirmPairingCode() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
//                        showToast("doConfirmPairingCode() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                    }
                });
            }
            else {
//                showToast("SDK Instance invalid, needs `SDK init`");
            }
        }
    };

    void demoSetRemoteCameraHandler() {
        _goFITSdk.setRemoteCameraHandler(new AppContract.RemoteCameraHandler() {
            @Override
            public void triggerCamera() {

                Log.e("[RemoteCamera]", "Trigger Remote Camera!");

                if (CameraClass.cameraGetCurrent() != null) {
                    CameraClass.cameraTakePicture(mCameraShutterCallback, CameraClass.mCameraPictureCallback);
                } else {
                    startActivity(new Intent(getApplicationContext(), RemoteCamera.class));
                }

            }
        });
    }

    public static void sync(String s) {
        subject = s;
        if (_goFITSdk != null) {


            // Demo - doSyncFitnessData API
            _goFITSdk.doSyncFitnessData(new GoFITSdk.SyncCallback() {
                @Override
                public void onCompletion() {
                    Log.i(_tag, "doSyncFitnessData() : onCompletion()");
                    //showToast("Sync complete!\nDetail fitness data show in `Logcat`");
                }

                @Override
                public void onProgress(String message, int progress) {
                    Log.i(_tag, "doSyncFitnessData() : onProgress() : message = " + message + ", progress = " + progress);

                    String summary = String.format("%d", progress);
                    //showToast(summary);
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    Log.e(_tag, "doSyncFitnessData() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                    //showToast("doSyncFitnessData() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }

                @Override
                public void onGetFitnessData(ArrayList<TableStepRecord> stepRecords, ArrayList<TableSleepRecord> sleepRecords, ArrayList<TablePulseRecord> hrRecords, ArrayList<TableSpO2Record> spo2Records) {

                    StringEntity entity ;

                    try {
                        for (TablePulseRecord hr : hrRecords) {

                            Log.i(_tag, "doSyncFitnessData() : onGetFitnessData() : hr = " + hr.toJSONString());
                            JSONObject j = new JSONObject();
                            j.put("name", PersonFragment.editText2.getText().toString());
                            j.put("subject", subject);
                            j.put("serial number", PersonFragment.editText.getText().toString());
                            j.put("pulse",hr.getPulse());
                            j.put("time",hr.getTimestamp());
                            j.put("timeForCompare",hr.getTimestampForCompare());
                            entity = new StringEntity(j.toString(), HTTP.UTF_8);
                            ElasticRestClient.post(null,"hh3/_doc", entity, "application/json",new JsonHttpResponseHandler() );
                        }

                    } catch (JSONException e) {
                    }

                    for (TableSpO2Record spo2 : spo2Records) {
                        Log.i(_tag, "doSyncFitnessData() : onGetFitnessData() : spo2 = " + spo2.toJSONString());
                    }
                }
            });
        }
        else {
            //showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    public static void clear() {
        if (_goFITSdk != null) {

            // Demo - doClearDeviceData API
            _goFITSdk.doClearDeviceData(new GoFITSdk.GenericCallback() {
                @Override
                public void onSuccess() {
                    Log.i(_tag, "doClearDeviceData() : onSuccess()");
                    //showToast("Clear Data OK");
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    Log.e(_tag, "doClearDeviceData() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                    //showToast("doClearDeviceData() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                }
            });
        }
        else {
            //showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    @SuppressWarnings("deprecation")
    private Camera.ShutterCallback mCameraShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            AudioManager mgr = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND, 100);

            Vibrator myVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
            myVibrator.vibrate(300);
        }
    };

    public void setting() {
        if (_goFITSdk != null) {

            if (mCareSettings == null) {
                mCareSettings = _goFITSdk.getNewCareSettings();
            }
            //displaySettingMainMenu();
            demoSettingHRTimingMeasure();
        }
        else {
            //showToast("SDK Instance invalid, needs `SDK init`");
        }
    }

    void displaySettingMainMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Setting Option");
        builder.setMessage("1 : 步數目標\n" +
                "2 : 設定單位\n" +
                "3 : 設定時間顯示\n" +
                "4 : 抬手點亮\n" +
                "5 : 久坐提醒\n" +
                "6 : 藍芽斷開連結通知\n" +
                "7 : 配戴的手腕\n" +
                "8 : 設定鬧鐘\n" +
                "9 : 心率偵測\n" +
                "10 : 設定語言\n" +
                "11 : 勿擾模式 \n" +
                "12 : 螢幕鎖 \n" +
                "13 : 心率警示\n" +
                "\n\n999 : Restore default setting");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                String instructions = "";
                switch (userInput) {
                    case "1" :
                        instructions = "e.g : 8000";
                        displaySettingDetail(instructions, SettingItem.STEP_TARGET);
                        break;
                    case "2" :
                        instructions = "格式 : [\"imperial(英制)\" / \"metric(公制)\"]\ne.g : imperial";
                        displaySettingDetail(instructions, SettingItem.UNIT);
                        break;
                    case "3" :
                        instructions = "格式 : [\"12\" / \"24\"]\ne.g : 12";
                        displaySettingDetail(instructions, SettingItem.TIME_FORMAT);
                        break;
                    case "4" :
                        instructions = "格式 : [0:off / 1:on]\ne.g : 1";
                        displaySettingDetail(instructions, SettingItem.AUTO_SHOW_SCREEN);
                        break;
                    case "5" :
                        instructions = "格式 : [on/off], [repeatDays(0~127 bit operator)], [HH:mm(startTime)], [HH:mm(endTime)], [IntervalMin]\ne.g : on,127,09:30,18:30,15";
                        displaySettingDetail(instructions, SettingItem.SIT_REMINDER);
                        break;
                    case "6" :
                        instructions = "格式 : [0:off / 1:on]\ne.g : 1";
                        displaySettingDetail(instructions, SettingItem.BLE_DISCONNECT_NOTIFICATION);
                        break;
                    case "7" :
                        instructions = "格式 : [\"left\" / \"right\"]\ne.g : left";
                        displaySettingDetail(instructions, SettingItem.HANDEDNESS);
                        break;
                    case "8" :
                        instructions = "格式 : [on/off], [index(0~29)], [repeatDays(0~127 bit operator)], [HH:mm], [category]\ne.g : on,2,0,07:30,0";
                        displaySettingDetail(instructions, SettingItem.ALARM_CLOCK);
                        break;
                    case "9" :
                        instructions = "格式 : [on/off], [HH:mm(startTime)], [HH:mm(endTime)], [IntervalMin]\ne.g : on,00:00,23:59,15";
                        displaySettingDetail(instructions, SettingItem.HR_TIMING_MEASURE);
                        break;
                    case "10" :
                        instructions = "格式 : [0:TW / 1:CN / 2:EN / 3:JP]\ne.g : 2";
                        displaySettingDetail(instructions, SettingItem.LANGUAGE);
                        break;
                    case "11" :
                        instructions = "格式 : [0:off / 1:on]\ne.g : 1";
                        displaySettingDetail(instructions, SettingItem.DND);
                        break;
                    case "12" :
                        instructions = "格式 : [0:off / 1:on]\ne.g : 1";
                        displaySettingDetail(instructions, SettingItem.SCREEN_LOCK);
                        break;
                    case "13" :
                        instructions = "格式 : [on/off], [max warning], [min warning]\ne.g : on,170,50 (max value must larger than min value)";
                        displaySettingDetail(instructions, SettingItem.HR_WARNING);
                        break;
                    case "999" :
                        mCareSettings = _goFITSdk.getDefaultCareSettings();
                        demoSetSettingToDevice();
                        break;
                    default :
                        showToast("Invalid input");
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    SetCareSetting demoGenSettingObject() {
        // Demo - new Setting object
        SetCareSetting mCareSettings = _goFITSdk.getDefaultCareSettings();

        if (mCareSettings != null) {
            // Demo - generate system unit setting
            String systemUnit = mCareSettings.getSystemUnit();
            systemUnit = "imperial";
            mCareSettings.setSystemUnit(systemUnit);

            // Demo - generate time format setting
            String timeFormat = mCareSettings.getTimeFormat();
            timeFormat = "12";
            mCareSettings.setTimeFormat(timeFormat);

            // Demo - generate disconnect alert setting
            SetCareSetting.Switch disconnectAlert = mCareSettings.getEnableDisconnectAlert();
            disconnectAlert = SetCareSetting.Switch.True;
            mCareSettings.setEnableDisconnectAlert(disconnectAlert);
        }

        return mCareSettings;
    }

    void displaySettingDetail(String instructions, final SettingItem setting) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage(instructions);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                switch (setting) {
                    case STEP_TARGET:
                        demoSettingStepTarget(userInput);
                        break;
                    case UNIT:
                        demoSettingUnit(userInput);
                        break;
                    case TIME_FORMAT:
                        demoSettingTimeFormat(userInput);
                        break;
                    case AUTO_SHOW_SCREEN:
                        demoSettingAutoShowScreen(userInput);
                        break;
                    case SIT_REMINDER:
                        demoSettingSitReminder(userInput);
                        break;
                    case BLE_DISCONNECT_NOTIFICATION:
                        demoSettingBLEDisconnectNotification(userInput);
                        break;
                    case HANDEDNESS:
                        demoSettingHandedness(userInput);
                        break;
                    case ALARM_CLOCK:
                        demoSettingAlarmClock(userInput);
                        break;
                    case HR_TIMING_MEASURE:
                        //demoSettingHRTimingMeasure(userInput);
                        break;
                    case LANGUAGE:
                        demoSettingLanguage(userInput);
                        break;
                    case DND:
                        demoSettingDND(userInput);
                        break;
                    case SCREEN_LOCK:
                        demoSettingScreenLock(userInput);
                        break;
                    case HR_WARNING:
                        demoSettingHRWarning(userInput);
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    void demoSettingStepTarget(String userInput) {
        try {
            // Demo - step target setting
            int target = Integer.valueOf(userInput);
            mCareSettings.setStepGoal(target);
            demoSetSettingToDevice();
        }
        catch (NumberFormatException e) {
            showToast("Error Format (not number format)");
        }
    }

    void demoSettingUnit(String userInput) {
        if (userInput.equals("imperial") || userInput.equals("metric")) {
            // Demo - system unit setting
            String systemUnit = userInput;
            mCareSettings.setSystemUnit(systemUnit);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid string : must be `imperial` or `metric`)");
        }
    }

    void demoSettingTimeFormat(String userInput) {
        if (userInput.equals("12") || userInput.equals("24")) {
            // Demo - time format setting
            String timeFormat = userInput;
            mCareSettings.setTimeFormat(timeFormat);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid string : must be `12` or `24`)");
        }
    }

    void demoSettingAutoShowScreen(String userInput) {
        if (userInput.equals("0") || userInput.equals("1")) {
            // Demo - auto show screen setting
            SetCareSetting.Switch setting = SetCareSetting.Switch.None;
            if (userInput.equals("1")) {
                setting = SetCareSetting.Switch.True;
            }
            else {
                setting = SetCareSetting.Switch.False;
            }
            mCareSettings.setEnableAutoLightUp(setting);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid input : must be `0` or `1`)");
        }
    }

    void demoSettingSitReminder(String userInput) {
        String[] separated = userInput.split(",");
        // Demo - sit reminder setting
        CareIdleAlert setting = mCareSettings.getDefaultIdleAlert();
        if (separated.length == 5) {
            if (separated[0].equals("on") || separated[0].equals("off")) {
                boolean enable = separated[0].equals("on") ? true : false;
                setting.setEnableIdleAlert(enable);
            }
            else {
                showToast("Error Format (invalid input : must be `on` or `off`)");
                return;
            }

            try {
                int repeatDays = Integer.valueOf(separated[1]);
                if (repeatDays >= 0 && repeatDays <= 127) {
                    byte[] bytesRepeatDay = convertRepeatDay(repeatDays);
                    setting.setRepeatDays(bytesRepeatDay);
                }
                else {
                    showToast("Error Format (invalid range : 0~127)");
                    return;
                }

            }
            catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            int startMin = convertHHmmToMin(separated[2]);
            if (startMin >= 0 && startMin <= 1439) {
                setting.setStartMin((short) startMin);
            }
            else {
                showToast("Error Format (invalid time format)");
            }

            int endMin = convertHHmmToMin(separated[3]);
            if (endMin >= 0 && endMin <= 1439) {
                setting.setEndMin((short) endMin);
            }
            else {
                showToast("Error Format (invalid time format)");
            }

            try {
                int intervalMin = Integer.valueOf(separated[4]);
                setting.setInterval((short)intervalMin);
            }
            catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            mCareSettings.setIdleAlert(setting);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid parameter counts)");
            return;
        }
    }

    void demoSettingBLEDisconnectNotification(String userInput) {
        if (userInput.equals("0") || userInput.equals("1")) {
            // Demo - BLE disconnect alert setting
            SetCareSetting.Switch setting = SetCareSetting.Switch.None;
            if (userInput.equals("1")) {
                setting = SetCareSetting.Switch.True;
            }
            else {
                setting = SetCareSetting.Switch.False;
            }
            mCareSettings.setEnableDisconnectAlert(setting);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid input : must be `0` or `1`)");
        }
    }

    void demoSettingHandedness(String userInput) {
        if (userInput.equals("left") || userInput.equals("right")) {
            // Demo - handedness setting
            mCareSettings.setHandedness(userInput);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid input : must be `left` or `right`)");
        }
    }

    void demoSettingAlarmClock(String userInput) {
        String[] separated = userInput.split(",");
        if (separated.length == 5) {
            // Demo - alarm clock setting
            CareAlarm careAlarms = mCareSettings.getAlarms();
            if (careAlarms == null) {
                careAlarms = mCareSettings.getDefaultAlarms();
            }
            ArrayList<CareAlarm.Alarm> alarms = careAlarms.getAlarms();
            CareAlarm.Alarm setting = null;
            int index = 0;
            try {
                index = Integer.valueOf(separated[1]).intValue();
                if (index >= 0 && index <= 29) {
                    setting = alarms.get(index);
                } else {
                    showToast("Error Format [index] (invalid range : 0~29)");
                    return;
                }
            } catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            if (separated[0].equals("on") || separated[0].equals("off")) {
                boolean enable = separated[0].equals("on") ? true : false;
                setting.setEnableAlarm(enable);
                setting.setIsActive(enable);
            } else {
                showToast("Error Format (invalid input : must be `on` or `off`)");
                return;
            }

            try {
                int repeatDays = Integer.valueOf(separated[2]).intValue();
                if (repeatDays >= 0 && repeatDays <= 127) {
                    byte[] bytesRepeatDay = convertRepeatDay(repeatDays);
                    setting.setRepeatDays(bytesRepeatDay);
                } else {
                    showToast("Error Format [repeatDays] (invalid range : 0~127)");
                    return;
                }

            } catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            int reminderTime = convertHHmmToMin(separated[3]) * 60;  // input must be `seconds`
            if (reminderTime >= 0 && reminderTime <= 86399) {
                setting.setReminderTime(reminderTime);
            } else {
                showToast("Error Format [reminderTime] (invalid time format)");
                return;
            }

            try {
                int category = Integer.valueOf(separated[4]).intValue();
                if (category >= 0 && category <= 7) {
                    setting.setCategory((short) category);
                } else {
                    showToast("Error Format [category] (invalid range : 0~7)");
                    return;
                }
            } catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            alarms.set(index, setting);
            careAlarms.setAlarms(alarms);
            mCareSettings.setAlarms(careAlarms);
            demoSetSettingToDevice();
        } else {
            showToast("Error Format (invalid parameter counts)");
        }
    }

    void demoSettingHRTimingMeasure() {
//        String[] separated = userInput.split(",");//on,00:00,23:59,1
//        if (separated.length == 4) {
//            // Demo - HR timing measure setting
        CareMeasureHR careMeasureHR = mCareSettings.getDefaultMeasureHR();
//            careMeasureHR.setRepeatDays(convertRepeatDay(127));
//            if (separated[0].equals("on") || separated[0].equals("off")) {
//                boolean enable = separated[0].equals("on") ? true : false;
//                careMeasureHR.setEnableMeasureHR(enable);
//            }
//            else {
//                showToast("Error Format (invalid input : must be `on` or `off`)");
//                return;
//            }
//
//            int startMin = convertHHmmToMin(separated[1]);
//            if (startMin >= 0 && startMin <= 1439) {
//                careMeasureHR.setStartMin((short) startMin);
//            }
//            else {
//                showToast("Error Format (invalid time format)");
//            }
//
//            int endMin = convertHHmmToMin(separated[2]);
//            if (endMin >= 0 && endMin <= 1439) {
//                careMeasureHR.setEndMin((short) endMin);
//            }
//            else {
//                showToast("Error Format (invalid time format)");
//            }

        try {
//                int intervalMin = Integer.valueOf(01);
            careMeasureHR.setInterval((short)1);
        }
        catch (NumberFormatException e) {
            //showToast("Error Format (not number format)");
            return;
        }

        mCareSettings.setMeasureHR(careMeasureHR);
        demoSetSettingToDevice();
    }
//        else {
//            showToast("Error Format (invalid parameter counts)");
//        }
//    }



    void demoSettingLanguage(String userInput) {
        // Demo - language setting
        AppContract.SystemLanguage language;
        if (userInput.equals("0")) {
            language = AppContract.SystemLanguage.zhrTW;
            mCareSettings.setSystemLanguage(language);
            demoSetSettingToDevice();
        }
        else if (userInput.equals("1")) {
            language = AppContract.SystemLanguage.zhrCN;
            mCareSettings.setSystemLanguage(language);
            demoSetSettingToDevice();
        }
        else if (userInput.equals("2")) {
            language = AppContract.SystemLanguage.en;
            mCareSettings.setSystemLanguage(language);
            demoSetSettingToDevice();
        }
        else if (userInput.equals("3")) {
            language = AppContract.SystemLanguage.ja;
            mCareSettings.setSystemLanguage(language);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid input)");
        }
    }

    void demoSettingDND(String userInput) {
        if (userInput.equals("0") || userInput.equals("1")) {
            // Demo - DND setting
            CareDoNotDisturb setting = mCareSettings.getDefaultDoNotDisturb();
            boolean enable = userInput.equals("1") ? true : false;
            setting.setEnableDoNotDisturb(enable);
            setting.setRepeatDays(convertRepeatDay(127));
            setting.setStartMin((short)convertHHmmToMin("22:00"));
            setting.setEndMin((short)convertHHmmToMin("07:30"));
            mCareSettings.setDoNotDisturb(setting);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid input : must be `0` or `1`)");
        }
    }

    void demoSettingScreenLock(String userInput) {
        if (userInput.equals("0") || userInput.equals("1")) {
            // Demo - screen lock setting
            SetCareSetting.Switch setting = SetCareSetting.Switch.None;
            if (userInput.equals("1")) {
                setting = SetCareSetting.Switch.True;
            }
            else {
                setting = SetCareSetting.Switch.False;
            }
            mCareSettings.setEnableHorizontalUnlock(setting);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid input : must be `0` or `1`)");
        }
    }

    void demoSettingHRWarning(String userInput) {
        String[] separated = userInput.split(",");
        if (separated.length == 3) {
            // Demo - HR warning setting
            CareHRWarning careHRWarning = mCareSettings.getDefaultHRWarning();
            if (separated[0].equals("on") || separated[0].equals("off")) {
                boolean enable = separated[0].equals("on") ? true : false;
                careHRWarning.setEnableHRWarning(enable);
            }
            else {
                showToast("Error Format (invalid input : must be `on` or `off`)");
                return;
            }

            try {
                int maxWarning = Integer.valueOf(separated[1]).intValue();
                careHRWarning.setMaxHRLimit(maxWarning);
            }
            catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            try {
                int minWarning = Integer.valueOf(separated[2]).intValue();
                careHRWarning.setMinHRLimit(minWarning);
            }
            catch (NumberFormatException e) {
                showToast("Error Format (not number format)");
                return;
            }

            mCareSettings.setHRWarning(careHRWarning);
            demoSetSettingToDevice();
        }
        else {
            showToast("Error Format (invalid parameter counts)");
        }
    }

    byte[] convertRepeatDay(int days) {
        byte[] repeatDays = {0, 0, 0, 0, 0, 0, 0};
        try {
            repeatDays[0] = (byte) (((days & 0x01) == 1) ? 1 : 0);
            repeatDays[1] = (byte) ((((days >> 1) & 0x01) == 1) ? 1 : 0);
            repeatDays[2] = (byte) ((((days >> 2) & 0x01) == 1) ? 1 : 0);
            repeatDays[3] = (byte) ((((days >> 3) & 0x01) == 1) ? 1 : 0);
            repeatDays[4] = (byte) ((((days >> 4) & 0x01) == 1) ? 1 : 0);
            repeatDays[5] = (byte) ((((days >> 5) & 0x01) == 1) ? 1 : 0);
            repeatDays[6] = (byte) ((((days >> 6) & 0x01) == 1) ? 1 : 0);
        } catch (Exception e) {
            for (int i = 0; i < repeatDays.length; i++) {
                repeatDays[i] = 0;
            }
        }

        return repeatDays;
    }

    int convertHHmmToMin(String HHmm) {
        try {
            String[] timestamp = HHmm.split(":");
            int hour = Integer.parseInt(timestamp[0]);
            int minute = Integer.parseInt(timestamp[1]);
            return (hour * 60 + minute);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    void demoSetSettingToDevice() {
        // Demo - doSetSetting API
        _goFITSdk.doSetSettings(mCareSettings, new GoFITSdk.SettingsCallback() {
            @Override
            public void onCompletion() {
                Log.i(_tag, "doSetSettings() : onCompletion()");
                //showToast("Setting OK");
                //demo_function_setting

                String summary = "Setting OK";
                //showToast(summary);
            }

            @Override
            public void onProgress(String message) {
                Log.i(_tag, "doSetSettings() : onProgress() : message = " + message);
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                Log.e(_tag, "doSetSettings() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
                //showToast("doSetSettings() : onFailure() : errorCode = " + errorCode + ", " + "errorMsg = " + errorMsg);
            }
        });

    }
}