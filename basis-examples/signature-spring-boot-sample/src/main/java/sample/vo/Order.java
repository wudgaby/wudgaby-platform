package sample.vo;

import lombok.Data;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/21 4:15
 * @Desc :
 */
@Data
public class Order{
    private String name;
    private Integer amount;
    private Double price;
}