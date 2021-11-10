package shinhands.com.model.hub.misc.policy.userifs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailOptions {
    private From from;
    private String to;
    private String html;
    private String text;
    private String subject;
    private String markdown;
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class From {
    private String name;
    private String address;
}
