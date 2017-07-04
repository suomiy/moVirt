package org.ovirt.mobile.movirt.ui.events;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.ovirt.mobile.movirt.R;
import org.ovirt.mobile.movirt.auth.account.AccountDeletedException;
import org.ovirt.mobile.movirt.model.Event;
import org.ovirt.mobile.movirt.model.base.OVirtAccountNamedEntity;
import org.ovirt.mobile.movirt.provider.OVirtContract;
import org.ovirt.mobile.movirt.provider.ProviderFacade;
import org.ovirt.mobile.movirt.rest.ConnectivityHelper;
import org.ovirt.mobile.movirt.rest.Response;
import org.ovirt.mobile.movirt.ui.ProgressBarResponse;

import java.util.List;

@EFragment(R.layout.fragment_base_entity_list)
public abstract class NamedEntityResumeSyncableEventsFragment extends EventsFragment {

    @InstanceState
    protected boolean resumeSynced = false;

    @Bean
    protected ConnectivityHelper connectivityHelper;

    private String entityName;

    @Override
    public void onResume() {
        super.onResume();
        if (!resumeSynced) {
            resumeSynced = true;
            try {
                if (connectivityHelper.isNetworkAvailable() && environmentStore.getAccountPropertiesManager(account).hasAdminPermissions()) {
                    onRefresh();
                }
            } catch (AccountDeletedException ignore) {
            }
        }
    }

    @Override
    protected void appendQuery(ProviderFacade.QueryBuilder<Event> query) {
        super.appendQuery(query);
        try {
            if (!environmentStore.getAccountPropertiesManager(account).hasAdminPermissions()) {
                query.where(OVirtContract.Event.ID, "-1"); // prevent showing accidental events so the list is empty for unsupported user role
            }
        } catch (AccountDeletedException ignore) {
        }
    }

    protected <E extends OVirtAccountNamedEntity> String getEntityName(Class<E> clazz, String id) {
        if (entityName == null) {
            synchronized (this) {
                if (entityName == null) {
                    E entity = provider.query(clazz).id(id).first();
                    if (entity != null) {
                        entityName = entity.getName();
                    }
                }
            }
        }

        return entityName;
    }

    @Background
    @Override
    public void onRefresh() {
        try {
            if (environmentStore.getAccountPropertiesManager(account).hasAdminPermissions()) {
                sync(new ProgressBarResponse<>(this));
            } else {
                environmentStore.getMessageHelper(account).showToast("User entity events are not supported.");
                hideProgressBar();
            }
        } catch (AccountDeletedException ignore) {
        }
    }

    protected abstract void sync(Response<List<Event>> response);
}
