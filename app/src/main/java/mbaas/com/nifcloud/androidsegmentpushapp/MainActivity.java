package mbaas.com.nifcloud.androidsegmentpushapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nifcloud.mbaas.core.DoneCallback;
import com.nifcloud.mbaas.core.FindCallback;
import com.nifcloud.mbaas.core.NCMB;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBInstallation;
import com.nifcloud.mbaas.core.NCMBQuery;
import com.nifcloud.mbaas.core.TokenCallback;

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
    String deviceTokenSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //**************** APIキーの設定とSDKの初期化 **********************
        NCMB.initialize(this.getApplicationContext(), "YOUR_APPLICATION_KEY", "YOUR_CLIENT_KEY");

        final NCMBInstallation installation = NCMBInstallation.getCurrentInstallation();

        setContentView(R.layout.activity_main);

        //表示する端末情報のデータを反映
        _objectId = (TextView) findViewById(R.id.txtObject);
        _appversion = (TextView) findViewById(R.id.txtAppversion);
        _channels = (Spinner) findViewById(R.id.spinChannel);
        _devicetoken = (TextView) findViewById(R.id.txtDevicetoken);
        _sdkversion = (TextView) findViewById(R.id.txtSdkversion);
        _timezone = (TextView) findViewById(R.id.txtTimezone);
        _createdate = (TextView) findViewById(R.id.txtCreatedate);
        _updatedate = (TextView) findViewById(R.id.txtUpdatedate);
        _txtPrefectures = (EditText) findViewById(R.id.txtPrefecture);

        installation.getDeviceTokenInBackground(new TokenCallback() {
            @Override
            public void done(String s, NCMBException e) {
                if (e == null) {

                    NCMBQuery<NCMBInstallation> query = NCMBInstallation.getQuery();
                    deviceTokenSearch = s;
                    query.whereEqualTo("deviceToken", deviceTokenSearch);
                    query.findInBackground(new FindCallback<NCMBInstallation>() {
                        @Override
                        public void done(List<NCMBInstallation> list, NCMBException e) {
                            NCMBInstallation ncmbInstallation = list.get(0);
                            //表示する端末情報を指定
                            _objectId.setText(ncmbInstallation.getObjectId());
                            _appversion.setText(ncmbInstallation.getAppVersion());
                            try {
                                if (ncmbInstallation.getChannels() != null) {
                                    String selectChannel = ncmbInstallation.getChannels().get(0).toString();
                                    String[] channelArray = new String[]{"A", "B", "C", "D"};
                                    Integer selectId = Arrays.asList(channelArray).indexOf(selectChannel);
                                    _channels.setSelection(selectId);
                                }
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                            _devicetoken.setText(deviceTokenSearch);
                            _sdkversion.setText(ncmbInstallation.getSDKVersion());
                            _timezone.setText(ncmbInstallation.getTimeZone());
                            try {
                                _createdate.setText(ncmbInstallation.getCreateDate().toString());
                                _updatedate.setText(ncmbInstallation.getUpdateDate().toString());
                            } catch (NCMBException e1) {
                                e1.printStackTrace();
                            }
                            if (ncmbInstallation.getString("Prefectures") != null) {
                                _txtPrefectures.setText(ncmbInstallation.getString("Prefectures"));
                            }
                        }
                    });

                }
            }
        });


        Button _btnSave = (Button) findViewById(R.id.btnSave);
        _btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _channels = (Spinner) findViewById(R.id.spinChannel);
                _txtPrefectures = (EditText) findViewById(R.id.txtPrefecture);
                String prefectures = _txtPrefectures.getText().toString();
                String item = (String) _channels.getSelectedItem();
                JSONArray tmpArray = new JSONArray();
                tmpArray.put(item);
                installation.setChannels(tmpArray);
                try {
                    installation.put("Prefectures", prefectures);
                } catch (NCMBException e) {
                    e.printStackTrace();
                }
                installation.saveInBackground(new DoneCallback() {
                    @Override
                    public void done(NCMBException e) {
                        if (e != null) {
                            //保存失敗
                            Toast.makeText(MainActivity.this, "端末情報の保存に失敗しました。" + e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            //保存成功
                            Toast.makeText(MainActivity.this, "端末情報の保存に成功しました。", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


}
