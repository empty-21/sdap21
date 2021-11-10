package shinhands.com.model.daml.template.erc721;

import java.util.List;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import shinhands.com.model.daml.template.BaseERCPayload;

@Data
@SuperBuilder(toBuilder = true)
// @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ERC721Payload extends BaseERCPayload {
    private List<Object> tokenOwners;
    private List<Object> holderTokens;
    private Number tokenIndex = 1;
    private String baseURI;
    private List<Object> tokenURIs;
}
