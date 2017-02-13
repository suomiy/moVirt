package org.ovirt.mobile.movirt.ui.vms;

import android.database.Cursor;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.ovirt.mobile.movirt.Broadcasts;
import org.ovirt.mobile.movirt.R;
import org.ovirt.mobile.movirt.model.Snapshot;
import org.ovirt.mobile.movirt.provider.SortOrder;
import org.ovirt.mobile.movirt.ui.ProgressBarResponse;
import org.ovirt.mobile.movirt.ui.listfragment.VmBoundResumeSyncableBaseEntityListFragment;
import org.ovirt.mobile.movirt.ui.listfragment.spinner.CustomSort;
import org.ovirt.mobile.movirt.util.CursorHelper;
import org.ovirt.mobile.movirt.util.DateUtils;

import java.util.List;

import static org.ovirt.mobile.movirt.provider.OVirtContract.Snapshot.DATE;
import static org.ovirt.mobile.movirt.provider.OVirtContract.Snapshot.NAME;
import static org.ovirt.mobile.movirt.provider.OVirtContract.Snapshot.PERSIST_MEMORYSTATE;
import static org.ovirt.mobile.movirt.provider.OVirtContract.Snapshot.SNAPSHOT_STATUS;
import static org.ovirt.mobile.movirt.provider.OVirtContract.Snapshot.TYPE;

@EFragment(R.layout.fragment_base_entity_list)
public class VmSnapshotsFragment extends VmBoundResumeSyncableBaseEntityListFragment<Snapshot> {
    private static final String TAG = VmSnapshotsFragment.class.getSimpleName();

    public VmSnapshotsFragment() {
        super(Snapshot.class);
    }

    @Override
    protected CursorAdapter createCursorAdapter() {
        SimpleCursorAdapter snapshotListAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.snapshot_list_item,
                null,
                new String[]{NAME, SNAPSHOT_STATUS, DATE, PERSIST_MEMORYSTATE},
                new int[]{R.id.snapshot_description, R.id.snapshot_status, R.id.snapshot_date, R.id.snapshot_persist_memorystate}, 0);
        snapshotListAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                TextView textView = (TextView) view;

                if (columnIndex == cursor.getColumnIndex(NAME)) {
                    String name = cursor.getString(columnIndex);
                    textView.setText(name);
                } else if (columnIndex == cursor.getColumnIndex(DATE)) {
                    String date = DateUtils.convertDateToString(getActivity(), cursor.getLong(columnIndex));
                    textView.setText(date);
                } else if (columnIndex == cursor.getColumnIndex(SNAPSHOT_STATUS)) {
                    String status = cursor.getString(columnIndex);
                    textView.setText(status == null ? getString(R.string.NA) : status.replace("_", " ").toUpperCase());
                } else if (columnIndex == cursor.getColumnIndex(PERSIST_MEMORYSTATE)) {
                    textView.setText(getString(R.string.snapshot_memory));
                    textView.setVisibility((new CursorHelper(cursor)).getBoolean(columnIndex) ? View.VISIBLE : View.GONE);
                }

                return true;
            }
        });

        return snapshotListAdapter;
    }

    @Override
    public CustomSort getCustomSort() {
        return new CustomSort(new CustomSort.CustomSortEntry[]{
                new CustomSort.CustomSortEntry(SNAPSHOT_STATUS, SortOrder.ASCENDING),
                new CustomSort.CustomSortEntry(TYPE, SortOrder.ASCENDING),
                new CustomSort.CustomSortEntry(NAME, SortOrder.ASCENDING)
        });
    }

    @Background
    @Override
    public void onRefresh() {
        entityFacade.syncAll(new ProgressBarResponse<List<Snapshot>>(this), getVmId());
    }

    @Background
    @Receiver(actions = Broadcasts.IN_SYNC, registerAt = Receiver.RegisterAt.OnResumeOnPause)
    protected void syncingChanged(@Receiver.Extra(Broadcasts.Extras.SYNCING) boolean syncing) {
        if (syncing) {
            entityFacade.syncAll(getVmId());
        }
    }
}

