package com.microservices.demo.producer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "outbox")
@AllArgsConstructor
@NoArgsConstructor
public class OutboxItem {
    @Id
    private UUID id = UUID.randomUUID();
    @Column
    private String aggregateType;
    @Column
    private String aggregateId;
    @Column
    private String type;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column
    private String correlationId;
    @Column
    private String payload;

    public OutboxItem(String aggregateType, String aggregateId, String type, Date timestamp, String correlationId, String payload) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.type = type;
        this.timestamp = timestamp;
        this.correlationId = correlationId;
        this.payload = payload;
    }
}
