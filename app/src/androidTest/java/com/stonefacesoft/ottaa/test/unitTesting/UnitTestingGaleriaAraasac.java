package com.stonefacesoft.ottaa.test.unitTesting;

import android.view.KeyEvent;
import android.view.View;
import android.widget.SearchView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.stonefacesoft.ottaa.GaleriaArasaac;
import com.stonefacesoft.ottaa.R;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;


/**
 * @author Gonzalo Juarez
 * @version 1.0
 * <h1> Description </h1>
 * <p> this class was created in order to available the functions about the galeriaAraasac with a searchView</p>
 * */
@RunWith(AndroidJUnit4.class)
public class UnitTestingGaleriaAraasac extends TestCase {
    @Rule
    public ActivityTestRule<GaleriaArasaac> activityTestRule=new ActivityTestRule<>(GaleriaArasaac.class);

    @Test
    public void UnitTestingGaleriaAraasac(){
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text))
                .perform(typeText("car"));
        onView(withId(R.id.action_search)).perform(pressKey(KeyEvent.KEYCODE_SEARCH));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //      onView(withId(R.id.action_search)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

    }

    public static ViewAction typeSearchViewText(final String text){
        return new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SearchView)view).setVisibility(View.VISIBLE);
                ((SearchView) view).setQuery(text,false);

            }

        };
    }

    @Override
    protected TestResult createResult() {
        return super.createResult();
    }

    @Override
    public TestResult run() {
        return super.run();
    }
}
