package mbaas.com.nifcloud.androidsegmentpushapp;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExecuteUITest {

    ViewInteraction _objectId;
    ViewInteraction _appversion;
    ViewInteraction _channels;
    ViewInteraction _devicetoken;
    ViewInteraction _sdkversion;
    ViewInteraction _timezone;
    ViewInteraction _createdate;
    ViewInteraction _updatedate;
    ViewInteraction _txtPrefectures;
    ViewInteraction btnSave;

    private View decorView;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        _objectId = onView(withId(R.id.txtObject));
        _appversion = onView(withId(R.id.txtAppversion));
        _channels = onView(withId(R.id.spinChannel));
        _devicetoken = onView(withId(R.id.txtDevicetoken));
        _sdkversion = onView(withId(R.id.txtSdkversion));
        _timezone = onView(withId(R.id.txtTimezone));
        _createdate = onView(withId(R.id.txtCreatedate));
        _updatedate = onView(withId(R.id.txtUpdatedate));
        _txtPrefectures = onView(withId(R.id.txtPrefecture));
        btnSave = onView(withId(R.id.btnSave));

        activityRule.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });
    }

    @Test
    public void initialScreen() {
        Espresso.closeSoftKeyboard();
        onView(withText("Current Installation")).perform(scrollTo(), click()).check(matches(isDisplayed()));
        onView(withText("ObjectId")).perform(scrollTo(), click()).check(matches(isDisplayed()));
        _objectId.check(matches(isDisplayed()));
        onView(withText("appVersion")).perform(scrollTo(), click()).check(matches(isDisplayed()));
        _appversion.check(matches(isDisplayed()));
        onView(withText("channels")).perform(scrollTo(), click()).check(matches(isDisplayed()));
        _channels.check(matches(isDisplayed()));
        onView(withText("deviceToken")).perform(scrollTo(), click()).check(matches(isDisplayed()));
        _devicetoken.check(matches(isDisplayed()));
        onView(withText("sdkVersion")).perform(scrollTo(), click()).check(matches(isDisplayed()));
        _sdkversion.check(matches(isDisplayed()));
        onView(withText("timeZone")).perform(scrollTo(), click()).check(matches(isDisplayed()));
        _timezone.check(matches(isDisplayed()));
        onView(withText("createDate")).perform(scrollTo(), click()).check(matches(isDisplayed()));
        _createdate.check(matches(isDisplayed()));
        onView(withText("updateDate")).perform(scrollTo(), click()).check(matches(isDisplayed()));
        _updatedate.check(matches(isDisplayed()));
        onView(withText("Prefectures")).perform(scrollTo(), click()).check(matches(isDisplayed()));
        _txtPrefectures.check(matches(isDisplayed()));
        btnSave.perform(scrollTo()).check(matches(withText("SAVE")));
    }

    @Test
    public void doSave() throws InterruptedException {
        Thread.sleep(2000);
        _txtPrefectures.perform(typeText("Hoge"), (ViewAction) ViewActions.closeSoftKeyboard());
        btnSave.perform(scrollTo(), click());
        onView(withText("端末情報の保存に成功しました。"))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }

}