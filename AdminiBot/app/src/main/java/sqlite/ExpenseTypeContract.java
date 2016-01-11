package sqlite;

import android.provider.BaseColumns;

import io.dojogeek.adminibot.R;

import static sqlite.SQLiteConstants.CLOSE_PARENTHESIS;
import static sqlite.SQLiteConstants.COMMA_SEP;
import static sqlite.SQLiteConstants.CREATE_TABLE;
import static sqlite.SQLiteConstants.DOUBLE;
import static sqlite.SQLiteConstants.DROP_TABLE_IF_EXIST;
import static sqlite.SQLiteConstants.INTEGER;
import static sqlite.SQLiteConstants.OPEN_PARENTHESIS;
import static sqlite.SQLiteConstants.PRIMARY_KEY;
import static sqlite.SQLiteConstants.TEXT_TYPE;
import static sqlite.SQLiteConstants.INSERT_INTO;
import static sqlite.SQLiteConstants.OPEN_QUOTE;
import static sqlite.SQLiteConstants.CLOSE_QUOTE;
import static sqlite.SQLiteConstants.VALUES;

public class ExpenseTypeContract {

    private static int [] expensesTypes = {R.string.expenses_types_foods,
            R.string.expenses_types_clothes, R.string.expenses_types_drinks, R.string.expenses_types_medicals,
            R.string.expenses_types_luxuries};
    private static int [] expensesTypesDescriptions = {R.string.expenses_types_foods_description,
            R.string.expenses_types_clothes_description, R.string.expenses_types_drinks_description,
            R.string.expenses_types_medicals_description, R.string.expenses_types_luxuries_description};

    public static abstract class ExpenseType implements BaseColumns {

        public static final String TABLE_NAME = "expenses_types ";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_NAME_NULLABLE = "null";

    }

    public static final String SQL_CREATE_ENTRIES = CREATE_TABLE + ExpenseType.TABLE_NAME + OPEN_PARENTHESIS +
            ExpenseType._ID + INTEGER + PRIMARY_KEY + COMMA_SEP +
            ExpenseType.COLUMN_NAME + INTEGER + COMMA_SEP +
            ExpenseType.COLUMN_DESCRIPTION + INTEGER + CLOSE_PARENTHESIS;

    public static final String SQL_INSERT_INITIAL_VALUES_1 = INSERT_INTO + ExpenseType.TABLE_NAME +
            VALUES + OPEN_PARENTHESIS + ExpenseType.COLUMN_NAME_NULLABLE + COMMA_SEP +
            OPEN_QUOTE + expensesTypes[0] + CLOSE_QUOTE + COMMA_SEP +
            OPEN_QUOTE + expensesTypesDescriptions[0] + CLOSE_QUOTE + CLOSE_PARENTHESIS;

    public static final String SQL_INSERT_INITIAL_VALUES_2 = INSERT_INTO + ExpenseType.TABLE_NAME +
            VALUES + OPEN_PARENTHESIS + ExpenseType.COLUMN_NAME_NULLABLE + COMMA_SEP +
            OPEN_QUOTE + expensesTypes[1] + CLOSE_QUOTE + COMMA_SEP +
            OPEN_QUOTE + expensesTypesDescriptions[1] + CLOSE_QUOTE + CLOSE_PARENTHESIS;

    public static final String SQL_INSERT_INITIAL_VALUES_3 = INSERT_INTO + ExpenseType.TABLE_NAME +
            VALUES + OPEN_PARENTHESIS + ExpenseType.COLUMN_NAME_NULLABLE + COMMA_SEP +
            OPEN_QUOTE + expensesTypes[2] + CLOSE_QUOTE + COMMA_SEP +
            OPEN_QUOTE + expensesTypesDescriptions[2] + CLOSE_QUOTE + CLOSE_PARENTHESIS;

    public static final String SQL_INSERT_INITIAL_VALUES_4 = INSERT_INTO + ExpenseType.TABLE_NAME +
            VALUES + OPEN_PARENTHESIS + ExpenseType.COLUMN_NAME_NULLABLE + COMMA_SEP +
            OPEN_QUOTE + expensesTypes[3] + CLOSE_QUOTE + COMMA_SEP +
            OPEN_QUOTE + expensesTypesDescriptions[3] + CLOSE_QUOTE + CLOSE_PARENTHESIS;

    public static final String SQL_INSERT_INITIAL_VALUES_5 = INSERT_INTO + ExpenseType.TABLE_NAME +
            VALUES + OPEN_PARENTHESIS + ExpenseType.COLUMN_NAME_NULLABLE + COMMA_SEP +
            OPEN_QUOTE + expensesTypes[4] + CLOSE_QUOTE + COMMA_SEP +
            OPEN_QUOTE + expensesTypesDescriptions[4] + CLOSE_QUOTE + CLOSE_PARENTHESIS;

    public static final String SQL_DELETE_ENTRIES = DROP_TABLE_IF_EXIST + ExpenseType.TABLE_NAME;

}
