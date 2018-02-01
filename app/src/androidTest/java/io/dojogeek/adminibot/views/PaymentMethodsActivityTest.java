package io.dojogeek.adminibot.views;

import android.support.test.espresso.intent.Intents;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.dojogeek.adminibot.R;
import io.dojogeek.adminibot.dtos.IncomeDto;
import io.dojogeek.adminibot.presenters.PaymentMethodsPresenter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class PaymentMethodsActivityTest {

    @Rule
    public ActivityTestRule<PaymentMethodsActivity> mActivityRule = new ActivityTestRule<>(PaymentMethodsActivity.class);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private PaymentMethodsPresenter presenter;

    @Before
    public void setUp() {
        PaymentMethodsActivity paymentMethodsActivity = mActivityRule.getActivity();
        paymentMethodsActivity.mPresenter = presenter;
    }

    @Test
    public void testIncomeConcept_isAccepted() {
        onView(withId(R.id.edit_concept_of_income)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_concept_of_income)).perform(typeText("This a test concept"));
        onView(withText(R.string.msg_accept)).perform(click());
        onView(withText("This a test concept")).check(matches(isDisplayed()));
    }

    @Test
    public void testIncomeConcept_errorIfAcceptWithEmptyConcept() {
        onView(withText(R.string.msg_accept)).perform(click());
        onView(withText(R.string.error_concept_of_income)).check(matches(isDisplayed()));
    }

    @Test
    public void testIncomeConcept_cancelFlow() {
        onView(withText(R.string.msg_cancel)).perform(click());

        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void testIncomeConcept_isNotCancelable() {
        onView(withId(R.id.edit_concept_of_income)).perform(pressBack()).check(matches(isDisplayed()));
    }

    @Test
    public void testCash_isAccepted() {
        fillIncomeConcept();

        onView(withText(R.string.msg_cash)).perform(click());
        onView(withId(R.id.edit_cash_amount)).perform(typeText("17500"));
        onView(withText(R.string.msg_accept)).perform(click());

        onView(withId(R.id.text_total_amount)).check(matches(isDisplayed())).check(matches(withText("$17500.00")));
    }

    @Test
    public void testCash_errorIfAcceptWithEmptyAmount() {
        fillIncomeConcept();

        onView(withText(R.string.msg_cash)).perform(click());
        onView(withText(R.string.msg_accept)).perform(closeSoftKeyboard()).perform(click());
        onView(withText(R.string.error_amount)).check(matches(isDisplayed()));
    }

    @Test
    public void testCash_cancelFlow() {
        fillIncomeConcept();

        onView(withText(R.string.msg_cash)).perform(click());
        onView(withText(R.string.msg_cancel)).perform(closeSoftKeyboard()).perform(click());
        onView(withText(R.string.title_payment_methods)).check(matches(isDisplayed()));
    }

    @Test
    public void testCash_isCancelable() {
        fillIncomeConcept();

        onView(withText(R.string.msg_cash)).perform(click());
        onView(withId(R.id.edit_cash_amount)).perform(closeSoftKeyboard()).check(matches(isDisplayed()));
        onView(withId(R.id.edit_cash_amount)).perform(pressBack());
        onView(withText(R.string.title_payment_methods)).check(matches(isDisplayed()));
    }

    @Test
    public void testFoodCoupon_isAccepted() {
        fillIncomeConcept();

        onView(withText(R.string.msg_food_coupons)).perform(click());
        onView(withId(R.id.edit_food_coupon_amount)).perform(typeText("17500.09"));
        onView(withText(R.string.msg_accept)).perform(click());

        onView(withId(R.id.text_total_amount)).check(matches(isDisplayed())).check(matches(withText("$17500.09")));
    }

    @Test
    public void testFoodCoupon_errorIfAcceptWithEmptyAmount() {
        fillIncomeConcept();

        onView(withText(R.string.msg_food_coupons)).perform(click());
        onView(withText(R.string.msg_accept)).perform(closeSoftKeyboard()).perform(click());
        onView(withText(R.string.error_amount)).check(matches(isDisplayed()));
    }

    @Test
    public void testFoodCoupon_cancelFlow() {
        fillIncomeConcept();

        onView(withText(R.string.msg_food_coupons)).perform(click());
        onView(withText(R.string.msg_cancel)).perform(closeSoftKeyboard()).perform(click());
        onView(withText(R.string.title_payment_methods)).check(matches(isDisplayed()));
    }

    @Test
    public void testFoodCoupon_isCancelable() {
        fillIncomeConcept();

        onView(withText(R.string.msg_food_coupons)).perform(click());
        onView(withId(R.id.edit_food_coupon_amount)).perform(closeSoftKeyboard()).check(matches(isDisplayed()));
        onView(withId(R.id.edit_food_coupon_amount)).perform(pressBack());
        onView(withText(R.string.title_payment_methods)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegistrationSomePaymentMethods() {
        fillIncomeConcept();

        onView(withText(R.string.msg_cash)).perform(click());
        onView(withId(R.id.edit_cash_amount)).perform(typeText("1000.90"));
        onView(withText("Aceptar")).perform(click());

        onView(withText(R.string.msg_food_coupons)).perform(click());
        onView(withId(R.id.edit_food_coupon_amount)).perform(typeText("500.10"));
        onView(withText("Aceptar")).perform(click());

        onView(withId(R.id.text_total_amount)).check(matches(isDisplayed())).check(matches(withText("$1501.00")));
    }

    @Test
    public void testClickSaveButton_withNoPaymentMethodAdded() {
        fillIncomeConcept();

        onView(withId(R.id.button_save_payment_methods)).perform(click());
        onView(withText(R.string.msg_alert_empty_payment_methods)).check(matches(isDisplayed()));
        onView(withText(R.string.msg_accept)).check(matches(isDisplayed()));
    }

    @Test
    public void testDebitCard_registrationViewIsLaunched() {
        fillIncomeConcept();

        Intents.init();// start recording the fired intents

        onView(withText(R.string.msg_debit_card)).perform(click());

        intended(hasComponent(DebitCardActivity.class.getName()));
        Intents.release(); // clear the Intents state and stop recording intents
    }

    @Test
    public void testClickSaveButton_processIncome() {
        fillIncomeConcept();

        onView(withText(R.string.msg_cash)).perform(click());
        onView(withId(R.id.edit_cash_amount)).perform(typeText("17500"));
        onView(withText(R.string.msg_accept)).perform(click());

        onView(withId(R.id.button_save_payment_methods)).perform(click());

        onView(withId(R.id.confirm_income_title)).check(matches(isDisplayed()));
        onView(withId(R.id.total_amount_cash)).check(matches(isDisplayed())).check(matches(withText("$17500.00")));
        onView(withId(R.id.total_food_coupons)).check(matches(isDisplayed())).check(matches(withText("$0")));
        onView(withId(R.id.total_amount_debit_cards)).check(matches(isDisplayed())).check(matches(withText("$0")));
        onView(withId(R.id.total_amount_income)).check(matches(isDisplayed())).check(matches(withText("$17500.00")));
        onView(withText(R.string.msg_accept)).perform(click());

        doNothing().when(presenter).registerIncome(any(IncomeDto.class));

        verify(presenter).registerIncome(any(IncomeDto.class));
    }

    @Test
    public void testClickEditButton_launchMovementsView() {
        fillIncomeConcept();

        Intents.init();// start recording the fired intents

        onView(withContentDescription(R.string.msg_action_bar_edit_action)).perform(click());

        intended(hasComponent(MovementsActivity.class.getName()));
        Intents.release(); // clear the Intents state and stop recording intents
    }


    private void fillIncomeConcept() {
        onView(withId(R.id.edit_concept_of_income)).perform(typeText("This a test concept"));
        onView(withText(R.string.msg_accept)).perform(click());
    }

}
