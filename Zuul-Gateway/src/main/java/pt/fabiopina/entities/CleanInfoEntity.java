package pt.fabiopina.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CleanInfoEntity {
    private Timestamp startDate, endDate;
    private long startTimestamp, endTimestamp, duration;
    private String sourceIpAddr, destinyMicroservice, destinyInstance, destinyIpAddr, method, url, sourcePort, destinyPort, statusCode;
}
