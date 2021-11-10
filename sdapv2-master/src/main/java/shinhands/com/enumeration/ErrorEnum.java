package shinhands.com.enumeration;

public class ErrorEnum {
    public enum User {
        ALREADY_USER_EXIST
        ;
    }

    public enum Kyc {
        UNAUTHORIZED, // 인증전
        AUTHORIZED, // 완료
        DENIED, // 거절
        UNDER_EXAMINATION; // 심사중
    }
}
