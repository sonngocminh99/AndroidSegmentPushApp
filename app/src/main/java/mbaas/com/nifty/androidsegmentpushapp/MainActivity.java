package mbaas.com.nifty.androidsegmentpushapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBInstallation;
import com.nifty.cloud.mb.core.NCMBQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    TextView _objectId;
    TextView _appversion;
    Spinner _channels;
    TextView _devicetoken;
    TextView _sdkversion;
    TextView _timezone;
    TextView _createdate;
    TextView _updatedate;
    EditText _txtPrefectures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //**************** APIキーの設定とSDKの初期化 **********************
        NCMB.initialize(this, "YOUR_APPLICATION_KEY", "YOUR_CLIENT_KEY");

        final NCMBInstallation installation = NCMBInstallation.getCurrentInstallation();

        //GCMからRegistrationIdを取得しinstallationに設定する
        installation.getRegistrationIdInBackground("ANDROID_SENDER_ID", new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e == null) {
                    installation.saveInBackground(new DoneCallback() {
                        @Override
                        public void done(NCMBException e1) {
                            if (e1 == null) {

                                //表示する端末情報のデータを反映
                                _objectId = (TextView) findViewById(R.id.txtObject);
                                _appversion = (TextView) findViewById(R.id.txtAppversion);
                                _channels = (Spinner)findViewById(R.id.spinChannel);
                                _devicetoken = (TextView) findViewById(R.id.txtDevicetoken);
                                _sdkversion = (TextView) findViewById(R.id.txtSdkversion);
                                _timezone = (TextView) findViewById(R.id.txtTimezone);
                                _createdate = (TextView) findViewById(R.id.txtCreatedate);
                                _updatedate = (TextView) findViewById(R.id.txtUpdatedate);
                                _txtPrefectures = (EditText) findViewById(R.id.txtPrefecture);

                                //表示する端末情報を指定
                                _objectId.setText(installation.getObjectId());
                                _appversion.setText(installation.getAppVersion());
                                try {
                                    if (installation.getChannels() != null) {
                                        String selectChannel = installation.getChannels().get(0).toString();
                                        String[] channelArray = new String[] {"A", "B", "C", "D"};
                                        Integer selectId = Arrays.asList(channelArray).indexOf(selectChannel);
                                        _channels.setSelection(selectId);
                                    }
                                } catch (JSONException e2) {
                                    e2.printStackTrace();
                                }
                                _devicetoken.setText(installation.getDeviceToken());
                                _sdkversion.setText(installation.getSDKVersion());
                                _timezone.setText(installation.getTimeZone());
                                _createdate.setText(installation.getCreateDate().toString());
                                _updatedate.setText(installation.getUpdateDate().toString());
                                if (installation.getString("Prefectures") != null) {
                                    _txtPrefectures.setText(installation.getString("Prefectures"));
                                }

                            } else if (NCMBException.DUPLICATE_VALUE.equals(e1.getCode())) {
                                //保存失敗 : registrationID重複の場合に実行
                                updateInstallation(installation);
                            } else {
                                //保存失敗 : その他エラーが発生
                                Toast.makeText(MainActivity.this, "端末情報を登録失敗しました。" + e1.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    //ID取得失敗
                    Toast.makeText(MainActivity.this, "端末のデバイストークンを取得失敗しました。", Toast.LENGTH_LONG).show();
                }
            }
        });

        setContentView(R.layout.activity_main);

        Button _btnSave = (Button) findViewById(R.id.btnSave);
        _btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _channels = (Spinner)findViewById(R.id.spinChannel);
                _txtPrefectures = (EditText) findViewById(R.id.txtPrefecture);
                String prefectures = _txtPrefectures.getText().toString();
                String item = (String) _channels.getSelectedItem();
                JSONArray tmpArray = new JSONArray();
                tmpArray.put(item);
                installation.setChannels(tmpArray);
                installation.put("Prefectures", prefectures);
                installation.saveInBackground(new DoneCallback() {
                    @Override
                    public void done(NCMBException e) {
                        if (e != null) {
                            //保存失敗
                            Toast.makeText(MainActivity.this, "端末情報を保存失敗しました。" + e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            //保存成功
                            Toast.makeText(MainActivity.this, "端末情報を保存成功しました。", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


    public static void updateInstallation(final NCMBInstallation installation) {
        //installationクラスを検索するクエリの作成
        NCMBQuery<NCMBInstallation> query = NCMBInstallation.getQuery();
        //同じRegistration IDをdeviceTokenフィールドに持つ端末情報を検索する
        query.whereEqualTo("deviceToken", installation.getDeviceToken());
        //データストアの検索を実行
        query.findInBackground(new FindCallback<NCMBInstallation>() {
            @Override
            public void done(List<NCMBInstallation> results, NCMBException e) {
                //検索された端末情報のobjectIdを設定
                installation.setObjectId(results.get(0).getObjectId());
                //端末情報を更新する
                installation.saveInBackground();
            }
        });
    }

}
