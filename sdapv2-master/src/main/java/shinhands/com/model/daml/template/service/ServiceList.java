package shinhands.com.model.daml.template.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceList {
    private String userid;
    private String address;
    private List<String> tokenSymbols;
}
