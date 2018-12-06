public final class InstituteInBank {
    private final String shortName;

    private final String fullName;

    private final String signDate;

    private final String onlineDate;

    private final String storageName;

    private final String storageVersion;

    private final String id;

    private InstituteInBank(Builder builder) {
        this.shortName = builder.shortName;
        this.fullName = builder.fullName;
        this.signDate = builder.signDate;
        this.onlineDate = builder.onlineDate;
        this.storageName = builder.storageName;
        this.storageVersion = builder.storageVersion;
        this.id = builder.id;
    }

    public static class Builder {
        private String shortName;

        private String fullName;

        private String signDate;

        private String onlineDate;

        private String storageName;

        private String storageVersion;

        private String id;

        Builder () {

        }

        Builder id (String id) {
            this.id = id;
            return this;
        }

        Builder shortName (String shortName) {
            this.shortName = shortName;
            return this;
        }

        Builder fullName (String fullName) {
            this.fullName = fullName;
            return this;
        }

        Builder signDate (String date) {
            this.signDate = date;
            return this;
        }

        Builder onlineDate(String onlineDate) {
            this.onlineDate = onlineDate;
            return this;
        }

        Builder storageName (String storageName) {
            this.storageName = storageName;
            return this;
        }

        Builder storageVersion (String storageVersion) {
            this.storageVersion = storageVersion;
            return this;
        }

        InstituteInBank build() {
            return new InstituteInBank(this);
        }


    }


    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSignDate() {
        return signDate;
    }

    public String getOnlineDate() {
        return onlineDate;
    }

    public String getStorageName() {
        return storageName;
    }

    public String getStorageVersion() {
        return storageVersion;
    }


    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "institute short name is : " + shortName +  "id " + id + " full name is " + fullName +
                " signature date is :" + signDate + " online date is : " +
                onlineDate + " storage name is : " + storageName + " storage version is :" + storageVersion;
    }
}
