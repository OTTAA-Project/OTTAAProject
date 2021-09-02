package com.stonefacesoft.ottaa;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.Manifest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class loginActivityTesting {


    @Rule
    public ActivityScenarioRule<LoginActivity2> mActivityTestRule = new ActivityScenarioRule<>(LoginActivity2.class);


    @Rule GrantPermissionRule networState = GrantPermissionRule.grant(Manifest.permission.ACCESS_NETWORK_STATE);
    @Rule GrantPermissionRule coarseLocation = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);
    @Rule GrantPermissionRule fineLocation = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule GrantPermissionRule vibrate = GrantPermissionRule.grant(Manifest.permission.VIBRATE);
    @Rule GrantPermissionRule writeExternalStorage = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule GrantPermissionRule readExternalStorage = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
    @Rule GrantPermissionRule readCalendar = GrantPermissionRule.grant(Manifest.permission.READ_CALENDAR);
    @Rule GrantPermissionRule writeCalendar = GrantPermissionRule.grant(Manifest.permission.WRITE_CALENDAR);
    @Rule GrantPermissionRule getAccounts = GrantPermissionRule.grant(Manifest.permission.GET_ACCOUNTS);
    @Rule GrantPermissionRule readContacts = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS);
    @Rule
    public GrantPermissionRule permissionInternet = GrantPermissionRule.grant(Manifest.permission.INTERNET);
    @Rule
    public GrantPermissionRule wakeLockPermission = GrantPermissionRule.grant(Manifest.permission.WAKE_LOCK);


    @Test
    public void loginActivityTest(){
        ViewInteraction fv = onView(
                allOf(withText("Acceder con Google"),
                        childAtPosition(
                                allOf(withId(R.id.googleSignInButton),
                                        childAtPosition(
                                                withId(R.id.singInContainer),
                                                0)),
                                0),
                        isDisplayed()));
        fv.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }


}
