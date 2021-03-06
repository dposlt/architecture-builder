package dvoraka.archbuilder.prototype.actioncoordinator.model;

import dvoraka.archbuilder.prototype.actioncoordinator.action.order.OrderStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(
        name = "ORDER_DATA"
)
public class Order {

    @Id
    @GeneratedValue
    private long id;

    private long userId;
    private long itemId;
    private OrderStatus status;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", status=" + status +
                '}';
    }
}
