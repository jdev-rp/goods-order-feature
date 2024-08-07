package kr.project.goodsorderfeature.api.domain.entity;

import kr.project.goodsorderfeature.api.domain.code.DeliveryStatus;
import kr.project.goodsorderfeature.api.domain.vo.Address;
import kr.project.goodsorderfeature.core.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "DELIVERY")
public class DeliveryEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @Comment("배달아이디")
    private Long deliveryId;

    @Comment("배달상태")
    @Column(name = "DELIVERY_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @AttributeOverrides({
        @AttributeOverride(name = "address", column = @Column(name = "ADDRESS")),
        @AttributeOverride(name = "addressDetail", column = @Column(name = "ADDRESS_DETAIL")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "ZIP_CODE"))
    })
    @Embedded
    private Address address;

    @Setter
    @OneToOne(mappedBy = "delivery")
    private OrderEntity orderEntity;

    public static DeliveryEntity ofAddressForReady(Address address) {
        return DeliveryEntity.builder()
                .address(address)
                .deliveryStatus(DeliveryStatus.READY)
                .build();
    }

    public boolean isComplete() {
        return DeliveryStatus.COMPLETE == this.deliveryStatus;
    }

    public void changeComplete() {
        this.deliveryStatus = DeliveryStatus.COMPLETE;
        this.orderEntity.complete();
    }

}