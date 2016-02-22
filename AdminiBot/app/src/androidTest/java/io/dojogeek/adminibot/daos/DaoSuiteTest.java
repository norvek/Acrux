package io.dojogeek.adminibot.daos;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({UserDaoImplTest.class, ExpenseTypeDaoImplTest.class, ExpenseDaoImplTest.class,
        IncomeDaoImplTest.class, BankCardDaoImplTest.class, CardDetailDaoImplTest.class,
        OtherPaymentMethodDaoImplTest.class, MovementExpenseBankCardDaoImplTest.class,
        MovementIncomeBankCardDaoImplTest.class})
public class DaoSuiteTest {

}
