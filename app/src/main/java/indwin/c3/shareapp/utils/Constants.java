package indwin.c3.shareapp.utils;

/**
 * Created by rock on 5/11/16.
 */
public class Constants {


    public static final String ACCOUNT_DELETED = "account_deleted";
    public static final String DISABLE_ADD = "disableAdd";


    public static enum IMAGE_TYPE {
        COLLEGE_ID {
            @Override
            public String toString() {
                return "collegeId";
            }
        }, ADDRESS_PROOF {
            @Override
            public String toString() {
                return "addressProof";
            }
        },
        PAN {
            @Override
            public String toString() {
                return "pan";
            }
        }, BANK_PROOF {
            @Override
            public String toString() {
                return "bankProof";
            }
        }, BANK_STMNTS {
            @Override
            public String toString() {
                return "bankStmnts";
            }
        }, MARKSHEETS {
            @Override
            public String toString() {
                return "marksheets";
            }
        }
    }

    public static enum STATUS {
        APPLIED {
            @Override
            public String toString() {
                return "applied";
            }
        }, APPROVED {
            @Override
            public String toString() {
                return "approved";
            }
        }, DECLINED {
            @Override
            public String toString() {
                return "declined";
            }
        }, WAITLISTED {
            @Override
            public String toString() {
                return "waitlisted";
            }
        }
    }


    public static enum VERIFICATION_STATUS {
        UNDER_VEIFICATION {
            @Override
            public String toString() {
                return "Under verification...";
            }
        }, VERIFIED {
            @Override
            public String toString() {
                return "Verified...";
            }
        }, REJECTED {
            @Override
            public String toString() {
                return "Rejected...";
            }
        }
    }


    public static enum BANDS {
        FLASH {
            @Override
            public String toString() {
                return "Flash";
            }
        }, OXYGEN {
            @Override
            public String toString() {
                return "Oxygen";
            }
        }, SILICON {
            @Override
            public String toString() {
                return "Silicon";
            }
        }, PALLADIUM {
            @Override
            public String toString() {
                return "Palladium";
            }
        }, KRYPTON {
            @Override
            public String toString() {
                return "Krypton";
            }
        }
    }


}
