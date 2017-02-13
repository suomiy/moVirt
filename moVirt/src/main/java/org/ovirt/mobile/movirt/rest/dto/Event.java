package org.ovirt.mobile.movirt.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.ovirt.mobile.movirt.model.enums.EventSeverity;
import org.ovirt.mobile.movirt.rest.RestEntityWrapper;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements RestEntityWrapper<org.ovirt.mobile.movirt.model.Event> {
    public int id;
    public int code;
    public String description;
    public String severity;
    public long time;

    public IdRef vm;
    public IdRef host;
    public IdRef cluster;
    public IdRef data_center;
    public IdRef storage_domain;

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class IdRef {
        public String id;
    }

    @Override
    public org.ovirt.mobile.movirt.model.Event toEntity() {
        org.ovirt.mobile.movirt.model.Event event = new org.ovirt.mobile.movirt.model.Event();
        event.setId(id);
        event.setCode(code);
        event.setDescription(description);
        event.setSeverity(EventSeverity.fromString(severity));
        event.setTime(new Timestamp(time));

        if (vm != null) {
            event.setVmId(vm.id);
        }
        if (host != null) {
            event.setHostId(host.id);
        }
        if (cluster != null) {
            event.setClusterId(cluster.id);
        }
        if (storage_domain != null) {
            event.setStorageDomainId(storage_domain.id);
        }
        if (data_center != null) {
            event.setDataCenterId(data_center.id);
        }
        return event;
    }
}
