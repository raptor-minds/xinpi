public enum CompanyInfo {
    NAME("公司简称", 0),
    REG_COUNTRY("公司注册地址国家", 1),
    REG_CITY("公司注册地所在市", 2),
    REG_PRO("公司注册地址省份", 3),
    REG_CAPITAL("公司注册资本(万元)", 4),
    OPE_COUNTRY("公司经营地所在国家", 5),
    OPE_PRO("公司经营所在地省份", 6),
    OPE_CITY("公司经营所在地市", 7),
    REG_DATE("公司成立时间", 8),
    FUND_DEPOSITORY("资金存管银行全称", 9),
    SAFETY_CERT_LEVEL("信息安全测评认证信息", 10),
    REG_DIGS_CODE("法人机构注册地区代码", 12),
    ID("公司ID", 11);

    private final String name;
    private final int id;

    CompanyInfo(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static CompanyInfo formValue(String name) {
        for (CompanyInfo c : CompanyInfo.values()) {
            if (c.name.equals(name)){
                return c;
            }
        }
        return ID;
    }


}
