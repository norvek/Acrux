package io.dojogeek.adminibot.daos;

import android.content.Context;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import io.dojogeek.adminibot.R;
import io.dojogeek.adminibot.models.ExpenseModel;
import io.dojogeek.adminibot.models.ExpenseTypeModel;
import io.dojogeek.adminibot.sqlite.AdminiBotSQLiteOpenHelper;
import io.dojogeek.adminibot.sqlite.ExpensesTypesContract;
import io.dojogeek.adminibot.utiltest.CreatorModels;

import static android.support.test.InstrumentationRegistry.getTargetContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ExpenseTypeDaoImplTest {

    private static final int NO_VALUE = 0;
    private static final int OPERATIONAL_ERROR = -1;
    private ExpenseTypeDao mExpenseTypeDao;
    private Context mContext;

    @Before
    public void setup() {
        mContext = getTargetContext();

        mExpenseTypeDao = new ExpenseTypeDaoImpl(mContext);
        mExpenseTypeDao.openConection();
    }

    @After
    public void finishTest() {
        mExpenseTypeDao.closeConection();
        mContext.deleteDatabase(AdminiBotSQLiteOpenHelper.DATABASE_NAME);
    }

    @Test
    public void expenseTypeDao_creationExpenseType_isTrue() {

        ExpenseTypeModel expenseTypeModel = CreatorModels.createExpenseTypeModel();

        long insertedExpenseTypeId = mExpenseTypeDao.createExpenseType(expenseTypeModel);

        assertNotEquals(OPERATIONAL_ERROR, insertedExpenseTypeId);
    }

    @Test
    public void expenseTypeDao_creationAndObtainingExpenseTypeById_isTrue() {

        ExpenseTypeModel expenseTypeModel = CreatorModels.createExpenseTypeModel();

        long insertedExpenseTypeId = mExpenseTypeDao.createExpenseType(expenseTypeModel);

        ExpenseTypeModel expenseType = mExpenseTypeDao.getExpenseTypeById(insertedExpenseTypeId);

        assertNotNull(expenseType);
        assertEquals(expenseTypeModel.name, expenseType.name);
        assertEquals(expenseTypeModel.description, expenseType.description);

    }

    @Test
    public void expenseTypeDao_obtainingAllExpensesTypesInitiallyInserted_isTrue() {

        int initiallyInsertedExpensesTypes = 5;

        List<ExpenseTypeModel> actualExpenseTypeModelList = mExpenseTypeDao.getExpensesTypes();

        assertNotNull(actualExpenseTypeModelList);
        assertTrue(!actualExpenseTypeModelList.isEmpty());
        assertEquals(initiallyInsertedExpensesTypes, actualExpenseTypeModelList.size());

        compareExpensesModelsInitiallyInserted(actualExpenseTypeModelList);
    }

    @Test
    public void expenseTypeDao_creationUpdatingAndObtainingExpenseTypeById_isTrue() {

        ExpenseTypeModel expenseTypeModel = CreatorModels.createExpenseTypeModel();

        long insertedExpenseTypeId = mExpenseTypeDao.createExpenseType(expenseTypeModel);

        ExpenseTypeModel expectedNewExpenseTypeModel = changeExpenseTypeValues(expenseTypeModel);

        String where = ExpensesTypesContract.ExpenseType._ID + "= " + insertedExpenseTypeId;

        long rowsUpdated = mExpenseTypeDao.updateExpensetype(expectedNewExpenseTypeModel, where);

        assertNotEquals(NO_VALUE, rowsUpdated);

        ExpenseTypeModel actualExpenseTypeModel = mExpenseTypeDao.getExpenseTypeById(insertedExpenseTypeId);

        compareExpensesModels(expectedNewExpenseTypeModel, actualExpenseTypeModel);
    }

    @Test
    public void expenseTypeDao_deletionOfExpenseTypeInitiallyInserted_isTrue() {

        long initiallyInsertedExpenseTypeId = 3;

        long removedRows = mExpenseTypeDao.removeExpenseTypeById(initiallyInsertedExpenseTypeId);

        assertEquals(1, removedRows);

    }


    private ExpenseTypeModel changeExpenseTypeValues(ExpenseTypeModel expenseTypeModel) {

        expenseTypeModel.name = R.string.expenses_types_luxuries;
        expenseTypeModel.description = R.string.expenses_types_clothes;

        return expenseTypeModel;
    }

    private void compareExpensesModels(ExpenseTypeModel expectedExpenseTypeModel,
                                       ExpenseTypeModel actualExpenseTypeModel) {

        assertEquals(expectedExpenseTypeModel.name, actualExpenseTypeModel.name);
        assertEquals(expectedExpenseTypeModel.description, actualExpenseTypeModel.description);

    }

    private void compareExpensesModelsInitiallyInserted(List<ExpenseTypeModel> actualExpenseTypeModelList) {

        for (int index = 0; index < actualExpenseTypeModelList.size(); index++) {
            ExpenseTypeModel actualExpenseTypeModel = actualExpenseTypeModelList.get(index);

            String expectedName = mContext.getString(ExpensesTypesContract.EXPENSES_TYPES[index]);
            String expectedDescription = mContext.getString(ExpensesTypesContract.EXPENSES_TYPES_DESCRIPTIONS[index]);

            assertEquals(expectedName, mContext.getString(actualExpenseTypeModel.name));
            assertEquals(expectedDescription, mContext.getString(actualExpenseTypeModel.description));

        }

    }

}
