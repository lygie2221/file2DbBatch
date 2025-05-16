package de.lygie.batch.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "`references`")
public class Reference {
    @Column(name = "referred_by", length = 11)
    private String referredBy;

    @Column(name = "referring_object", length = 16)
    private String referringObject;

    @Column(name = "relationship", length = 14)
    private String relationship;

    @Column(name = "object_name", length = 11)
    private String objectName;

    @Column(name = "object_type", length = 11)
    private String objectType;

    @Column(name = "legacy_object", length = 13)
    private String legacyObject;

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getReferringObject() {
        return referringObject;
    }

    public void setReferringObject(String referringObject) {
        this.referringObject = referringObject;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getLegacyObject() {
        return legacyObject;
    }

    public void setLegacyObject(String legacyObject) {
        this.legacyObject = legacyObject;
    }

}