package io.dojogeek.adminibot.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dagger.AdminiBotComponent;
import dagger.AdminiBotModule;
import dagger.AppComponent;
import io.dojogeek.adminibot.R;
import io.dojogeek.adminibot.adapters.SimpleItemAdapter;
import io.dojogeek.adminibot.dtos.DtoSimpleAdapter;
import io.dojogeek.adminibot.presenters.CashPresenter;
import io.dojogeek.adminibot.presenters.MyCashPresenter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MyCashActivity.class)
public class MyCashActivityTest {

    @Mock
    private RecyclerView mRecyclerView;

    @Mock
    private TextView mTotalCash;

    @Mock
    public MyCashPresenter mMyCashPresenter;

    @InjectMocks
    @Spy
    private MyCashActivity mCashActivity = new MyCashActivity();

    @Test
    public void testGetLayoutActivity_returnCorrectLayout() {

        int currentLayout = mCashActivity.getLayoutActivity();

        assertThat(currentLayout, is(R.layout.activity_my_cash));
    }

    @Test
    public void testSetupComponent_injectActivity() throws Exception {

        AppComponent appComponent = mock(AppComponent.class);

        AdminiBotModule adminiBotModule = mock(AdminiBotModule.class);
        whenNew(AdminiBotModule.class).withArguments(mCashActivity).thenReturn(adminiBotModule);

        AdminiBotComponent adminiBotComponent = mock(AdminiBotComponent.class);
        when(appComponent.plus(adminiBotModule)).thenReturn(adminiBotComponent);

        mCashActivity.setupComponent(appComponent);

        verify(appComponent).plus(adminiBotModule);
        verify(adminiBotComponent).inject(mCashActivity);
    }

    @Test
    public void testLoadViews_setViews() {

        doReturn(mRecyclerView).when(mCashActivity).findViewById(R.id.my_recycler_view);
        doReturn(mTotalCash).when(mCashActivity).findViewById(R.id.total_cash);

        mCashActivity.loadViews();

        verify(mCashActivity).findViewById(R.id.my_recycler_view);
        verify(mCashActivity).findViewById(R.id.total_cash);
    }

    @Test
    public void testLoadDataView_setTitle() {

        doNothing().when(mCashActivity).setTitle(R.string.title_my_cash_activity);

        mCashActivity.loadDataView();

        verify(mCashActivity).setTitle(R.string.title_my_cash_activity);
    }

    @Test
    public void testLoadDataView_showCreditCardsList() throws Exception {

        doNothing().when(mCashActivity).setTitle(R.string.title_my_cash_activity);

        mCashActivity.loadDataView();

        verify(mMyCashPresenter).obtainCash();

    }

    @Test
    public void testUnnusedView_closeConnections() {

        mCashActivity.closeConnections();

        verify(mMyCashPresenter).unnusedView();
    }

    @Test
    public void test_listMyCash() throws Exception {

        LinearLayoutManager linearLayoutManagerMock = mock(LinearLayoutManager.class);
        whenNew(LinearLayoutManager.class).withArguments(mCashActivity).thenReturn(linearLayoutManagerMock);

        List<DtoSimpleAdapter> simpleAdapterList = new ArrayList<>();

        SimpleItemAdapter simpleItemAdapterMock = mock(SimpleItemAdapter.class);
        whenNew(SimpleItemAdapter.class).withArguments(mCashActivity, simpleAdapterList).
                thenReturn(simpleItemAdapterMock);

        mCashActivity.listMyCash(simpleAdapterList);

        verify(mRecyclerView).setHasFixedSize(true);
        verify(mRecyclerView).setLayoutManager(linearLayoutManagerMock);
        verify(mRecyclerView).setAdapter(simpleItemAdapterMock);
    }

    @Test
    public void testShowTotalCash_setValueToView() {

        BigDecimal totalCash = new BigDecimal(873.90);

        mCashActivity.showTotalCash(totalCash);

        verify(mTotalCash).setText(totalCash.toString());

    }
}
