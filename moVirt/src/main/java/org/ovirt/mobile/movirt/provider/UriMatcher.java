package org.ovirt.mobile.movirt.provider;

import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;

import org.ovirt.mobile.movirt.model.Cluster;
import org.ovirt.mobile.movirt.model.ConnectionInfo;
import org.ovirt.mobile.movirt.model.Console;
import org.ovirt.mobile.movirt.model.DataCenter;
import org.ovirt.mobile.movirt.model.Disk;
import org.ovirt.mobile.movirt.model.DiskAttachment;
import org.ovirt.mobile.movirt.model.Event;
import org.ovirt.mobile.movirt.model.Host;
import org.ovirt.mobile.movirt.model.Nic;
import org.ovirt.mobile.movirt.model.Snapshot;
import org.ovirt.mobile.movirt.model.SnapshotDisk;
import org.ovirt.mobile.movirt.model.SnapshotNic;
import org.ovirt.mobile.movirt.model.SnapshotVm;
import org.ovirt.mobile.movirt.model.StorageDomain;
import org.ovirt.mobile.movirt.model.Vm;
import org.ovirt.mobile.movirt.model.trigger.Trigger;
import org.ovirt.mobile.movirt.model.view.DiskAndAttachment;

public class UriMatcher extends OrmLiteUriMatcher {
    public UriMatcher(String authority) {
        super(authority);
    }

    @Override
    public void instantiate() {
        addClass(OVirtContract.PATH_VMS, Vm.class);
        addClass(OVirtContract.PATH_VM, Vm.class);

        addClass(OVirtContract.PATH_SNAPSHOT_VMS, SnapshotVm.class);
        addClass(OVirtContract.PATH_SNAPSHOT_VM, SnapshotVm.class);

        addClass(OVirtContract.PATH_CLUSTERS, Cluster.class);
        addClass(OVirtContract.PATH_CLUSTER, Cluster.class);

        addClass(OVirtContract.PATH_TRIGGERS, Trigger.class);
        addClass(OVirtContract.PATH_TRIGGER, Trigger.class);

        addClass(OVirtContract.PATH_EVENTS, Event.class);
        addClass(OVirtContract.PATH_EVENT, Event.class);

        addClass(OVirtContract.PATH_HOSTS, Host.class);
        addClass(OVirtContract.PATH_HOST, Host.class);

        addClass(OVirtContract.PATH_DATA_CENTERS, DataCenter.class);
        addClass(OVirtContract.PATH_DATA_CENTER, DataCenter.class);

        addClass(OVirtContract.PATH_STORAGE_DOMAINS, StorageDomain.class);
        addClass(OVirtContract.PATH_STORAGE_DOMAIN, StorageDomain.class);

        addClass(OVirtContract.PATH_CONNECTION_INFOS, ConnectionInfo.class);
        addClass(OVirtContract.PATH_CONNECTION_INFO, ConnectionInfo.class);

        addClass(OVirtContract.PATH_SNAPSHOTS, Snapshot.class);
        addClass(OVirtContract.PATH_SNAPSHOT, Snapshot.class);

        addClass(OVirtContract.PATH_DISKS, Disk.class);
        addClass(OVirtContract.PATH_DISK, Disk.class);

        addClass(OVirtContract.PATH_SNAPSHOT_DISKS, SnapshotDisk.class);
        addClass(OVirtContract.PATH_SNAPSHOT_DISK, SnapshotDisk.class);

        addClass(OVirtContract.PATH_NICS, Nic.class);
        addClass(OVirtContract.PATH_NIC, Nic.class);

        addClass(OVirtContract.PATH_SNAPSHOT_NICS, SnapshotNic.class);
        addClass(OVirtContract.PATH_SNAPSHOT_NIC, SnapshotNic.class);

        addClass(OVirtContract.PATH_CONSOLES, Console.class);
        addClass(OVirtContract.PATH_CONSOLE, Console.class);

        addClass(OVirtContract.PATH_DISK_ATTACHMENTS, DiskAttachment.class);
        addClass(OVirtContract.PATH_DISK_ATTACHMENT, DiskAttachment.class);

        addClass(OVirtContract.PATH_DISKS_AND_ATTACHMENTS, DiskAndAttachment.class);
        addClass(OVirtContract.PATH_DISKS_AND_ATTACHMENT, DiskAndAttachment.class);
    }

    public static UriDependencies.UriDependency[] getUriDependencies() {
        return new UriDependencies.UriDependency[]{
                new UriDependencies.UriDependency(OVirtContract.DiskAttachment.CONTENT_URI,
                        OVirtContract.DiskAndAttachment.CONTENT_URI),
                new UriDependencies.UriDependency(OVirtContract.Disk.CONTENT_URI,
                        OVirtContract.DiskAndAttachment.CONTENT_URI)
        };
    }
}
