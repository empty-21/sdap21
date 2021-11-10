package shinhands.com.enumeration;

public class Stat {
    public enum User {
        UNKNOWN,
        ACTIVATE, // 활성
        DEACTIVATE, // 비활성
        DELETED, // 탈퇴
        TEMPLOCK, // 임시 제한
        REQJOIN, // 재가입
        REQINITPASSWORD // 패스워드 변경필요
        ;
    }

    public enum Kyc {
        UNAUTHORIZED, // 인증전
        AUTHORIZED, // 완료
        DENIED, // 거절
        UNDER_EXAMINATION; // 심사중
    }
}
