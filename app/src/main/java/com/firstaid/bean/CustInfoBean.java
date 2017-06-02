package com.firstaid.bean;

/**
 * Created by tony on 2016/3/24.
 */
public class CustInfoBean extends BaseBean {

    public CustInfo data;

    public class CustInfo {

        public String activationFlag;
        public String activationTime;
        public String activationUser;
        public String ambulanceStatus;
        public String city;
        public String cityName;
        public String county;
        public String creditScore;
        public String cusType;
        public String deviceToken;
        public Expand expand;
        public String hasSent;
        public String headImg;
        public String id;
        public String idCardNo;
        public String invitationCode;
        public String lastLoginIp;
        public String lastLoginTime;
        public String latitude;
        public String lockExpireTime;
        public String lockFlag;
        public String lockTime;
        public String lockUser;
        public String longitude;
        public String nickname;
        public String phone;
        public String province;
        public String provinceName;
        public String realName;
        public String registerTime;
        public String remark;
        public String scoreYear;
        public String serviceExpireTime;
        public String statusReason;
        public String updateTime;
        public String updateUser;
        public String yearScore;

        public class Expand {
            public String accountName;
            public String audit;
            public String authenticationAmount;
            public String bankBranch;
            public String bankName;
            public String bindingPhone;
            public String checkStatus;
            public String checkTime;
            public String cusBasicInfo;
            public String cusId;
            public String fixTelephone;
            public String id;
            public String legalPersonCertificate;
            public String organizationCertificate;
            public String submiTime;
            public String submitCustId;
            public String submitCustMobile;
            public String submitCustName;
            public String technicalPerson;
            public String tollLevel;
            public String unitAddress;
            public String unitName;
            public String useCity;
            public String useProvince;
        }


    }
}
