package io.dojogeek.adminibot.presenters;

import android.database.SQLException;

import java.math.BigDecimal;
import java.util.List;

import io.dojogeek.adminibot.daos.BankCardDao;
import io.dojogeek.adminibot.daos.BankCardDaoImpl;
import io.dojogeek.adminibot.daos.ExpenseBankCardDao;
import io.dojogeek.adminibot.daos.ExpenseBankCardDaoImpl;
import io.dojogeek.adminibot.daos.ExpenseDao;
import io.dojogeek.adminibot.daos.ExpenseDaoImpl;
import io.dojogeek.adminibot.daos.ExpenseOtherPaymentMethodDao;
import io.dojogeek.adminibot.daos.ExpenseOtherPaymentMethodDaoImpl;
import io.dojogeek.adminibot.daos.OtherPaymentMethodDao;
import io.dojogeek.adminibot.daos.OtherPaymentMethodDaoImpl;
import io.dojogeek.adminibot.daos.SQLiteGlobalDao;
import io.dojogeek.adminibot.exceptions.DataException;
import io.dojogeek.adminibot.models.BankCardModel;
import io.dojogeek.adminibot.models.ExpenseBankCardModel;
import io.dojogeek.adminibot.models.ExpenseModel;
import io.dojogeek.adminibot.models.ExpenseOtherPaymentMethodModel;
import io.dojogeek.adminibot.models.OtherPaymentMethodModel;
import io.dojogeek.adminibot.views.ExpenseCreation;

public class ExpenseCreationPresenterImpl implements ExpenseCreationPresenter {

    private ExpenseCreation mExpenseCreation;
    private ExpenseDao mExpenseDao;
    private ExpenseOtherPaymentMethodDao mExpenseOtherPaymentMethodDao;
    private OtherPaymentMethodDao mOtherPaymentMethodDao;
    private BankCardDao mBankCardDao;
    private ExpenseBankCardDao mExpenseBankCardDao;

    public ExpenseCreationPresenterImpl(ExpenseCreation expenseCreation, ExpenseDao expenseDao,
                                        OtherPaymentMethodDao otherPaymentMethodDao, BankCardDao bankCardDao,
                                        ExpenseOtherPaymentMethodDao expenseOtherPaymentMethodDao,
                                        ExpenseBankCardDao expenseBankCardDao) {

        mExpenseCreation = expenseCreation;
        mExpenseDao = expenseDao;
        mOtherPaymentMethodDao = otherPaymentMethodDao;
        mBankCardDao = bankCardDao;
        mExpenseOtherPaymentMethodDao = expenseOtherPaymentMethodDao;
        mExpenseBankCardDao = expenseBankCardDao;
    }

    @Override
    public void createExpense(ExpenseModel expenseModel) {

        try {
            beginTransactions();

            long insertedExpenseId = mExpenseDao.createExpense(expenseModel);

            registerOtherPaymentMethodsUsedForExpense(insertedExpenseId,
                    expenseModel.getOtherPaymentMethodModels());

            searchOtherPaymentMethodsAndUpdateAmountsSpent(expenseModel.getOtherPaymentMethodModels());

            registerBankCardsUsedForExpense(insertedExpenseId, expenseModel.getExpenseBankCardModels());

            searchBankCardsAndUpdateAmountsSpent(expenseModel.getExpenseBankCardModels());

            setTransactionsSuccessful();

            mExpenseCreation.successfulExpenseCreation();

        } catch (SQLException exception) {

            exception.printStackTrace();

            mExpenseCreation.errorExpenseCreation();

        } finally {
            endTransactions();
        }
    }

    private void registerOtherPaymentMethodsUsedForExpense(long expenseId,
                                                           List<ExpenseOtherPaymentMethodModel> paymentMethodModels) {

        for (ExpenseOtherPaymentMethodModel otherPaymentMethodModel: paymentMethodModels) {

            otherPaymentMethodModel.setExpenseId(expenseId);

            mExpenseOtherPaymentMethodDao.createExpenseOtherPaymentMethod(otherPaymentMethodModel);

        }
    }

    private void searchOtherPaymentMethodsAndUpdateAmountsSpent(List<ExpenseOtherPaymentMethodModel> paymentMethodModels) {

        for (ExpenseOtherPaymentMethodModel otherPaymentMethodModel: paymentMethodModels) {

            OtherPaymentMethodModel otherPaymentMethodModelToUpdate = getOtherPaymentMethodById(otherPaymentMethodModel.getId());

            otherPaymentMethodModelToUpdate =
                    updateAmountForOtherPaymentMethodModel(otherPaymentMethodModelToUpdate, otherPaymentMethodModel.getAmount());

            mOtherPaymentMethodDao.updateOtherPaymentMethod(otherPaymentMethodModelToUpdate, otherPaymentMethodModelToUpdate.getId());
        }
    }

