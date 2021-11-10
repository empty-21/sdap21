package shinhands.com.model.hub.misc.templete;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Create {
    private String name;
    private String base;
    private List<String> behaviors;
}
