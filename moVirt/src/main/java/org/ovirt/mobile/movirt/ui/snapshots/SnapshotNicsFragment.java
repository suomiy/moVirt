package org.ovirt.mobile.movirt.ui.snapshots;

import android.database.Cursor;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.ovirt.mobile.movirt.Broadcasts;
import org.ovirt.mobile.movirt.R;
import org.ovirt.mobile.movirt.model.SnapshotNic;
import org.ovirt.mobile.movirt.ui.ProgressBarResponse;
import org.ovirt.mobile.movirt.ui.listfragment.SnapshotBoundResumeSyncableBaseEntityListFragment;
import org.ovirt.mobile.movirt.ui.listfragment.spinner.ItemName;
import org.ovirt.mobile.movirt.ui.listfragment.spinner.SortEntry;
import org.ovirt.mobile.movirt.ui.listfragment.spinner.SortOrderType;
import org.ovirt.mobile.movirt.util.CursorHelper;

import java.util.List;

import static org.ovirt.mobile.movirt.provider.OVirtContract.Nic.LINKED;
import static org.ovirt.mobile.movirt.provider.OVirtContract.Nic.MAC_ADDRESS;
import static org.ovirt.mobile.movirt.provider.OVirtContract.Nic.NAME;
import static org.ovirt.mobile.movirt.provider.OVirtContract.Nic.PLUGGED;

@EFragment(R.layout.fragment_base_entity_list)
public class SnapshotNicsFragment extends SnapshotBoundResumeSyncableBaseEntityListFragment<SnapshotNic> {

    public SnapshotNicsFragment() {
        super(SnapshotNic.class);
    }

    @Override
    protected CursorAdapter createCursorAdapter() {
        SimpleCursorAdapter nicListAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.nic_list_item,
                null,
                new String[]{NAME, LINKED, PLUGGED},
                new int[]{R.id.nic_mac, R.id.nic_status, R.id.nic_para}, 0);
        nicListAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                if (columnIndex == cursor.getColumnIndex(NAME)) {
                    TextView textView = (TextView) view;
                    String name = cursor.getString(columnIndex);
                    String mac = cursor.getString(cursor.getColumnIndex(MAC_ADDRESS));
                    textView.setText(getString(R.string.nic_name_and_address, name, mac));
                } else if (columnIndex == cursor.getColumnIndex(LINKED)) {
                    ImageView imageView = (ImageView) view;
                    CursorHelper cursorHelper = new CursorHelper(cursor);
                    boolean linked = cursorHelper.getBoolean(columnIndex);
                    boolean plugged = cursorHelper.getBoolean(cursor.getColumnIndex(PLUGGED));
                    imageView.setImageResource((linked && plugged) ? R.drawable.icn_play : R.drawable.icn_stop);
                } else if (columnIndex == cursor.getColumnIndex(PLUGGED)) {
                    TextView textView = (TextView) view;
                    CursorHelper cursorHelper = new CursorHelper(cursor);
                    boolean plugged = cursorHelper.getBoolean(columnIndex);
                    boolean linked = cursorHelper.getBoolean(cursor.getColumnIndex(LINKED));
                    textView.setText(getString(R.string.nic_para, linked, plugged));
                }

                return true;
            }
        });

        return nicListAdapter;
    }

    @Override
    public SortEntry[] getSortEntries() {
        return new SortEntry[]{
                new SortEntry(new ItemName(NAME), SortOrderType.A_TO_Z)
        };
    }

    @Background
    @Receiver(actions = Broadcasts.IN_SYNC, registerAt = Receiver.RegisterAt.OnResumeOnPause)
    protected void syncingChanged(@Receiver.Extra(Broadcasts.Extras.SYNCING) boolean syncing) {
        if (syncing) {
            entityFacade.syncAll(getVmId(), getSnapshotId());
        }
    }

    @Background
    @Override
    public void onRefresh() {
        entityFacade.syncAll(new ProgressBarResponse<List<SnapshotNic>>(this), getVmId(), getSnapshotId());
    }
}
