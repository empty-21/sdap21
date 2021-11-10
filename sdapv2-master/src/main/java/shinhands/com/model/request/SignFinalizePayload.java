package shinhands.com.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignFinalizePayload {
    private String user;
    private String serviceName;
    private String tokenSymbol;
    private Boolean YN;
}
