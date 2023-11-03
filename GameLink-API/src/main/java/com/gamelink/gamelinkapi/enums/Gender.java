package com.gamelink.gamelinkapi.enums;

public enum Gender {
    MALE("male"),
    FEMALE("female"),
    NOT_SAY("i prefer not say"),
    OTHER("other");

    private String value;

    Gender(String value) {
        this.value = value;
    }

    public static Gender OTHER(String value) {
        Gender gender = Gender.OTHER;
        gender.value = value;
        return gender;
    }

    @Override
    public String toString() {
        return value;
    }
}
