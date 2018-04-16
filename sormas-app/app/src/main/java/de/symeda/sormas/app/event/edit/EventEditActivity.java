package de.symeda.sormas.app.event.edit;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import de.symeda.sormas.api.event.EventStatus;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.app.AbstractSormasActivity;
import de.symeda.sormas.app.BaseEditActivity;
import de.symeda.sormas.app.BaseEditActivityFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.event.Event;
import de.symeda.sormas.app.backend.event.EventDao;
import de.symeda.sormas.app.component.menu.LandingPageMenuItem;
import de.symeda.sormas.app.core.BoolResult;
import de.symeda.sormas.app.core.async.IJobDefinition;
import de.symeda.sormas.app.core.async.ITaskExecutor;
import de.symeda.sormas.app.core.async.ITaskResultCallback;
import de.symeda.sormas.app.core.async.TaskExecutorFor;
import de.symeda.sormas.app.core.async.TaskResultHolder;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.core.notification.NotificationType;
import de.symeda.sormas.app.databinding.FragmentEventEditLayoutBinding;
import de.symeda.sormas.app.event.edit.sub.EventNewPersonsInvolvedActivity;
import de.symeda.sormas.app.rest.RetroProvider;
import de.symeda.sormas.app.rest.SynchronizeDataAsync;
import de.symeda.sormas.app.shared.EventFormNavigationCapsule;
import de.symeda.sormas.app.util.ConstantHelper;
import de.symeda.sormas.app.util.ErrorReportingHelper;
import de.symeda.sormas.app.util.MenuOptionsHelper;
import de.symeda.sormas.app.util.SyncCallback;

/**
 * Created by Orson on 07/02/2018.
 * <p>
 * www.technologyboard.org
 * sampson.orson@gmail.com
 * sampson.orson@technologyboard.org
 */

public class EventEditActivity extends BaseEditActivity<Event> {

    public static final String TAG = EventEditActivity.class.getSimpleName();

    private AsyncTask saveTask;
    private final int DATA_XML_PAGE_MENU = R.xml.data_form_page_alert_menu; // "xml/data_edit_page_alert_menu.xml";

    private static final int MENU_INDEX_EVENT_INFO = 0;
    private static final int MENU_INDEX_EVENT__PERSON_INVOLVED = 1;
    private static final int MENU_INDEX_EVENT_TASK = 2;

    private boolean showStatusFrame = false;
    private boolean showTitleBar = true;
    private boolean showPageMenu = false;

    private EventStatus pageStatus = null;
    private String recordUuid = null;
    private int activeMenuKey = ConstantHelper.INDEX_FIRST_MENU;
    private BaseEditActivityFragment activeFragment = null;

    private MenuItem saveMenu = null;
    private MenuItem addMenu = null;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //SaveFilterStatusState(outState, filterStatus);
        SavePageStatusState(outState, pageStatus);
        SaveRecordUuidState(outState, recordUuid);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeActivity(Bundle arguments) {
        //filterStatus = (EventStatus) getFilterStatusArg(arguments);
        pageStatus = (EventStatus) getPageStatusArg(arguments);
        recordUuid = getRecordUuidArg(arguments);

        this.showStatusFrame = true;
        this.showTitleBar = true;
        this.showPageMenu = true;
    }

    @Override
    protected Event getActivityRootData(String recordUuid) {
        return DatabaseHelper.getEventDao().queryUuid(recordUuid);
    }

    @Override
    protected Event getActivityRootDataIfRecordUuidNull() {
        return null;
    }

    @Override
    public BaseEditActivityFragment getActiveEditFragment(Event activityRootData) throws IllegalAccessException, InstantiationException {
        if (activeFragment == null) {
            EventFormNavigationCapsule dataCapsule = new EventFormNavigationCapsule(EventEditActivity.this,
                    recordUuid, pageStatus);
            activeFragment = EventEditFragment.newInstance(this, dataCapsule, activityRootData);
        }

        return activeFragment;
    }

    @Override
    public boolean showStatusFrame() {
        return showStatusFrame;
    }

    @Override
    public boolean showTitleBar() {
        return showTitleBar;
    }

    @Override
    public boolean showPageMenu() {
        return showPageMenu;
    }

    @Override
    public Enum getPageStatus() {
        return pageStatus;
    }

    @Override
    public int getPageMenuData() {
        return DATA_XML_PAGE_MENU;
    }

    @Override
    protected BaseEditActivityFragment getNextFragment(LandingPageMenuItem menuItem, Event activityRootData) {
        EventFormNavigationCapsule dataCapsule = new EventFormNavigationCapsule(EventEditActivity.this,
                recordUuid, pageStatus);

        try {
            if (menuItem.getKey() == MENU_INDEX_EVENT_INFO) {
                activeFragment = EventEditFragment.newInstance(this, dataCapsule, activityRootData);
            } else if (menuItem.getKey() == MENU_INDEX_EVENT__PERSON_INVOLVED) {
                activeFragment = EventEditPersonsInvolvedListFragment.newInstance(this, dataCapsule, activityRootData);
            } else if (menuItem.getKey() == MENU_INDEX_EVENT_TASK) {
                activeFragment = EventEditTaskListFragement.newInstance(this, dataCapsule, activityRootData);
            }

            //processActionbarMenu();
        } catch (InstantiationException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        }

        return activeFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getSaveMenu().setTitle(R.string.action_save_event);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!MenuOptionsHelper.handleEditModuleOptionsItemSelected(this, item))
            return super.onOptionsItemSelected(item);

