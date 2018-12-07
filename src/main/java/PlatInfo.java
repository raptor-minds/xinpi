public final class PlatInfo {

    private String companyId;

    private String companyName;

    private String regCapital;

    private String regCountry;

    private String regProv;

    private String regCity;

    private String operateCountry;

    private String operateProv;

    private String operateCity;

    private String regDate;

    private String regDist;

    private String fundDepository;

    private String safetyCertLevel;


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRegCapital() {
        return regCapital;
    }

    public void setRegCapital(String regCapital) {
        this.regCapital = regCapital;
    }

    public String getRegCountry() {
        return regCountry;
    }

    public void setRegCountry(String regCountry) {
        this.regCountry = regCountry;
    }

    public String getRegProv() {
        return regProv;
    }

    public void setRegProv(String regProv) {
        this.regProv = regProv;
    }

    public String getRegCity() {
        return regCity;
    }

    public void setRegCity(String regCity) {
        this.regCity = regCity;
    }

    public String getOperateCountry() {
        return operateCountry;
    }

    public void setOperateCountry(String operateCountry) {
        this.operateCountry = operateCountry;
    }

    public String getOperateProv() {
        return operateProv;
    }

    public void setOperateProv(String operateProv) {
        this.operateProv = operateProv;
    }

    public String getOperateCity() {
        return operateCity;
    }

    public void setOperateCity(String operateCity) {
        this.operateCity = operateCity;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegDist() {
        return regDist;
    }

    public void setRegDist(String regDist) {
        this.regDist = regDist;
    }

    public String getFundDepository() {
        return fundDepository;
    }

    public void setFundDepository(String fundDepository) {
        this.fundDepository = fundDepository;
    }

    public String getSafetyCertLevel() {
        return safetyCertLevel;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setSafetyCertLevel(String safetyCertLevel) {
        this.safetyCertLevel = safetyCertLevel;
    }

    @Override
    public String toString() {
        return "PlatInfo{" +
                "companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", regCapital='" + regCapital + '\'' +
                ", regCountry='" + regCountry + '\'' +
                ", regProv='" + regProv + '\'' +
                ", regCity='" + regCity + '\'' +
                ", operateCountry='" + operateCountry + '\'' +
                ", operateProv='" + operateProv + '\'' +
                ", operateCity='" + operateCity + '\'' +
                ", regDate='" + regDate + '\'' +
                ", regDist='" + regDist + '\'' +
                ", fundDepository='" + fundDepository + '\'' +
                ", safetyCertLevel='" + safetyCertLevel + '\'' +
                '}';
    }
}
