package com.example.currencyconverter.view;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.example.currencyconverter.MainActivity;
import com.example.currencyconverter.R;
import com.example.currencyconverter.view.ValuteView;
import com.example.currencyconverter.model.CurrencyModel;
import com.example.currencyconverter.presenter.CurrencyPresenter;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by user on 14.05.17.
 */

@RunWith(MockitoJUnitRunner.class)
public class ValuteViewTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Captor
    ArgumentCaptor<List<String>> listCaptor;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    @Mock
    ValuteView mockedCallback;

    @Mock
    CurrencyModel model;

    // test if interface methods onError and onPublishList are called
    @Test
    public void testCallback() {

        Context context = InstrumentationRegistry.getTargetContext();
        CurrencyPresenter pr = new CurrencyPresenter(context);
        pr.setIView(mockedCallback);
        pr.publish();

        verify(mockedCallback).onPublishList(listCaptor.capture());
        assertTrue(listCaptor.getValue().size()>0);

        pr.convert("","","");
        pr.convert("1","","");
        when(model.getCurrencyCodes()).thenReturn(new ArrayList<String>());
        pr.setModel(model);
        pr.publish();
        verify(mockedCallback,times(3)).onError(stringCaptor.capture());

        assertEquals(context.getString(R.string.enter_sum_message), stringCaptor.getAllValues().get(0));
        assertEquals(context.getString(R.string.conversion_error_text), stringCaptor.getAllValues().get(1));
        assertEquals(context.getString(R.string.empty_list_error_text), stringCaptor.getAllValues().get(2));
    }

    // test if interface method onConvert is called
    @Test
    public void testConvert() {

        // waiting for view is being enabled
        onView(ViewMatchers.withId(R.id.et_summa)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Wait for enabled.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                while(!view.isEnabled())
                    uiController.loopMainThreadForAtLeast(1);
            }
        });

        onView(ViewMatchers.withId(R.id.et_summa)).perform(ViewActions.typeText("1"));
        onView(ViewMatchers.withId(R.id.btn_convert)).perform(ViewActions.click());

        final String[] stringHolder = {null};
        onView(ViewMatchers.withId(R.id.tv_result)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(AppCompatTextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a AppCompatTextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                AppCompatTextView tv = (AppCompatTextView) view;
                stringHolder[0] = tv.getText().toString();
            }
        });

        assertEquals("testConvertedValue", stringHolder[0], "1.00");
    }

}