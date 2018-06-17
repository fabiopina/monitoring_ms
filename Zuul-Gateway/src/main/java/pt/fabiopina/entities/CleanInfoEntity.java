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
public class CleanInfoEntity {
    private long startTime, endTime, duration;
    private String sourceIpAddr, destinyMicroservice, destinyInstance, destinyIpAddr, destinyFunction;
    private int sourcePort;
}
