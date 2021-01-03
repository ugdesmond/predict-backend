package com.example.demo.utils;

public enum Constant {
    TEST(1);

    private int total;

    Constant(int s) {
        total = s;
    }


    public enum STATUS {
        PENDING("PENDING"),
        COMPLETED("COMPLETED"),
        NOT_COMPLETED("NOT_COMPLETED"),
        ASSIGNED("ASSIGNED"),
        CANCELLED("CANCELLED"),
        PAID("PAID"),
        NOT_PAID("NOT_PAID"),
        ACTIVATED("ACTIVATED"),
        DELETED("DELETED");

        public String value;

        STATUS(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum ROLES {
        USER("USER"),
        ADMIN("COMPLETED");

        public String value;

        ROLES(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
