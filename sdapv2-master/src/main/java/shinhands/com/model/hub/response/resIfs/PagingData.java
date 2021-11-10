package shinhands.com.model.hub.response.resIfs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagingData {
    private Number totalCnt;
    private Number page;
    private Number rows;
    private List<Object> data;
}
