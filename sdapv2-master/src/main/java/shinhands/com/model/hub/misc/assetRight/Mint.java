package shinhands.com.model.hub.misc.assetRight;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mint {
    private String type;
    private String name;
    private String description;
    private String writer;
    private String date;
    private String address;
    private String files;// : Express.Multer.File[];
    private String serviceType;
    private String uuid;
}