    private OtherPaymentMethodModel getOtherPaymentMethodById(long id) {

        OtherPaymentMethodModel otherPaymentMethodModelToUpdate = null;

        try {
            otherPaymentMethodModelToUpdate = mOtherPaymentMethodDao.getOtherPaymentMethodById(id);

        } catch (DataException e) {
            e.printStackTrace();
        }

        return otherPaymentMethodModelToUpdate;
    }

    private OtherPaymentMethodModel updateAmountForOtherPaymentMethodModel(OtherPaymentMethodModel otherPaymentMethodModel,
                                                                           double amount) {

        double totalAmount = otherPaymentMethodModel.getAvailableCredit().doubleValue() - amount;
        otherPaymentMethodModel.setAvailableCredit(new BigDecimal(totalAmount));

        return otherPaymentMethodModel;
    }

    private void registerBankCardsUsedForExpense(long expenseId,
                                                 List<ExpenseBankCardModel> expenseBankCardModels) {

        for (ExpenseBankCardModel expenseBankCardModel : expenseBankCardModels) {

            expenseBankCardModel.setExpenseId(expenseId);

            mExpenseBankCardDao.createMovementExpenseBankCard(expenseBankCardModel);
        }
    }

    private void searchBankCardsAndUpdateAmountsSpent(List<ExpenseBankCardModel> expenseBankCardModels) {

        for (ExpenseBankCardModel expenseBankCardModel : expenseBankCardModels) {

            BankCardModel bankCardModelToUpdate = getBankCardModelById(expenseBankCardModel.getBankCardId());

            bankCardModelToUpdate = updateAmountForBankCardModel(bankCardModelToUpdate, expenseBankCardModel.getAmount());

            mBankCardDao.updateBankCard(bankCardModelToUpdate, bankCardModelToUpdate.getId());
        }
    }

    private BankCardModel getBankCardModelById(long id) {

        BankCardModel responseBankCardModel = null;

        try {
            responseBankCardModel =
                    mBankCardDao.getBankCardById(id);

        } catch (DataException e) {
            e.printStackTrace();
        }

        return responseBankCardModel;
    }

    private BankCardModel updateAmountForBankCardModel(BankCardModel bankCardModel, double amount) {

        double availableCredit = bankCardModel.getAvailableCredit();

        bankCardModel.setAvailableCredit(availableCredit + amount);

        return bankCardModel;
    }

    private void beginTransactions() {

        beginTransaction((ExpenseDaoImpl) mExpenseDao);
        beginTransaction((ExpenseOtherPaymentMethodDaoImpl) mExpenseOtherPaymentMethodDao);
        beginTransaction((OtherPaymentMethodDaoImpl) mOtherPaymentMethodDao);
        beginTransaction((BankCardDaoImpl) mBankCardDao);
        beginTransaction((ExpenseBankCardDaoImpl) mExpenseBankCardDao);

    }

    private void setTransactionsSuccessful() {
        setTransactionSuccessful((ExpenseDaoImpl) mExpenseDao);
        setTransactionSuccessful((ExpenseOtherPaymentMethodDaoImpl) mExpenseOtherPaymentMethodDao);
        setTransactionSuccessful((OtherPaymentMethodDaoImpl) mOtherPaymentMethodDao);
        setTransactionSuccessful((BankCardDaoImpl) mBankCardDao);
        setTransactionSuccessful((ExpenseBankCardDaoImpl) mExpenseBankCardDao);
    }

    private void endTransactions() {

        endTransaction((ExpenseDaoImpl) mExpenseDao);
        endTransaction((ExpenseOtherPaymentMethodDaoImpl) mExpenseOtherPaymentMethodDao);
        endTransaction((OtherPaymentMethodDaoImpl) mOtherPaymentMethodDao);
        endTransaction((BankCardDaoImpl) mBankCardDao);
        endTransaction((ExpenseBankCardDaoImpl) mExpenseBankCardDao);

    }

    private void beginTransaction(SQLiteGlobalDao sqLiteGlobalDao) {
        sqLiteGlobalDao.beginTransaction();
    }

    private void endTransaction(SQLiteGlobalDao sqLiteGlobalDao) {
        sqLiteGlobalDao.endTransaction();
    }

    private void setTransactionSuccessful(SQLiteGlobalDao sqLiteGlobalDao) {
        sqLiteGlobalDao.setTransactionSuccessful();
    }

}
