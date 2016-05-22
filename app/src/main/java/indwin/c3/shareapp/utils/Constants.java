package indwin.c3.shareapp.utils;

/**
 * Created by rock on 5/11/16.
 */
public class Constants {


    public static final String ACCOUNT_DELETED = "account_deleted";
    public static final String DISABLE_ADD = "disableAdd";

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
