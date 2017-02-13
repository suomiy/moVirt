package org.ovirt.mobile.movirt.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.ovirt.mobile.movirt.rest.RestEntityWrapper;
import org.ovirt.mobile.movirt.rest.dto.common.Mac;

/**
 * Created by yixin on 2015/3/24.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Nic implements RestEntityWrapper<org.ovirt.mobile.movirt.model.Nic> {
    public String id;
    public String name;
    public boolean linked;
    public Mac mac;
    public boolean plugged;

    public org.ovirt.mobile.movirt.model.Nic toEntity() {
        org.ovirt.mobile.movirt.model.Nic nic = new org.ovirt.mobile.movirt.model.Nic();
        nic.setId(id);
        nic.setName(name);
        nic.setLinked(linked);
        if (mac != null) {
            nic.setMacAddress(mac.address);
        }
        nic.setPlugged(plugged);

        return nic;
    }
}