        return true;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.heading_level4_event_edit;
    }

    @Override
    public void gotoNewView() {
        EventFormNavigationCapsule dataCapsule = (EventFormNavigationCapsule)new EventFormNavigationCapsule(getContext(), pageStatus)
                .setEventUuid(recordUuid);
        EventNewPersonsInvolvedActivity.goToActivity(EventEditActivity.this, dataCapsule);
    }

    @Override
    public void saveData() {
        if (activeFragment == null)
            return;

        FragmentEventEditLayoutBinding binding = (FragmentEventEditLayoutBinding)activeFragment.getContentBinding();
        EventDao eventDao = DatabaseHelper.getEventDao();
        Event record = (Event)activeFragment.getPrimaryData();

        if (record == null)
            return;


        //TODO: Validation
        //EventValidator.clearErrorsForEventData(binding);

        /*if (!EventValidator.validateEventData(record, binding)) {
            //Validation Failed
            NotificationHelper.showNotification((INotificationContext) this, NotificationType.ERROR, resultStatus.getMessage());
            return;
        }*/

        try {
            ITaskExecutor executor = TaskExecutorFor.job(new IJobDefinition() {
                private EventDao dao;
                private Event event;
                private String saveUnsuccessful;

                @Override
                public void preExecute(BoolResult resultStatus, TaskResultHolder resultHolder) {
                    saveUnsuccessful = String.format(getResources().getString(R.string.snackbar_save_error), getResources().getString(R.string.entity_event));
                }

                @Override
                public void execute(BoolResult resultStatus, TaskResultHolder resultHolder) {
                    try {
                        this.dao.saveAndSnapshot(this.event);
                    } catch (DaoException e) {
                        Log.e(getClass().getName(), "Error while trying to save event", e);
                        resultHolder.setResultStatus(new BoolResult(false, saveUnsuccessful));
                        ErrorReportingHelper.sendCaughtException(tracker, e, event, true);
                    }
                }

                private IJobDefinition init(EventDao dao, Event event) {
                    this.dao = dao;
                    this.event = event;

                    return this;
                }

            }.init(eventDao, record));
            saveTask = executor.execute(new ITaskResultCallback() {
                private Event event;

                @Override
                public void taskResult(BoolResult resultStatus, TaskResultHolder resultHolder) {
                    //getActivityCommunicator().hidePreloader();
                    //getActivityCommunicator().showFragmentView();

                    if (resultHolder == null){
                        return;
                    }

                    if (!resultStatus.isSuccess()) {
                        NotificationHelper.showNotification(EventEditActivity.this, NotificationType.ERROR, resultStatus.getMessage());
                        return;
                    } else {
                        NotificationHelper.showNotification(EventEditActivity.this, NotificationType.SUCCESS, "Event " + DataHelper.getShortUuid(event.getUuid()) + " saved");
                    }

                    if (RetroProvider.isConnected()) {
                        SynchronizeDataAsync.callWithProgressDialog(SynchronizeDataAsync.SyncMode.ChangesOnly, EventEditActivity.this, new SyncCallback() {
                            @Override
                            public void call(boolean syncFailed, String syncFailedMessage) {
                                if (syncFailed) {
                                    NotificationHelper.showNotification(EventEditActivity.this, NotificationType.WARNING, String.format(getResources().getString(R.string.snackbar_sync_error_saved), getResources().getString(R.string.entity_event)));
                                } else {
                                    NotificationHelper.showNotification(EventEditActivity.this, NotificationType.SUCCESS, String.format(getResources().getString(R.string.snackbar_save_success), getResources().getString(R.string.entity_event)));
                                }
                                finish();
                            }
                        });
                    } else {
                        NotificationHelper.showNotification(EventEditActivity.this, NotificationType.SUCCESS, String.format(getResources().getString(R.string.snackbar_save_success), getResources().getString(R.string.entity_event)));
                        finish();
                    }

                }

                private ITaskResultCallback init(Event event) {
                    this.event = event;

                    return this;
                }

            }.init(record));
        } catch (Exception ex) {
            //getActivityCommunicator().hidePreloader();
            //getActivityCommunicator().showFragmentView();
        }
    }

    public static <TActivity extends AbstractSormasActivity> void
    goToActivity(Context fromActivity, EventFormNavigationCapsule dataCapsule) {
        BaseEditActivity.goToActivity(fromActivity, EventEditActivity.class, dataCapsule);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (saveTask != null && !saveTask.isCancelled())
            saveTask.cancel(true);
    }

}