package pt.fabiopina.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class RawInfoEntity {
    private String remoteAddr, method, requestURL, instance;
    private int remotePort;
    private long startTime, endTime;

}
