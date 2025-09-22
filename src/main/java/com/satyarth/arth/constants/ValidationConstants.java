package com.satyarth.arth.constants;

public class ValidationConstants {
    public static final class DocumentNumber {
        public static final int MIN_LENGTH = 8;
        public static final String BLANK_MESSAGE = "INVALID DOCUMENT NUMBER";
        public static final String SIZE_MESSAGE = "UNACCEPTABLE DOCUMENT NUMBER LENGTH";
    }

    public static final class OperationType {
        public static final int MIN_OP_TYPE_ID = 1;
        public static final int MAX_OP_TYPE_ID = 4;
        public static final String INVALID_OP_TYPE = "UNSUPPORTED OPERATION TYPE";
    }
}