package shinhands.com.beans;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.extern.slf4j.Slf4j;
import shinhands.com.model.message.DefaultResult;

@ApplicationScoped
@Named("responseHelper")
@RegisterForReflection
@Slf4j
public class ResponseHelper {

    public DefaultResult generateBadResponse(String message) {
        DefaultResult result = new DefaultResult();
        result.setMessage(message);
        result.setCode(-1);
        log.error("=>",message);
        return result;
    }

    public DefaultResult generateSuccessResponse() {
        DefaultResult result = new DefaultResult();
        result.setMessage("정상적으로 처리되었습니다.");
        return result;
    }

    public DefaultResult generateResponse(Object data) {
        //[2021-08-10 13:23:02] null인경우 data:null 정보가 응답값으로 표현되지 못함
        DefaultResult result = new DefaultResult();
        result.setData(data);
        return result;
    }
}
