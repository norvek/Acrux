package io.dojogeek.adminibot.views;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AddPaymentMethodActivityTest.class,
        CardCreationActivityTest.class,
        CashActivityTest.class,
        CheckActivityTest.class,
        CreditCardActivityTest.class,
        CreditCardDetailActivityTest.class,
        FoodCouponsActivityTest.class,
        MyCashActivityTest.class,
        MyCreditCardsActivityTest.class,
        MyFoodCouponsActivityTest.class,
        PaymentMethodsActivityTest.class
})
public class ViewsSuiteTest {
}
