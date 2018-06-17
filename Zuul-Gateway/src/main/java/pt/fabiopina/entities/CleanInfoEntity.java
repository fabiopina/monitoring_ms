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
    long startTime, endTime, duration;
    String sourceIpAddr, destinyMicroservice, destinyInstance, destinyIpAddr, destinyFunction;
    int sourcePort;
}
