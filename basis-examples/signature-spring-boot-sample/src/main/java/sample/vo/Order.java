package sample.vo;

import lombok.Data;

/**
 * @ClassName : Order
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/21 4:15
 * @Desc :
 */
@Data
public class Order{
    private String name;
    private Integer amount;
    private Double price;
}